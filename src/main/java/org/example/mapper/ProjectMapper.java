package org.example.mapper;

import org.example.dto.ProjectRequestDto;
import org.example.dto.ProjectResponseDto;
import org.example.entity.*;
import org.mapstruct.*;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ProjectMapper {
    @AfterMapping
    default void setEntity(@MappingTarget ProjectEntity projectEntity, ProjectRequestDto projectRequestDto){
        if (projectRequestDto.getIdStatus() != null) projectEntity.setStatusEntity(new StatusEntity(projectRequestDto.getIdStatus()));
        if (projectRequestDto.getIdUser() != null) projectEntity.setUserEntity(new UserEntity(projectRequestDto.getIdUser()));
    }
    ProjectEntity ProjectRequestDtoToProjectEntity(ProjectRequestDto projectRequestDto);

    @AfterMapping
    default void setId(@MappingTarget ProjectResponseDto projectResponseDto, ProjectEntity projectEntity){
        if (projectEntity.getStatusEntity() != null) projectResponseDto.setIdStatus(projectEntity.getStatusEntity().getIdStatus());
        if (projectEntity.getUserEntity() != null) projectResponseDto.setIdUser(projectEntity.getUserEntity().getIdUser());
    }
    ProjectResponseDto ProjectEntityToProjectResponseDto(ProjectEntity projectEntity);
}
