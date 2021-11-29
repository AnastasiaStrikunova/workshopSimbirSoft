package org.example.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.Status;
import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.*;
import org.example.exception.NotFoundException;
import org.example.mapper.TaskMapper;
import org.example.repository.ProjectRepository;
import org.example.repository.StatusRepository;
import org.example.repository.TaskRepository;
import org.example.service.ReleaseService;
import org.example.service.TaskService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static org.example.specification.TaskSpecification.*;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final StatusRepository statusRepository;

    private final ReleaseService releaseService;

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository, StatusRepository statusRepository, ReleaseService releaseService) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.statusRepository = statusRepository;
        this.releaseService = releaseService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> findAll() {
        List<TaskEntity> taskEntityList = new ArrayList<>(taskRepository.findAll());
        List<TaskResponseDto> taskResponseDtoList = new ArrayList<>();
        Stream<TaskEntity> stream = taskEntityList.stream();
        stream.forEach(taskEntity -> taskResponseDtoList.add(taskMapper.TaskEntityToTaskResponseDto(taskEntity)));
        return taskResponseDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto findById(Long id) {
        return taskMapper.TaskEntityToTaskResponseDto(
                taskRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find task with id = %d", id)
                        )
                )
        );
    }

    @Override
    @Transactional
    public TaskResponseDto add(TaskRequestDto taskRequestDto) {
        TaskEntity taskEntity = taskMapper.TaskRequestDtoToTaskEntity(taskRequestDto);
        taskRepository.save(taskEntity);
        return taskMapper.TaskEntityToTaskResponseDto(taskEntity);
    }

    @Override
    @Transactional
    public TaskResponseDto change(Long id, TaskRequestDto taskRequestDto) {
        TaskEntity entity = taskMapper.TaskRequestDtoToTaskEntity(taskRequestDto);
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find task with id = %d", id)
                )
        );
        taskRepository.save(setFieldsTaskEntity(entity, taskEntity));
        return taskMapper.TaskEntityToTaskResponseDto(taskEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find task with id = %d", id)
                )
        );
        taskRepository.delete(taskEntity);
    }

    @Override
    @Transactional
    public TaskResponseDto changeStatus(Long id, TaskRequestDto taskRequestDto) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find task with id = %d", id)
                )
        );
        String newTitle;
        Long idStatus;
        if (taskRequestDto.getStatusEntity().getTitle() != null) {
            newTitle = taskRequestDto.getStatusEntity().getTitle();
            idStatus = statusRepository.findByTitle(newTitle).getIdStatus();
        } else {
            idStatus = taskRequestDto.getStatusEntity().getIdStatus();
            newTitle = statusRepository.findById(idStatus).orElseThrow(
                    () -> new NotFoundException(
                            String.format("Could not find status with id = %d", idStatus)
                    )
            ).getTitle();
        }
        checkStatus(taskEntity, newTitle);
        taskEntity.setStatusEntity(
                statusRepository.findById(idStatus).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find status with id = %d", idStatus)
                        )
                )
        );
        return taskMapper.TaskEntityToTaskResponseDto(taskEntity);
    }

    @Override
    @Transactional
    public List<TaskEntity> findAllByIdProject(Long id) {
        return taskRepository.findAllByProjectEntity(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer countAfterDateAssignedRelease(Long id) {
        return taskRepository.countAllByEndTimeAfter(releaseService.findById(id).getEndTime());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponseDto> findByFilter(TaskRequestDto taskRequestDto) {
        List<TaskEntity> taskEntityList = new ArrayList<>(taskRepository.findAll(filterByTitle(taskRequestDto.getTitle())
                .or(filterByPriority(taskRequestDto.getPriority()))
                //.or(filterByAuthor(taskRequestDto.getAuthorEntity().getIdUser())
                //.or(filterByPerformer(taskRequestDto.getPerformerEntity().getIdUser()))
                .or(filterByStartTime(taskRequestDto.getStartTime()))
                .or(filterByEndTime(taskRequestDto.getEndTime()))
                //.or(filterByIdProject(taskRequestDto.getProjectEntity().getIdProject()))
                //.or(filterByIdStatus(taskRequestDto.getStatusEntity().getIdStatus())))
                //.or(filterByIdRelease(taskRequestDto.getReleaseEntity().getIdRelease()))
        ));
        List<TaskResponseDto> taskResponseDtoList = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntityList) {
            taskResponseDtoList.add(taskMapper.TaskEntityToTaskResponseDto(taskEntity));
        }
        return taskResponseDtoList;
    }

    @Override
    public TaskResponseDto readFile(MultipartFile multipartFile) throws IOException, ParseException {
        CSVParser csvParser = CSVParser.parse(multipartFile.getInputStream(), StandardCharsets.UTF_8, CSVFormat.EXCEL);
        List<List<String>> list = new ArrayList<>();
        for (CSVRecord csvRecord : csvParser) {
            list.add(csvRecord.toList());
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < list.get(0).size(); i++) {
            map.put(list.get(0).get(i), list.get(1).get(i));
        }
        return add(mapToTaskRequestDto(map));
    }

    private TaskEntity setFieldsTaskEntity(TaskEntity updatedEntity, TaskEntity sourceTaskEntity) {
        if (updatedEntity.getTitle() != null) sourceTaskEntity.setTitle(updatedEntity.getTitle());
        if (updatedEntity.getPriority() != null) sourceTaskEntity.setPriority(updatedEntity.getPriority());
        if (updatedEntity.getAuthorEntity() != null) sourceTaskEntity.setAuthorEntity(updatedEntity.getAuthorEntity());
        if (updatedEntity.getPerformerEntity() != null) sourceTaskEntity.setPerformerEntity(updatedEntity.getPerformerEntity());
        if (updatedEntity.getStartTime() != null) sourceTaskEntity.setStartTime(updatedEntity.getStartTime());
        if (updatedEntity.getEndTime() != null) sourceTaskEntity.setEndTime(updatedEntity.getEndTime());
        if (updatedEntity.getProjectEntity() != null) {
            ProjectEntity projectEntity = projectRepository.findById(updatedEntity.getProjectEntity().getIdProject()).orElseThrow(
                    () -> new NotFoundException(
                            String.format("Could not find project with id = %d", updatedEntity.getProjectEntity().getIdProject())
                    )
            );
            if (!projectEntity.getComplete()){
                sourceTaskEntity.setProjectEntity(updatedEntity.getProjectEntity());
            } else {
                throw new NotFoundException(
                        String.format("Unable to add task because project with id = %d completed", projectEntity.getIdProject())
                );
            }
        }
        if (updatedEntity.getStatusEntity() != null) sourceTaskEntity.setStatusEntity(updatedEntity.getStatusEntity());
        if (updatedEntity.getReleaseEntity() != null) sourceTaskEntity.setReleaseEntity(updatedEntity.getReleaseEntity());
        return sourceTaskEntity;
    }

    private void checkStatus(TaskEntity taskEntity, String newTitle) {
        if (newTitle.equalsIgnoreCase(Status.IN_PROGRESS.toString())) {
            if (taskEntity.getProjectEntity() == null) {
                throw new NotFoundException(
                        "Unable to change status. The task is not attached to the project"
                );
            }
            if (taskEntity.getProjectEntity().getStatusEntity() == null) {
                throw new NotFoundException(
                        "Unable to change status. The project to which the task is attached has no status"
                );
            }
            if (taskEntity.getProjectEntity().getStatusEntity().getTitle().equalsIgnoreCase(Status.BACKLOG.toString())) {
                throw new NotFoundException(
                        String.format("Project with id = %d was created, but did not start", taskEntity.getProjectEntity().getIdProject())
                );
            }
        }
    }

    private TaskRequestDto mapToTaskRequestDto(Map<String, String> map) throws ParseException {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        if (map.containsKey("title")){
            taskRequestDto.setTitle(map.get("title"));
        }
        if (map.containsKey("priority")){
            taskRequestDto.setPriority(map.get("priority"));
        }
        if (map.containsKey("authorEntity/idUser")){
            UserEntity userEntity = new UserEntity();
            userEntity.setIdUser(Long.parseLong(map.get("authorEntity/idUser")));
            taskRequestDto.setAuthorEntity(userEntity);
        }
        if (map.containsKey("performerEntity/idUser")){
            UserEntity userEntity = new UserEntity();
            userEntity.setIdUser(Long.parseLong(map.get("performerEntity/idUser")));
            taskRequestDto.setPerformerEntity(userEntity);
        }
        if (map.containsKey("startTime")){
            String startTime = map.get("startTime").substring(0, 19);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = formatter.parse(startTime);
            taskRequestDto.setStartTime(date);
        }
        if (map.containsKey("endTime")){
            String endTime = map.get("endTime").substring(0, 19);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = formatter.parse(endTime);
            taskRequestDto.setEndTime(date);
        }
        if (map.containsKey("projectEntity/idProject")){
            ProjectEntity projectEntity = new ProjectEntity();
            projectEntity.setIdProject(Long.parseLong(map.get("projectEntity/idProject")));
            taskRequestDto.setProjectEntity(projectEntity);
        }
        if (map.containsKey("statusEntity/idStatus")){
            StatusEntity statusEntity = new StatusEntity();
            statusEntity.setIdStatus(Long.parseLong(map.get("statusEntity/idStatus")));
            taskRequestDto.setStatusEntity(statusEntity);
        }
        if (map.containsKey("releaseEntity/idRelease")){
            ReleaseEntity releaseEntity = new ReleaseEntity();
            releaseEntity.setIdRelease(Long.parseLong(map.get("releaseEntity/idRelease")));
            taskRequestDto.setReleaseEntity(releaseEntity);
        }
        return taskRequestDto;
    }
}
