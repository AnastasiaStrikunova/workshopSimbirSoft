package org.example.service.impl;

import org.example.entity.UserEntity;
import org.example.enums.Status;
import org.example.dto.ProjectRequestDto;
import org.example.dto.ProjectResponseDto;
import org.example.entity.ProjectEntity;
import org.example.entity.StatusEntity;
import org.example.entity.TaskEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.ProjectMapper;
import org.example.proxy.PaymentClient;
import org.example.repository.ProjectRepository;
import org.example.repository.StatusRepository;
import org.example.service.ProjectService;
import org.example.service.TaskService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final StatusRepository statusRepository;

    private final TaskService taskService;

    private final ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    private final PaymentClient paymentClient;

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("myApp");

    public ProjectServiceImpl(ProjectRepository projectRepository, StatusRepository statusRepository, TaskService taskService, PaymentClient paymentClient) {
        this.projectRepository = projectRepository;
        this.statusRepository = statusRepository;
        this.taskService = taskService;
        this.paymentClient = paymentClient;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> findAll() {
        List<ProjectEntity> projectEntityList = new ArrayList<>(projectRepository.findAll());
        List<ProjectResponseDto> projectResponseDtoList = new ArrayList<>();
        projectEntityList.forEach(projectEntity -> projectResponseDtoList.add(projectMapper.ProjectEntityToProjectResponseDto(projectEntity)));
        return projectResponseDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto findById(Long id) {
        return projectMapper.ProjectEntityToProjectResponseDto(
                projectRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format(resourceBundle.getString("exceptionProjectNotExist"), id)
                        )
                )
        );
    }

    @Override
    @Transactional
    public ProjectResponseDto add(ProjectRequestDto projectRequestDto) {
        ProjectEntity projectEntity = projectMapper.ProjectRequestDtoToProjectEntity(projectRequestDto);
        projectRepository.save(projectEntity);
        return projectMapper.ProjectEntityToProjectResponseDto(projectEntity);
    }

    @Override
    @Transactional
    public ProjectResponseDto change(Long id, ProjectRequestDto projectRequestDto) {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format(resourceBundle.getString("exceptionProjectNotExist"), id)
                )
        );
        if (projectRequestDto.getTitle() != null) projectEntity.setTitle(projectRequestDto.getTitle());
        if (projectRequestDto.getComplete() != null) {
            if (projectRequestDto.getComplete()) {
                completeProject(id);
            } else {
                projectEntity.setComplete(false);
            }
        }
        if (projectRequestDto.getIdStatus() != null) projectEntity.setStatusEntity(new StatusEntity(projectRequestDto.getIdStatus()));
        if (projectRequestDto.getIdUser() != null) projectEntity.setUserEntity(new UserEntity(projectRequestDto.getIdUser()));
        return projectMapper.ProjectEntityToProjectResponseDto(projectEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format(resourceBundle.getString("exceptionProjectNotExist"), id)
                )
        );
        projectRepository.delete(projectEntity);
    }

    @Override
    @Transactional
    public ProjectResponseDto completeProject(Long id) {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format(resourceBundle.getString("exceptionProjectNotExist"), id)
                )
        );
        List<TaskEntity> taskEntityList = taskService.findAllByIdProject(id);
        taskEntityList.forEach(this::checkTaskStatus);
        projectEntity.setComplete(true);
        return projectMapper.ProjectEntityToProjectResponseDto(projectEntity);
    }

    @Override
    public ProjectResponseDto startProject(Long id) {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format(resourceBundle.getString("exceptionProjectNotExist"), id)
                )
        );
        checkPayment(id);
        StatusEntity statusEntity;
        if (statusRepository.findByTitle(Status.START.name()) != null) {
            statusEntity = statusRepository.findByTitle(Status.START.name());
        } else {
            statusEntity = new StatusEntity();
            statusEntity.setTitle(Status.START.name());
            statusRepository.save(statusEntity);
        }
        projectEntity.setStatusEntity(statusEntity);
        projectRepository.save(projectEntity);
        return projectMapper.ProjectEntityToProjectResponseDto(projectEntity);
    }

    private void checkTaskStatus(TaskEntity taskEntity) {
        if (taskEntity.getStatusEntity() == null) {
            throw new NotFoundException(
                    resourceBundle.getString("exceptionProjectNoStatus")
            );
        } else if (!taskEntity.getStatusEntity().getTitle().equalsIgnoreCase(Status.DONE.name())) {
            throw new NotFoundException(
                    resourceBundle.getString("exceptionProjectUnfinishedTasks")
            );
        }
    }

    private void checkPayment(Long id) {
        if (Boolean.FALSE.equals(paymentClient.isPaid(id).getBody())) {
            throw new NotFoundException(
                    resourceBundle.getString("exceptionProjectNotPaid")
            );
        }
    }

}

