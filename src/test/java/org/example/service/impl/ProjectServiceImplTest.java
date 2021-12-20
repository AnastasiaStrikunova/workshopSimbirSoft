package org.example.service.impl;

import org.example.dto.ProjectRequestDto;
import org.example.entity.ProjectEntity;
import org.example.entity.StatusEntity;
import org.example.entity.TaskEntity;
import org.example.entity.UserEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.ProjectMapper;
import org.example.proxy.PaymentClient;
import org.example.repository.ProjectRepository;
import org.example.repository.StatusRepository;
import org.example.service.TaskService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceImplTest {
    private final ProjectEntity projectEntity1 = new ProjectEntity(1L, "title1", false, new StatusEntity(1L), new UserEntity(1L));
    private final ProjectEntity projectEntity2 = new ProjectEntity(2L, "title2", true, null, null);

    @InjectMocks
    private ProjectServiceImpl projectService;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private TaskService taskService;
    @Mock
    private PaymentClient paymentClient;

    private final ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("myApp");

    @Test
    public void findAll() {
        Mockito.when(projectRepository.findAll()).thenReturn(Stream.of(projectEntity1, projectEntity2).collect(Collectors.toList()));
        var actual = projectService.findAll();
        Assert.assertEquals(projectMapper.ProjectEntityToProjectResponseDto(projectEntity1), actual.get(0));
        Assert.assertEquals(projectMapper.ProjectEntityToProjectResponseDto(projectEntity2), actual.get(1));
    }

    @Test
    public void findById() {
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(projectEntity1));
        Assert.assertEquals(projectMapper.ProjectEntityToProjectResponseDto(projectEntity1), projectService.findById(1L));
    }

    @Test
    public void add() {
        Mockito.when(projectRepository.save(Mockito.any())).thenReturn(projectEntity1);
        var actual = projectService.add(new ProjectRequestDto("title1", false, 1L, 1L));
        projectEntity1.setIdProject(null);
        Assert.assertEquals(projectMapper.ProjectEntityToProjectResponseDto(projectEntity1).getIdProject(), actual.getIdProject());
    }

    @Test
    public void change() {
        Mockito.when(projectRepository.findById(Mockito.any())).thenReturn(Optional.of(projectEntity1));
        Mockito.when(projectRepository.save(Mockito.any())).thenReturn(projectEntity1);
        var actual = projectService.change(projectEntity1.getIdProject(), new ProjectRequestDto());
        Assert.assertEquals(projectMapper.ProjectEntityToProjectResponseDto(projectEntity1), actual);
    }

    @Test
    public void delete() {
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(projectEntity1));
        projectService.delete(projectEntity1.getIdProject());
    }

    @Test
    public void completeProjectNegative1() {
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(projectEntity1));
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatusEntity(null);
        Mockito.when(taskService.findAllByIdProject(Mockito.anyLong())).thenReturn(Stream.of(taskEntity).collect(Collectors.toList()));
        try {
            projectService.completeProject(1L);
        } catch (NotFoundException e) {
            Assert.assertEquals(resourceBundle.getString("exceptionProjectNoStatus"), e.getMessage());
        }
    }

    @Test
    public void completeProjectNegative2() {
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(projectEntity1));
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatusEntity(new StatusEntity(1L, "not_done"));
        Mockito.when(taskService.findAllByIdProject(Mockito.anyLong())).thenReturn(Stream.of(taskEntity).collect(Collectors.toList()));
        try {
            projectService.completeProject(1L);
        } catch (NotFoundException e) {
            Assert.assertEquals(resourceBundle.getString("exceptionProjectUnfinishedTasks"), e.getMessage());
        }
    }

    @Test
    public void completeProject() {
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(projectEntity1));
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setStatusEntity(new StatusEntity(1L, "done"));
        Mockito.when(taskService.findAllByIdProject(Mockito.anyLong())).thenReturn(Stream.of(taskEntity).collect(Collectors.toList()));
        var actual = projectService.completeProject(1L);
        Assert.assertEquals(true, actual.getComplete());
    }

    @Test
    public void startProjectNegative1() {
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(projectEntity1));
        Mockito.when(paymentClient.isPaid(Mockito.anyLong())).thenReturn(ResponseEntity.ok(false));
        try {
            projectService.startProject(1L);
        } catch (NotFoundException e) {
            Assert.assertEquals(resourceBundle.getString("exceptionProjectNotPaid"), e.getMessage());
        }
    }

    @Test
    public void startProject() {
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(projectEntity1));
        Mockito.when(paymentClient.isPaid(Mockito.anyLong())).thenReturn(ResponseEntity.ok(true));
        Mockito.when(statusRepository.findByTitle(Mockito.any())).thenReturn(new StatusEntity(2L, "start"));
        var actual = projectService.startProject(1L);
        Assert.assertEquals((Long) 2L, actual.getIdStatus());
    }

}
