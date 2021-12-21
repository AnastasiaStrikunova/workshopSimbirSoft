package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Tag(name="Задачи", description = "Управление задачами")
@RestController
@RequestMapping("${api-base-url}/task")
@SecurityRequirement(name = "bearerAuth")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Получить все задачи", description = "Позволяет получить все задачи")
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> findAll(){
        return ResponseEntity.ok(taskService.findAll());
    }

    @Operation(summary = "Получить конкретную задачу")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> findById(@PathVariable@Parameter(description = "Идентификатор задачи") Long id){
        return ResponseEntity.ok(taskService.findById(id));
    }

    @Operation(summary = "Добавить задачу")
    @PostMapping
    public ResponseEntity<TaskResponseDto> add(@RequestBody TaskRequestDto taskRequestDto){
        return ResponseEntity.ok(taskService.add(taskRequestDto));
    }

    @Operation(summary = "Изменить задачу")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> change(@PathVariable Long id, @RequestBody TaskRequestDto taskRequestDto){
        return ResponseEntity.ok(taskService.change(id, taskRequestDto));
    }

    @Operation(summary = "Удалить задачу")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ taskService.delete(id); }

    @ExceptionHandler(IOException.class)
    public void handler(){
        System.out.println();
    }

    @Operation(summary = "Изменить статус задачи")
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> changeStatus(@PathVariable Long id, @RequestParam Long idStatus){
        return ResponseEntity.ok(taskService.changeStatus(id, idStatus));
    }

    @Operation(summary = "Получить количество задач, не завершившихся в заданный релиз")
    @GetMapping("/release/{id}")
    public ResponseEntity<Integer> countAfterDateRelease(@PathVariable Long id){
        return ResponseEntity.ok(taskService.countAfterDateAssignedRelease(id));
    }

    @Operation(summary = "Поиск задач по фильтрам")
    @PostMapping("/filter")
    public ResponseEntity<List<TaskResponseDto>> findByFilter(@RequestBody TaskRequestDto taskRequestDto){
        return ResponseEntity.ok(taskService.findByFilter(taskRequestDto));
    }

    @Operation(summary = "Создать задачу с помощью CSV-файла")
    @PostMapping("/file")
    public ResponseEntity<TaskResponseDto> addViaFile(@RequestPart("multipartFile") MultipartFile multipartFile) throws IOException, ParseException {
        return ResponseEntity.ok(taskService.readFile(multipartFile));
    }
}
