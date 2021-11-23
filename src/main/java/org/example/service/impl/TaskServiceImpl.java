package org.example.service.impl;

import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.ProjectEntity;
import org.example.entity.TaskEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.TaskMapper;
import org.example.object.Status;
import org.example.repository.ProjectRepository;
import org.example.repository.TaskRepository;
import org.example.service.ReleaseService;
import org.example.service.StatusService;
import org.example.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.example.specification.TaskSpecification.*;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;
    private final StatusService statusService;
    private final ReleaseService releaseService;

    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository, StatusService statusService, ReleaseService releaseService) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.statusService = statusService;
        this.releaseService = releaseService;
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> findAll() {
        List<TaskEntity> taskEntityList = new ArrayList<>(taskRepository.findAll());
        List<TaskResponseDto> taskResponseDtoList = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntityList) {
            taskResponseDtoList.add(TaskMapper.INSTANCE.TaskEntityToTaskResponseDto(taskEntity));
        }
        return taskResponseDtoList;
    }

    @Transactional(readOnly = true)
    public TaskResponseDto findById(Long id) {
        return TaskMapper.INSTANCE.TaskEntityToTaskResponseDto(
                taskRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find task with id = %d",id)
                        )
                )
        );
    }

    @Transactional
    public TaskResponseDto add(TaskRequestDto taskRequestDto) {
        TaskEntity taskEntity = TaskMapper.INSTANCE.TaskRequestDtoToTaskEntity(taskRequestDto);
        taskRepository.save(taskEntity);
        return TaskMapper.INSTANCE.TaskEntityToTaskResponseDto(taskEntity);
    }

    @Transactional
    public TaskResponseDto change(Long id, TaskRequestDto taskRequestDto) {
        TaskEntity entity = TaskMapper.INSTANCE.TaskRequestDtoToTaskEntity(taskRequestDto);
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find task with id = %d",id)
                )
        );
        taskRepository.save(setFieldsTaskEntity(entity, taskEntity));
        return TaskMapper.INSTANCE.TaskEntityToTaskResponseDto(taskEntity);
    }

    @Transactional
    public void delete(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find task with id = %d",id)
                )
        );
        taskRepository.delete(taskEntity);
    }

    @Transactional
    public TaskResponseDto changeByTitle(TaskRequestDto taskRequestDto) {
        TaskEntity entity = TaskMapper.INSTANCE.TaskRequestDtoToTaskEntity(taskRequestDto);
        List<TaskEntity> taskEntityList = taskRepository.findAllByTitle(entity.getTitle());
        if (taskEntityList.size()==1){
            TaskEntity taskEntity = taskEntityList.get(0);
            taskRepository.save(setFieldsTaskEntity(entity, taskEntity));
            return TaskMapper.INSTANCE.TaskEntityToTaskResponseDto(taskEntity);
        } else {
            throw new NotFoundException(
                    String.format("Created %d tasks with this title", taskEntityList.size())
            );
        }
    }

    @Transactional
    public TaskResponseDto changeStatus(Long id, Status status) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find task with id = %d",id)
                )
        );
        String newTitle = status.getTitle();
        if (newTitle!=null && newTitle.toLowerCase(Locale.ROOT).equals("in_progress")) {
            long idProject = taskEntity.getIdProject();
            ProjectEntity projectEntity = projectRepository.findById(idProject).orElseThrow(
                    () -> new NotFoundException(
                            String.format("Could not find project with id = %d",id)
                    )
            );
            long idStatus = projectEntity.getIdStatus();
            String titleProject = statusService.findById(idStatus).getTitle().toLowerCase(Locale.ROOT);
            if (titleProject.equals("created")) {
                throw new NotFoundException(
                        String.format("Project with id = %d was created, but did not start",idProject)
                );
            } else {
                if (status.getIdStatus()!=null) {
                    taskEntity.setIdStatus(statusService.findById(status.getIdStatus()).getIdStatus());
                } else {
                    status = statusService.add(status);
                    taskEntity.setIdStatus(status.getIdStatus());
                }
                return TaskMapper.INSTANCE.TaskEntityToTaskResponseDto(taskRepository.save(taskEntity));
            }
        } else if (status.getIdStatus()!=null) {
            taskEntity.setIdStatus(statusService.findById(status.getIdStatus()).getIdStatus());
        } else {
            status = statusService.add(status);
            taskEntity.setIdStatus(status.getIdStatus());
        }
        return TaskMapper.INSTANCE.TaskEntityToTaskResponseDto(taskRepository.save(taskEntity));
    }

    @Transactional
    public List<TaskEntity> findAllByIdProject(Long id) {
        return taskRepository.findAllByIdProject(id);
    }

    @Transactional(readOnly = true)
    public Integer countAfterDateRelease() {
        Date taskEndTime;
        Date releaseEndTime;
        int count = 0;
        List<TaskEntity> taskEntityList = new ArrayList<>(taskRepository.findAll());
        for (TaskEntity taskEntity : taskEntityList) {
            taskEndTime = taskEntity.getEndTime();
            if (taskEntity.getIdRelease() != null) {
                releaseEndTime = releaseService.findById(taskEntity.getIdRelease()).getEndTime();
                if (taskEndTime.after(releaseEndTime)){
                    count++;
                }
            }
        }
        return count;
    }

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
        for (TaskEntity taskEntity : taskEntityList) {
            taskResponseDtoList.add(TaskMapper.INSTANCE.TaskEntityToTaskResponseDto(taskEntity));
        }
        return taskResponseDtoList;
    }

    private TaskEntity setFieldsTaskEntity(TaskEntity updatedEntity, TaskEntity sourceTaskEntity){
        if (updatedEntity.getTitle() != null) sourceTaskEntity.setTitle(updatedEntity.getTitle());
        if (updatedEntity.getPriority() != null) sourceTaskEntity.setPriority(updatedEntity.getPriority());
        if (updatedEntity.getAuthor() != null) sourceTaskEntity.setAuthor(updatedEntity.getAuthor());
        if (updatedEntity.getPerformer() != null) sourceTaskEntity.setPerformer(updatedEntity.getPerformer());
        if (updatedEntity.getStartTime() != null) sourceTaskEntity.setStartTime(updatedEntity.getStartTime());
        if (updatedEntity.getEndTime() != null) sourceTaskEntity.setEndTime(updatedEntity.getEndTime());
        if (updatedEntity.getIdProject() != null) {
            ProjectEntity projectEntity = projectRepository.findById(updatedEntity.getIdProject()).orElseThrow(
                    () -> new NotFoundException(
                            String.format("Could not find project with id = %d",updatedEntity.getIdProject())
                    )
            );
            if (!projectEntity.getComplete()){
                sourceTaskEntity.setIdProject(updatedEntity.getIdProject());
            } else {
                throw new NotFoundException(
                        String.format("Unable to add task because project with id = %d completed", projectEntity.getIdProject())
                );
            }
        }
        if (updatedEntity.getIdStatus() != null) sourceTaskEntity.setIdStatus(updatedEntity.getIdStatus());
        if (updatedEntity.getIdRelease() != null) sourceTaskEntity.setIdRelease(updatedEntity.getIdRelease());
        return sourceTaskEntity;
    }
}
