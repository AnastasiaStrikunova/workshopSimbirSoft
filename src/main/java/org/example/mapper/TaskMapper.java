package org.example.mapper;

import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
    TaskEntity TaskRequestDtoToTaskEntity (TaskRequestDto taskRequestDto);
    TaskResponseDto TaskEntityToTaskResponseDto (TaskEntity taskEntity);
}
