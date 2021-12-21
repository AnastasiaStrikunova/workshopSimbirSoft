package org.example.service.impl;

import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.*;
import org.example.exception.NotFoundException;
import org.example.mapper.TaskMapper;
import org.example.repository.StatusRepository;
import org.example.repository.TaskRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceImplTest {
    private TaskEntity taskEntity1;
    private TaskEntity taskEntity2;
    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private StatusRepository statusRepository;

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("myApp");

    @Before
    public void init() {
        taskEntity1 = new TaskEntity(1L, "title1", "first",
                new UserEntity( 1L),new UserEntity(1L), new Date(), new Date(), new ProjectEntity(1L),
                new StatusEntity(1L), new ReleaseEntity(1L));
        taskEntity2 = new TaskEntity(2L, "title2", "second",
                new UserEntity(),new UserEntity(), new Date(), new Date(), new ProjectEntity(),
                new StatusEntity(), new ReleaseEntity());
    }

    @Test
    public void findAll() {
        Mockito.when(taskRepository.findAll()).thenReturn(Stream.of(taskEntity1, taskEntity2).collect(Collectors.toList()));
        var actual = taskService.findAll();
        Assert.assertEquals(taskMapper.TaskEntityToTaskResponseDto(taskEntity1), actual.get(0));
        Assert.assertEquals(taskMapper.TaskEntityToTaskResponseDto(taskEntity2), actual.get(1));
    }

    @Test
    public void findById() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(taskEntity1));
        Assert.assertEquals(taskMapper.TaskEntityToTaskResponseDto(taskEntity1), taskService.findById(1L));
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNegative() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        taskService.findById(taskEntity2.getIdTask());
    }

    @Test
    public void add() {
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(taskEntity1);
        var actual = taskService.add(new TaskRequestDto("title1", "first",  1L, 1L, new Date(), new Date(), 1L, 1L, 1L));
        taskEntity1.setIdTask(null);
        Assert.assertEquals(taskMapper.TaskEntityToTaskResponseDto(taskEntity1).getIdTask(), actual.getIdTask());
    }

    @Test
    public void change() {
        Mockito.when(taskRepository.findById(Mockito.any())).thenReturn(Optional.of(taskEntity1));
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(taskEntity1);
        var actual = taskService.change(taskEntity1.getIdTask(), new TaskRequestDto());
        Assert.assertEquals(taskMapper.TaskEntityToTaskResponseDto(taskEntity1), actual);
    }

    @Test
    public void delete() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(taskEntity1));
        taskService.delete(taskEntity1.getIdTask());
    }

    @Test(expected = NotFoundException.class)
    public void deleteNegative() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        taskService.delete(taskEntity1.getIdTask());
    }

    @Test
    public void changeStatus1() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity1));
        Mockito.when(statusRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new StatusEntity(2L,"status")));
        var actual = taskService.changeStatus(taskEntity1.getIdTask(), 2L);
        Assert.assertEquals((Long) 2L, actual.getIdStatus());
    }

    @Test
    public void changeStatus2() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setStatusEntity(new StatusEntity(3L, "not_backlog"));
        taskEntity1.setProjectEntity(projectEntity);
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity1));
        Mockito.when(statusRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new StatusEntity(2L,"in_progress")));
        var actual = taskService.changeStatus(taskEntity1.getIdTask(), 2L);
        Assert.assertEquals((Long) 2L, actual.getIdStatus());
    }

    @Test
    public void changeStatusNegative2() {
        taskEntity1.setProjectEntity(null);
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity1));
        Mockito.when(statusRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new StatusEntity(2L,"in_progress")));
        try {
            taskService.changeStatus(taskEntity1.getIdTask(), 2L);
        } catch (NotFoundException e) {
            Assert.assertEquals(resourceBundle.getString("exceptionTaskNotAttachedToProject"), e.getMessage());
        }
    }

    @Test
    public void changeStatusNegative3() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity1));
        Mockito.when(statusRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new StatusEntity(2L,"in_progress")));
        try {
            taskService.changeStatus(taskEntity1.getIdTask(), 2L);
        } catch (NotFoundException e) {
            Assert.assertEquals(resourceBundle.getString("exceptionTaskProjectNoStatus"), e.getMessage());
        }
    }

    @Test
    public void changeStatusNegative4() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setStatusEntity(new StatusEntity(3L, "backlog"));
        taskEntity1.setProjectEntity(projectEntity);
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity1));
        Mockito.when(statusRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new StatusEntity(2L,"in_progress")));
        try {
            taskService.changeStatus(taskEntity1.getIdTask(), 2L);
        } catch (NotFoundException e) {
            Assert.assertEquals(String.format(resourceBundle.getString("exceptionTaskProjectNotStart"), (Object) null), e.getMessage());
        }
    }

    @Test
    public void readFile() throws IOException, ParseException {
        MultipartFile multipartFile = new MockMultipartFile("new.csv", new FileInputStream("src/test/java/org/example/new.csv"));
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(Mockito.any());
        var actual = taskService.readFile(multipartFile);
        var expected = new TaskResponseDto(null, "task1", "1", 1L, 1L, null, null, 1L, 3L, 1L);
        Assert.assertEquals(expected, actual);
    }

}
