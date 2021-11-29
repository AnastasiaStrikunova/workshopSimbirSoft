package org.example.service.impl;

import org.example.Status;
import org.example.dto.ProjectRequestDto;
import org.example.dto.ProjectResponseDto;
import org.example.entity.ProjectEntity;
import org.example.entity.TaskEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.ProjectMapper;
import org.example.repository.ProjectRepository;
import org.example.service.ProjectService;
import org.example.service.TaskService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    private final TaskService taskService;

    private final ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    public ProjectServiceImpl(ProjectRepository projectRepository, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.taskService = taskService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> findAll() {
        List<ProjectEntity> projectEntityList = new ArrayList<>(projectRepository.findAll());
        List<ProjectResponseDto> projectResponseDtoList = new ArrayList<>();
        Stream<ProjectEntity> stream = projectEntityList.stream();
        stream.forEach(projectEntity -> projectResponseDtoList.add(projectMapper.ProjectEntityToProjectResponseDto(projectEntity)));
        return projectResponseDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectResponseDto findById(Long id) {
        return projectMapper.ProjectEntityToProjectResponseDto(
                projectRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find project with id = %d", id)
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
                        String.format("Could not find project with id = %d", id)
                )
        );
        if (projectRequestDto.getTitle() != null) projectEntity.setTitle(projectRequestDto.getTitle());
        if (projectRequestDto.getComplete() != null) projectEntity.setComplete(projectRequestDto.getComplete());
        if (projectRequestDto.getStatusEntity() != null) projectEntity.setStatusEntity(projectRequestDto.getStatusEntity());
        if (projectRequestDto.getUserEntity() != null) projectEntity.setUserEntity(projectRequestDto.getUserEntity());
        return projectMapper.ProjectEntityToProjectResponseDto(projectEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find project with id = %d", id)
                )
        );
        projectRepository.delete(projectEntity);
    }

    @Override
    @Transactional
    public ProjectResponseDto completeProject(Long id) {
        ProjectEntity projectEntity = projectRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find project with id = %d", id)
                )
        );
        List<TaskEntity> taskEntityList = taskService.findAllByIdProject(id);
        for (TaskEntity taskEntity : taskEntityList) {
            checkTaskStatus(taskEntity);
        }
        projectEntity.setComplete(true);
        return projectMapper.ProjectEntityToProjectResponseDto(projectEntity);
    }

    private void checkTaskStatus(TaskEntity taskEntity) {
        if (taskEntity.getStatusEntity() == null) {
            throw new NotFoundException(
                    "The task has no status assigned"
            );
        } else if (!taskEntity.getStatusEntity().getTitle().equalsIgnoreCase(Status.DONE.toString())) {
            throw new NotFoundException(
                    "The project cannot be completed because it has unfinished tasks"
            );
        }
    }

}

