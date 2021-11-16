package org.example.service;

import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.TaskEntity;
import org.example.object.Status;

import java.util.List;

public interface TaskService {
    List<TaskResponseDto> findAll();
    TaskResponseDto findById(Long id);
    TaskResponseDto add(TaskRequestDto taskRequestDto);
    TaskResponseDto change(Long id, TaskRequestDto taskRequestDto);
    void delete(Long id);

    TaskResponseDto changeByTitle(TaskRequestDto taskRequestDto);
    TaskResponseDto changeStatus(Long id, Status status);

    List<TaskEntity> findAllByIdProject(Long id);
    Integer countAfterDateRelease();
}
