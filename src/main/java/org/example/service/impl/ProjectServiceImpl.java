package org.example.service.impl;

import org.example.dto.ProjectRequestDto;
import org.example.dto.ProjectResponseDto;
import org.example.entity.ProjectEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.ProjectMapper;
import org.example.repository.ProjectRepository;
import org.example.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public List findAll() {
        List projectEntityList = new ArrayList(projectRepository.findAll());
        List projectResponseDtoList = new ArrayList();
        for (Object o : projectEntityList) {
            projectResponseDtoList.add(ProjectMapper.INSTANCE.ProjectEntityToProjectResponseDto((ProjectEntity) o));
        }
        return projectResponseDtoList;
    }

    @Transactional(readOnly = true)
    public ProjectResponseDto findById(Long id) {
        return ProjectMapper.INSTANCE.ProjectEntityToProjectResponseDto(
                projectRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find object with id = %d",id)
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
                        String.format("Could not find object with id = %d",id)
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
                        String.format("Could not find object with id = %d",id)
                )
        );
        projectRepository.delete(projectEntity);
    }

}

