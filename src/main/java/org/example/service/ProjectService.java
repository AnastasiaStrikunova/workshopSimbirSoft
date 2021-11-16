package org.example.service;

import org.example.dto.ProjectRequestDto;
import org.example.dto.ProjectResponseDto;

import java.util.List;

public interface ProjectService {
    List<ProjectResponseDto> findAll();
    ProjectResponseDto findById(Long id);
    ProjectResponseDto add(ProjectRequestDto projectRequestDto);
    ProjectResponseDto change(Long id, ProjectRequestDto projectRequestDto);
    void delete(Long id);

    ProjectResponseDto completeProject(Long id);
}
