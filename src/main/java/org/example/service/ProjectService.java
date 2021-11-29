package org.example.service;

import org.example.dto.ProjectRequestDto;
import org.example.dto.ProjectResponseDto;

import java.util.List;

/**
 * Этот интерфейс указывает какие методы необходимы для реализации бизнес-логики <i>проектов</i>
 * @author Anastasia Strikunova
 */
public interface ProjectService {
    List<ProjectResponseDto> findAll();
    ProjectResponseDto findById(Long id);
    ProjectResponseDto add(ProjectRequestDto projectRequestDto);
    ProjectResponseDto change(Long id, ProjectRequestDto projectRequestDto);
    void delete(Long id);

    /** Завершение выполнения проекта
     * @param id проекта
     * @return <code>ProjectResponseDto</code>
     */
    ProjectResponseDto completeProject(Long id);
}
