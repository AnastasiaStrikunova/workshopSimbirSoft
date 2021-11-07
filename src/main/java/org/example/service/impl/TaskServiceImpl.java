package org.example.service.impl;

import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.TaskEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.TaskMapper;
import org.example.repository.TaskRepository;
import org.example.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List findAll() {
        List taskEntityList = new ArrayList(taskRepository.findAll());
        List taskResponseDtoList = new ArrayList();
        for (Object o : taskEntityList) {
            taskResponseDtoList.add(TaskMapper.INSTANCE.TaskEntityToTaskResponseDto((TaskEntity) o));
        }
        return taskResponseDtoList;
    }

    @Transactional(readOnly = true)
    public TaskResponseDto findById(Long id) {
        return TaskMapper.INSTANCE.TaskEntityToTaskResponseDto(
                taskRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find object with id = %d",id)
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
                        String.format("Could not find object with id = %d",id)
                )
        );
        if (entity.getTitle() != null) taskEntity.setTitle(entity.getTitle());
        if (entity.getPriority() != null) taskEntity.setPriority(entity.getPriority());
        if (entity.getAuthor() != null) taskEntity.setAuthor(entity.getAuthor());
        if (entity.getPerformer() != null) taskEntity.setPerformer(entity.getPerformer());
        if (entity.getStartTime() != null) taskEntity.setStartTime(entity.getStartTime());
        if (entity.getEndTime() != null) taskEntity.setEndTime(entity.getEndTime());
        if (entity.getIdProject() != null) taskEntity.setIdProject(entity.getIdProject());
        if (entity.getIdStatus() != null) taskEntity.setIdStatus(entity.getIdStatus());
        if (entity.getIdRelease() != null) taskEntity.setIdRelease(entity.getIdRelease());
        taskRepository.save(taskEntity);
        return TaskMapper.INSTANCE.TaskEntityToTaskResponseDto(taskEntity);
    }

    @Transactional
    public void delete(Long id) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find object with id = %d",id)
                )
        );
        taskRepository.delete(taskEntity);
    }
}
