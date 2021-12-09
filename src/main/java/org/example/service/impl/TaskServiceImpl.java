package org.example.service.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.enums.Status;
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

import static org.example.specification.TaskSpecification.*;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final StatusRepository statusRepository;

    private final ReleaseService releaseService;

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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
        taskEntityList.forEach(taskEntity -> taskResponseDtoList.add(taskMapper.TaskEntityToTaskResponseDto(taskEntity)));
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

        if (taskRequestDto.getIdStatus() == null) {
            throw new NotFoundException("id_status is null");
        }
        String newTitle = statusRepository.findById(taskRequestDto.getIdStatus()).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find status with id = %d", taskRequestDto.getIdStatus())
                )
        ).getTitle();
        checkStatus(taskEntity, newTitle);
        taskEntity.setStatusEntity(
                statusRepository.findById(taskRequestDto.getIdStatus()).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find status with id = %d", taskRequestDto.getIdStatus())
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
    public List<TaskResponseDto> findByFilter(String title, String priority, Long author, Long performer, Date startTime,
                                              Date endTime, Long idProject, Long idStatus, Long idRelease) {
        List<TaskEntity> taskEntityList = new ArrayList<>(taskRepository.findAll(filterByTitle(title)
                .or(filterByPriority(priority))
                .or(filterByAuthor(author))
                .or(filterByPerformer(performer))
                .or(filterByStartTime(startTime))
                .or(filterByEndTime(endTime))
                .or(filterByIdProject(idProject))
                .or(filterByIdStatus(idStatus))
                .or(filterByIdRelease(idRelease))
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
        if (newTitle.equalsIgnoreCase(Status.IN_PROGRESS.name())) {
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
            if (taskEntity.getProjectEntity().getStatusEntity().getTitle().equalsIgnoreCase(Status.BACKLOG.name())) {
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
            taskRequestDto.setAuthor(Long.parseLong(map.get("authorEntity/idUser")));
        }
        if (map.containsKey("performerEntity/idUser")){
            taskRequestDto.setPerformer(Long.parseLong(map.get("performerEntity/idUser")));
        }
        if (map.containsKey("startTime")){
            String startTime = map.get("startTime").substring(0, 19);
            Date date = formatter.parse(startTime);
            taskRequestDto.setStartTime(date);
        }
        if (map.containsKey("endTime")){
            String endTime = map.get("endTime").substring(0, 19);
            Date date = formatter.parse(endTime);
            taskRequestDto.setEndTime(date);
        }
        if (map.containsKey("projectEntity/idProject")){
            taskRequestDto.setIdProject(Long.parseLong(map.get("projectEntity/idProject")));
        }
        if (map.containsKey("statusEntity/idStatus")){
            taskRequestDto.setIdStatus(Long.parseLong(map.get("statusEntity/idStatus")));
        }
        if (map.containsKey("releaseEntity/idRelease")){
            taskRequestDto.setIdRelease(Long.parseLong(map.get("releaseEntity/idRelease")));
        }
        return taskRequestDto;
    }
}
