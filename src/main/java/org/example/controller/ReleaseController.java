package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ReleaseRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Релизы", description = "Управление релизами")
@RestController
public class ReleaseController {
    //private final ReleaseServiceImpl releaseService;

    @Operation(summary = "Получить все релизы")
    @GetMapping("/release")
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить конкретный релиз")
    @GetMapping("/release/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить релиз")
    @PostMapping("/release")
    public ResponseEntity<Object> add(@RequestBody ReleaseRequestDto releaseRequestDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Изменить релиз")
    @PutMapping("/release/{id}")
    public ResponseEntity<Object> change(@PathVariable Long id, @RequestBody ReleaseRequestDto releaseRequestDto){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить релиз")
    @DeleteMapping("/release/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id){
        return ResponseEntity.ok().build();
    }
}
