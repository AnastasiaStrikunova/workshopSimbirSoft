package org.example.service.impl;

import org.example.dto.ProjectRequestDto;
import org.example.dto.ProjectResponseDto;
import org.example.entity.ProjectEntity;
import org.example.entity.TaskEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.ProjectMapper;
import org.example.repository.ProjectRepository;
import org.example.service.ProjectService;
import org.example.service.StatusService;
import org.example.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    private final TaskService taskService;
    private final StatusService statusService;

    public ProjectServiceImpl(ProjectRepository projectRepository, TaskService taskService, StatusService statusService) {
        this.projectRepository = projectRepository;
        this.taskService = taskService;
        this.statusService = statusService;
    }

    @Transactional(readOnly = true)
    public List<ProjectResponseDto> findAll() {
        List<ProjectEntity> projectEntityList = new ArrayList<>(projectRepository.findAll());
        List<ProjectResponseDto> projectResponseDtoList = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntityList) {
            projectResponseDtoList.add(ProjectMapper.INSTANCE.ProjectEntityToProjectResponseDto(projectEntity));
        }
        return projectResponseDtoList;
    }

    @Transactional(readOnly = true)
    public ProjectResponseDto findById(Long id) {
        return ProjectMapper.INSTANCE.ProjectEntityToProjectResponseDto(
                projectRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find project with id = %d",id)
                        )
                )
        );
    }

    @Transactional
    public ProjectResponseDto add(ProjectRequestDto projectRequestDto) {
        ProjectEntity projectEntity = ProjectMapper.INSTANCE.ProjectRequestDtoToProjectEntity(projectRequestDto);
        projectRepository.save(projectEntity);
        return ProjectMapper.INSTANCE.ProjectEntityToProjectResponseDto(projectEntity);
    }

    @Transactional
    public ProjectResponseDto change(Long id, ProjectRequestDto projectRequestDto) {
        ProjectEntity entity = ProjectMapper.INSTANCE.ProjectRequestDtoToProjectEntity(projectRequestDto);
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find project with id = %d",id)
                )
        );
        if (entity.getTitle() != null) projectEntity.setTitle(entity.getTitle());
        if (entity.getComplete() != null) projectEntity.setComplete(entity.getComplete());
        if (entity.getIdStatus() != null) projectEntity.setIdStatus(entity.getIdStatus());
        if (entity.getIdUser() != null) projectEntity.setIdUser(entity.getIdUser());
        projectRepository.save(projectEntity);
        return ProjectMapper.INSTANCE.ProjectEntityToProjectResponseDto(projectEntity);
    }

    @Transactional
    public void delete(Long id) {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find project with id = %d",id)
                )
        );
        projectRepository.delete(projectEntity);
    }

    @Transactional
    public ProjectResponseDto completeProject(Long id) {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find project with id = %d",id)
                )
        );
        List<TaskEntity> taskEntityList = taskService.findAllByIdProject(id);
        String titleStatus;
        for (TaskEntity taskEntity : taskEntityList) {
            if (taskEntity.getIdStatus() != null) {
                titleStatus = (statusService.findById(taskEntity.getIdStatus())).getTitle();
                if (!titleStatus.toLowerCase(Locale.ROOT).equals("done")) {
                    throw new NotFoundException(
                            "The project cannot be completed because it has unfinished tasks"
                    );
                }
            } else {
                throw new NotFoundException(
                        "The task has no status assigned"
                );
            }
        }
        projectEntity.setComplete(true);
        projectRepository.save(projectEntity);
        return ProjectMapper.INSTANCE.ProjectEntityToProjectResponseDto(projectEntity);
    }

}

