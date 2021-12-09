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

    /** Завершение выполнения проекта. Проект не должен быть закрыт, если хотя бы одна задача на доске не в статусе DONE
     * @param id проекта
     * @return <code>ProjectResponseDto</code>
     */
    ProjectResponseDto completeProject(Long id);
    /** Старт проекта. Проект может стартовать, только если заказчик произвел оплату
     * @param id проекта
     * @return <code>ProjectResponseDto</code>
     */
    ProjectResponseDto startProject(Long id);
}
