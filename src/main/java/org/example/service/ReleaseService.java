package org.example.service;


import org.example.dto.ReleaseRequestDto;
import org.example.dto.ReleaseResponseDto;

import java.util.List;

/**
 * Этот интерфейс указывает какие методы необходимы для реализации бизнес-логики <i>релизов</i>
 * @author Anastasia Strikunova
 */
public interface ReleaseService {
    List<ReleaseResponseDto> findAll();
    ReleaseResponseDto findById(Long id);
    ReleaseResponseDto add(ReleaseRequestDto releaseRequestDto);
    ReleaseResponseDto change(Long id, ReleaseRequestDto releaseRequestDto);
    void delete(Long id);
}
