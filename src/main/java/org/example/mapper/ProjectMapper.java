package org.example.mapper;

import org.example.dto.ProjectRequestDto;
import org.example.dto.ProjectResponseDto;
import org.example.entity.ProjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);
    ProjectEntity ProjectRequestDtoToProjectEntity(ProjectRequestDto projectRequestDto);
    ProjectResponseDto ProjectEntityToProjectResponseDto(ProjectEntity projectEntity);
}
