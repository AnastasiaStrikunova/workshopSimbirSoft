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

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("myApp");

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
                                String.format(resourceBundle.getString("exceptionTaskNotExist"), id)
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
                        String.format(resourceBundle.getString("exceptionTaskNotExist"), id)
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
                        String.format(resourceBundle.getString("exceptionTaskNotExist"), id)
                )
        );
        taskRepository.delete(taskEntity);
    }

    @Override
    @Transactional
    public TaskResponseDto changeStatus(Long id, Long idStatus) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format(resourceBundle.getString("exceptionTaskNotExist"), id)
                )
        );
        StatusEntity statusEntity = statusRepository.findById(idStatus).orElseThrow(
                () -> new NotFoundException(
                        String.format(resourceBundle.getString("exceptionStatusNotExist"), idStatus)
                )
        );
        String newTitle = statusEntity.getTitle();
        checkStatus(taskEntity, newTitle);
        taskEntity.setStatusEntity(statusEntity);
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
                .or(filterByAuthor(taskRequestDto.getAuthor()))
                .or(filterByPerformer(taskRequestDto.getPerformer()))
                .or(filterByStartTime(taskRequestDto.getStartTime()))
                .or(filterByEndTime(taskRequestDto.getEndTime()))
                .or(filterByIdProject(taskRequestDto.getIdProject()))
                .or(filterByIdStatus(taskRequestDto.getIdStatus()))
                .or(filterByIdRelease(taskRequestDto.getIdRelease()))
        ));
        List<TaskResponseDto> taskResponseDtoList = new ArrayList<>();
        taskEntityList.forEach(taskEntity -> taskResponseDtoList.add(taskMapper.TaskEntityToTaskResponseDto(taskEntity)));
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
        list.get(0).forEach(s -> map.put(s, list.get(1).get(list.get(0).indexOf(s))));
        return add(mapToTaskRequestDto(map));
    }

    private TaskEntity setFieldsTaskEntity(TaskEntity updatedEntity, TaskEntity sourceTaskEntity) {
        Optional.ofNullable(updatedEntity.getTitle()).ifPresent(sourceTaskEntity::setTitle);
        Optional.ofNullable(updatedEntity.getPriority()).ifPresent(sourceTaskEntity::setPriority);
        Optional.ofNullable(updatedEntity.getAuthorEntity()).ifPresent(sourceTaskEntity::setAuthorEntity);
        Optional.ofNullable(updatedEntity.getPerformerEntity()).ifPresent(sourceTaskEntity::setPerformerEntity);
        Optional.ofNullable(updatedEntity.getStartTime()).ifPresent(sourceTaskEntity::setStartTime);
        Optional.ofNullable(updatedEntity.getEndTime()).ifPresent(sourceTaskEntity::setEndTime);
        Optional.ofNullable(updatedEntity.getProjectEntity()).ifPresent(project -> {
            ProjectEntity projectEntity = projectRepository.findById(project.getIdProject()).orElseThrow(
                    () -> new NotFoundException(
                            String.format(resourceBundle.getString("exceptionProjectNotExist"), updatedEntity.getProjectEntity().getIdProject())
                    )
            );
            if (!projectEntity.getComplete()){
                sourceTaskEntity.setProjectEntity(updatedEntity.getProjectEntity());
            } else {
                throw new NotFoundException(
                        String.format(resourceBundle.getString("exceptionTaskProjectCompleted"), projectEntity.getIdProject())
                );
            }
        });
        Optional.ofNullable(updatedEntity.getStatusEntity()).ifPresent(status -> changeStatus(sourceTaskEntity.getIdTask(), status.getIdStatus()));
        Optional.ofNullable(updatedEntity.getReleaseEntity()).ifPresent(sourceTaskEntity::setReleaseEntity);
        return sourceTaskEntity;
    }

    private void checkStatus(TaskEntity taskEntity, String newTitle) {
        if (newTitle.equalsIgnoreCase(Status.IN_PROGRESS.name())) {
            Optional.ofNullable(
                    Optional.ofNullable(taskEntity.getProjectEntity()).orElseThrow(() -> new NotFoundException(resourceBundle.getString("exceptionTaskNotAttachedToProject")))
                    ).map(ProjectEntity::getStatusEntity).orElseThrow(() -> new NotFoundException(resourceBundle.getString("exceptionTaskProjectNoStatus")));
            if (taskEntity.getProjectEntity().getStatusEntity().getTitle().equalsIgnoreCase(Status.BACKLOG.name())) {
                throw new NotFoundException(
                        String.format(resourceBundle.getString("exceptionTaskProjectNotStart"), taskEntity.getProjectEntity().getIdProject())
                );
            }
        }
    }

    private TaskRequestDto mapToTaskRequestDto(Map<String, String> map) throws ParseException {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        if (map.containsKey("title")) {
            taskRequestDto.setTitle(map.get("title"));
        }
        if (map.containsKey("priority")) {
            taskRequestDto.setPriority(map.get("priority"));
        }
        if (map.containsKey("authorEntity/idUser")) {
            taskRequestDto.setAuthor(Long.parseLong(map.get("authorEntity/idUser")));
        }
        if (map.containsKey("performerEntity/idUser")) {
            taskRequestDto.setPerformer(Long.parseLong(map.get("performerEntity/idUser")));
        }
        if (map.containsKey("startTime")) {
            String startTime = map.get("startTime").substring(0, 19);
            Date date = formatter.parse(startTime);
            taskRequestDto.setStartTime(date);
        }
        if (map.containsKey("endTime")) {
            String endTime = map.get("endTime").substring(0, 19);
            Date date = formatter.parse(endTime);
            taskRequestDto.setEndTime(date);
        }
        if (map.containsKey("projectEntity/idProject")) {
            taskRequestDto.setIdProject(Long.parseLong(map.get("projectEntity/idProject")));
        }
        if (map.containsKey("statusEntity/idStatus")) {
            taskRequestDto.setIdStatus(Long.parseLong(map.get("statusEntity/idStatus")));
        }
        if (map.containsKey("releaseEntity/idRelease")) {
            taskRequestDto.setIdRelease(Long.parseLong(map.get("releaseEntity/idRelease")));
        }
        return taskRequestDto;
    }
}
