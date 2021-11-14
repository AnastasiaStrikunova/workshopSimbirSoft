package org.example.mapper;

import org.example.dto.ReleaseRequestDto;
import org.example.dto.ReleaseResponseDto;
import org.example.entity.ReleaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReleaseMapper {
    ReleaseMapper INSTANCE = Mappers.getMapper(ReleaseMapper.class);
    ReleaseEntity ReleaseRequestDtoToReleaseEntity(ReleaseRequestDto releaseRequestDto);
    ReleaseResponseDto ReleaseEntityToReleaseResponseDto(ReleaseEntity releaseEntity);
}
