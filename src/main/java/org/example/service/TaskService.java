package org.example.service;

import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.TaskEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
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

    /** Изменяет статус задачи
     * @param id айди задачи
     * @param taskRequestDto  тело задачи
     * @return <code>TaskResponseDto</code>
     */
    TaskResponseDto changeStatus(Long id, TaskRequestDto taskRequestDto);

    /** Возвращает все задачи заданного проекта
     * @param id айди проекта
     * @return <code>List</code><<code>TaskEntity</code>>
     */
    List<TaskEntity> findAllByIdProject(Long id);

    /** Возвращает количество задач, не завершившихся в заданный релиз
     * @return <code>Integer</code>
     */
    Integer countAfterDateAssignedRelease(Long id);

    /** Поиск задач по фильтрам
     * @param taskRequestDto ДТО задачи
     * @return <code>List</code><<code>TaskResponseDto</code>>
     */
    List<TaskResponseDto> findByFilter(TaskRequestDto taskRequestDto);

    TaskResponseDto readFile(MultipartFile multipartFile) throws IOException, ParseException;
}
