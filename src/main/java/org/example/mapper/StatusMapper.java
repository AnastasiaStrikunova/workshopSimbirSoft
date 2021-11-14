package org.example.mapper;

import org.example.object.Status;
import org.example.entity.StatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StatusMapper {
    StatusMapper INSTANCE = Mappers.getMapper(StatusMapper.class);
    StatusEntity StatusToStatusEntity(Status status);
    Status StatusEntityToStatus(StatusEntity statusEntity);
}
