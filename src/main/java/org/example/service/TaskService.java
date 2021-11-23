package org.example.service;

import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.TaskEntity;
import org.example.object.Status;

import java.util.List;

/**
 * Этот интерфейс указывает какие методы необходимы для реализации бизнес-логики <i>задач</i>
 * @author Anastasia Strikunova
 */
public interface TaskService {
    List<TaskResponseDto> findAll();
    TaskResponseDto findById(Long id);
    TaskResponseDto add(TaskRequestDto taskRequestDto);
    TaskResponseDto change(Long id, TaskRequestDto taskRequestDto);
    void delete(Long id);

    /** Изменяет задачу, находит по названию
     * @param taskRequestDto  ДТО задачи
     * @return <code>TaskResponseDto</code>
     */
    TaskResponseDto changeByTitle(TaskRequestDto taskRequestDto);

    /** Изменяет статус задачи
     * @param id айди задачи
     * @param status  тело статуса
     * @return <code>TaskResponseDto</code>
     */
    TaskResponseDto changeStatus(Long id, Status status);

    /** Возвращает все задачи заданного проекта
     * @param id айди проекта
     * @return <code>List</code><<code>TaskEntity</code>>
     */
    List<TaskEntity> findAllByIdProject(Long id);

    /** Возвращает количество задач, не завершившихся в заданный релиз
     * @return <code>Integer</code>
     */
    Integer countAfterDateRelease();

    /** Поиск задач по фильтрам
     * @param taskRequestDto ДТО задачи
     * @return <code>List</code><<code>TaskResponseDto</code>>
     */
    List<TaskResponseDto> findByFilter(TaskRequestDto taskRequestDto);
}
