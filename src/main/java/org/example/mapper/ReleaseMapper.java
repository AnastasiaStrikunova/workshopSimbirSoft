package org.example.mapper;

import org.example.dto.ReleaseRequestDto;
import org.example.dto.ReleaseResponseDto;
import org.example.entity.ReleaseEntity;
import org.mapstruct.*;

@Mapper
public interface ReleaseMapper {
    ReleaseEntity ReleaseRequestDtoToReleaseEntity(ReleaseRequestDto releaseRequestDto);
    ReleaseResponseDto ReleaseEntityToReleaseResponseDto(ReleaseEntity releaseEntity);
}
