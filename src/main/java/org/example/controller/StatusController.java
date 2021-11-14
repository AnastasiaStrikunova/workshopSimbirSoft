package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.object.Status;
import org.example.service.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Статусы", description = "Управление статусами")
@RestController
@RequestMapping("/status")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @Operation(summary = "Получить все статусы")
    @GetMapping
    public ResponseEntity<List<Status>> findAll(){
        return ResponseEntity.ok(statusService.findAll());
    }

    @Operation(summary = "Получить конкретный статус")
    @GetMapping("/{id}")
    public ResponseEntity<Status> findById(@PathVariable Long id){
        return ResponseEntity.ok(statusService.findById(id));
    }

    @Operation(summary = "Добавить статус")
    @PostMapping
    public ResponseEntity<Status> add(@RequestBody Status status){
        return ResponseEntity.ok(statusService.add(status));
    }

    @Operation(summary = "Изменить статус")
    @PutMapping("/{id}")
    public ResponseEntity<Status> change(@PathVariable Long id, @RequestBody Status status){
        return ResponseEntity.ok(statusService.change(id, status));
    }

    @Operation(summary = "Удалить статус")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        statusService.delete(id);
    }
}
