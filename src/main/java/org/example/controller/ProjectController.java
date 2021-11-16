package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ProjectRequestDto;
import org.example.dto.ProjectResponseDto;
import org.example.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Проекты", description = "Управление проектами")
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Получить все проекты")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> findAll(){
        return ResponseEntity.ok(projectService.findAll());
    }

    @Operation(summary = "Получить конкретный проект")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(projectService.findById(id));
    }

    @Operation(summary = "Добавить проект")
    @PostMapping
    public ResponseEntity<ProjectResponseDto> add(@RequestBody ProjectRequestDto projectRequestDto){
        return ResponseEntity.ok(projectService.add(projectRequestDto));
    }

    @Operation(summary = "Изменить проект")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> change(@PathVariable Long id, @RequestBody ProjectRequestDto projectRequestDto){
        return ResponseEntity.ok(projectService.change(id, projectRequestDto));
    }

    @Operation(summary = "Удалить проект")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        projectService.delete(id);
    }

    @Operation(summary = "Завершить проект")
    @PutMapping("/isComplete/{id}")
    public ResponseEntity<ProjectResponseDto> completeProject(@PathVariable Long id){
        return ResponseEntity.ok(projectService.completeProject(id));
    }
}
