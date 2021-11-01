package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ProjectRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Проекты", description = "Управление проектами")
@RestController
public class ProjectController {
    //private final ProjectServiceImpl projectService;

    @Operation(summary = "Получить все проекты")
    @GetMapping("/project")
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить конкретный проект")
    @GetMapping("/project/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить проект")
    @PostMapping("/project")
    public ResponseEntity<Object> add(@RequestBody ProjectRequestDto projectRequestDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Изменить проект")
    @PutMapping("/project/{id}")
    public ResponseEntity<Object> change(@PathVariable Long id, @RequestBody ProjectRequestDto projectRequestDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить проект")
    @DeleteMapping("/project/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }
}
