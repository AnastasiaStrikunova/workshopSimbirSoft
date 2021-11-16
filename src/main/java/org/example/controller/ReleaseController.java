package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.ReleaseRequestDto;
import org.example.dto.ReleaseResponseDto;
import org.example.entity.ReleaseEntity;
import org.example.mapper.ReleaseMapper;
import org.example.service.ReleaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Релизы", description = "Управление релизами")
@RestController
@RequestMapping("/release")
public class ReleaseController {
    private final ReleaseService releaseService;

    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @Operation(summary = "Получить все релизы")
    @GetMapping
    public ResponseEntity<List<ReleaseResponseDto>> findAll(){
        List<ReleaseEntity> releaseEntityList = new ArrayList<>(releaseService.findAll());
        List<ReleaseResponseDto> releaseResponseDtoList = new ArrayList<>();
        for (ReleaseEntity releaseEntity : releaseEntityList) {
            releaseResponseDtoList.add(ReleaseMapper.INSTANCE.ReleaseEntityToReleaseResponseDto(releaseEntity));
        }
        return ResponseEntity.ok(releaseResponseDtoList);
    }

    @Operation(summary = "Получить конкретный релиз")
    @GetMapping("/{id}")
    public ResponseEntity<ReleaseResponseDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(ReleaseMapper.INSTANCE.ReleaseEntityToReleaseResponseDto(releaseService.findById(id)));
    }

    @Operation(summary = "Добавить релиз")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody ReleaseRequestDto releaseRequestDto){
        return ResponseEntity.ok(
                ReleaseMapper.INSTANCE.ReleaseEntityToReleaseResponseDto(
                        releaseService.add(ReleaseMapper.INSTANCE.ReleaseRequestDtoToReleaseEntity(releaseRequestDto))
                )
        );
    }

    @Operation(summary = "Изменить релиз")
    @PutMapping("/{id}")
    public ResponseEntity<Object> change(@PathVariable Long id, @RequestBody ReleaseRequestDto releaseRequestDto){
        return ResponseEntity.ok(
                ReleaseMapper.INSTANCE.ReleaseEntityToReleaseResponseDto(
                        releaseService.change(id, ReleaseMapper.INSTANCE.ReleaseRequestDtoToReleaseEntity(releaseRequestDto))
                )
        );
    }

    @Operation(summary = "Удалить релиз")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        releaseService.delete(id);
    }
}
