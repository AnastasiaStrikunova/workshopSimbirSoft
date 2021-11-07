package org.example.service;

import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;

import java.util.List;

public interface TaskService {
    List<TaskResponseDto> findAll();
    TaskResponseDto findById(Long id);
    TaskResponseDto add(TaskRequestDto taskRequestDto);
    TaskResponseDto change(Long id, TaskRequestDto taskRequestDto);
    void delete(Long id);
}
