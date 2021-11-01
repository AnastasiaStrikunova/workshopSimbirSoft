package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.StatusRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Статусы", description = "Управление статусами")
@RestController
public class StatusController {
    //private final StatusServiceImpl statusService;

    @Operation(summary = "Получить все статусы")
    @GetMapping("/status")
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить конкретный статус")
    @GetMapping("/status/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить статус")
    @PostMapping("/status")
    public ResponseEntity<Object> add(@RequestBody StatusRequestDto statusRequestDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Изменить статус")
    @PutMapping("/status/{id}")
    public ResponseEntity<Object> change(@PathVariable Long id, @RequestBody StatusRequestDto statusRequestDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить статус")
    @DeleteMapping("/status/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }
}
