package org.example.service.impl;

import org.example.dto.ReleaseRequestDto;
import org.example.dto.ReleaseResponseDto;
import org.example.entity.ReleaseEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.ReleaseMapper;
import org.example.repository.ReleaseRepository;
import org.example.service.ReleaseService;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReleaseServiceImpl implements ReleaseService {
    private final ReleaseRepository releaseRepository;
    private final ReleaseMapper releaseMapper = Mappers.getMapper(ReleaseMapper.class);

    public ReleaseServiceImpl(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReleaseResponseDto> findAll() {
        List<ReleaseEntity> releaseEntityList = new ArrayList<>(releaseRepository.findAll());
        List<ReleaseResponseDto> releaseResponseDtoList = new ArrayList<>();
        Stream<ReleaseEntity> stream = releaseEntityList.stream();
        stream.forEach(releaseEntity -> releaseResponseDtoList.add(releaseMapper.ReleaseEntityToReleaseResponseDto(releaseEntity)));
        return releaseResponseDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public ReleaseResponseDto findById(Long id) {
        return releaseMapper.ReleaseEntityToReleaseResponseDto(
                releaseRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find release with id = %d", id)
                        )
                )
        );
    }

    @Override
    @Transactional
    public ReleaseResponseDto add(ReleaseRequestDto releaseRequestDto) {
        ReleaseEntity releaseEntity = releaseMapper.ReleaseRequestDtoToReleaseEntity(releaseRequestDto);
        releaseRepository.save(releaseEntity);
        return releaseMapper.ReleaseEntityToReleaseResponseDto(releaseEntity);
    }

    @Override
    @Transactional
    public ReleaseResponseDto change(Long id, ReleaseRequestDto releaseRequestDto) {
        ReleaseEntity releaseEntity = releaseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find release with id = %d", id)
                )
        );
        if (releaseRequestDto.getVersion() != null) releaseEntity.setVersion(releaseRequestDto.getVersion());
        if (releaseRequestDto.getStartTime() != null) releaseEntity.setStartTime(releaseRequestDto.getStartTime());
        if (releaseRequestDto.getEndTime() != null) releaseEntity.setEndTime(releaseRequestDto.getEndTime());
        return releaseMapper.ReleaseEntityToReleaseResponseDto(releaseEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ReleaseEntity releaseEntity = releaseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find release with id = %d", id)
                )
        );
        releaseRepository.delete(releaseEntity);
    }
}
