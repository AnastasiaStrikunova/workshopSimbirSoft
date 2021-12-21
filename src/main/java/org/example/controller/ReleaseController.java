package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ReleaseRequestDto;
import org.example.dto.ReleaseResponseDto;
import org.example.service.ReleaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Релизы", description = "Управление релизами")
@RestController
@RequestMapping("${api-base-url}/release")
@SecurityRequirement(name = "bearerAuth")
public class ReleaseController {
    private final ReleaseService releaseService;

    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }


    @Operation(summary = "Получить все релизы")
    @GetMapping
    public ResponseEntity<List<ReleaseResponseDto>> findAll(){
        return ResponseEntity.ok(releaseService.findAll());
    }

    @Operation(summary = "Получить конкретный релиз")
    @GetMapping("/{id}")
    public ResponseEntity<ReleaseResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(releaseService.findById(id));
    }

    @Operation(summary = "Добавить релиз")
    @PostMapping
    public ResponseEntity<ReleaseResponseDto> add(@RequestBody ReleaseRequestDto releaseRequestDto){
        return ResponseEntity.ok(releaseService.add(releaseRequestDto));
    }

    @Operation(summary = "Изменить релиз")
    @PutMapping("/{id}")
    public ResponseEntity<ReleaseResponseDto> change(@PathVariable Long id, @RequestBody ReleaseRequestDto releaseRequestDto){
        return ResponseEntity.ok(releaseService.change(id, releaseRequestDto));
    }

    @Operation(summary = "Удалить релиз")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        releaseService.delete(id);
    }
}
