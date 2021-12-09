package org.example.mapper;

import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.*;
import org.mapstruct.*;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TaskMapper {
    @AfterMapping
    default void setEntity(@MappingTarget TaskEntity taskEntity, TaskRequestDto taskRequestDto){
        if (taskRequestDto.getAuthor() != null) taskEntity.setAuthorEntity(new UserEntity(taskRequestDto.getAuthor()));
        if (taskRequestDto.getPerformer() != null) taskEntity.setPerformerEntity(new UserEntity(taskRequestDto.getPerformer()));
        if (taskRequestDto.getIdProject() != null) taskEntity.setProjectEntity(new ProjectEntity(taskRequestDto.getIdProject()));
        if (taskRequestDto.getIdStatus() != null) taskEntity.setStatusEntity(new StatusEntity(taskRequestDto.getIdStatus()));
        if (taskRequestDto.getIdRelease() != null) taskEntity.setReleaseEntity(new ReleaseEntity(taskRequestDto.getIdRelease()));
    }
    TaskEntity TaskRequestDtoToTaskEntity (TaskRequestDto taskRequestDto);

    @AfterMapping
    default void setId(@MappingTarget TaskResponseDto taskResponseDto, TaskEntity taskEntity){
        if ( taskEntity.getAuthorEntity() != null ) taskResponseDto.setAuthor(taskEntity.getAuthorEntity().getIdUser());
        if ( taskEntity.getPerformerEntity() != null ) taskResponseDto.setPerformer(taskEntity.getPerformerEntity().getIdUser());
        if ( taskEntity.getProjectEntity() != null ) taskResponseDto.setIdProject(taskEntity.getProjectEntity().getIdProject());
        if ( taskEntity.getStatusEntity() != null ) taskResponseDto.setIdStatus(taskEntity.getStatusEntity().getIdStatus());
        if ( taskEntity.getReleaseEntity() != null ) taskResponseDto.setIdRelease(taskEntity.getReleaseEntity().getIdRelease());
    }
    TaskResponseDto TaskEntityToTaskResponseDto (TaskEntity taskEntity);
}
