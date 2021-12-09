package org.example.service;

import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.TaskEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
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

    /** Изменяет статус задачи. Задача может быть в статусе IN_PROGRESS, только когда стартовал проект.
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
     * @return <code>List</code><<code>TaskResponseDto</code>>
     */
    List<TaskResponseDto> findByFilter(String title, String priority, Long author, Long performer, Date startTime,
                                       Date endTime, Long idProject, Long idStatus, Long idRelease);

    /** Парсер файла
     * @param multipartFile файл
     * @throws IOException
     * @throws ParseException
     * @return <code>TaskResponseDto</code>
     */
    TaskResponseDto readFile(MultipartFile multipartFile) throws IOException, ParseException;
}
