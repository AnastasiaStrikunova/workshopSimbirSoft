package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.TaskRequestDto;
import org.example.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name="Задачи", description = "Управление задачами")
@RestController
@RequestMapping("/task")
public class TaskController {
    //private final TaskService taskService;

    @Operation(summary = "Получить все задачи", description = "Позволяет получить все задачи")
    @GetMapping
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить конкретную задачу")
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable@Parameter(description = "Идентификатор задачи") Long id){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить задачу")
    @PostMapping
    public TaskRequestDto add(@RequestBody TaskRequestDto taskRequestDto){
        /*
        TaskEntity task = new TaskEntity();
        task.set(taskRequest.get());
        TaskResponseDto taskResponse = new TaskResponseDto();
        taskResponse.set(task.get());
        return taskResponse;
         */
        return taskRequestDto;
    }

    @Operation(summary = "Изменить задачу")
    @PutMapping("/{id}")
    public ResponseEntity<Object> save(@RequestBody TaskRequestDto taskRequestDto, @PathVariable Long id){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить задачу")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){}

    @ExceptionHandler(IOException.class)
    public void handler(){
        System.out.println();
    }
}
