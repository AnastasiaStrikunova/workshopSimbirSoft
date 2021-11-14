package org.example.service.impl;

import org.example.entity.ReleaseEntity;
import org.example.exception.NotFoundException;
import org.example.repository.ReleaseRepository;
import org.example.service.ReleaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReleaseServiceImpl implements ReleaseService {
    private final ReleaseRepository releaseRepository;

    public ReleaseServiceImpl(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    @Transactional(readOnly = true)
    public List<ReleaseEntity> findAll() {
        return releaseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ReleaseEntity findById(Long id) {
        return releaseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find object with id = %d",id)
                )
        );
    }

    @Transactional
    public ReleaseEntity add(ReleaseEntity releaseEntity) {
        return releaseRepository.save(releaseEntity);
    }

    @Transactional
    public ReleaseEntity change(Long id, ReleaseEntity releaseEntity) {
        ReleaseEntity entity = releaseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find object with id = %d",id)
                )
        );
        if (releaseEntity.getVersion() != null) entity.setVersion(releaseEntity.getVersion());
        if (releaseEntity.getStartTime() != null) entity.setStartTime(releaseEntity.getStartTime());
        if (releaseEntity.getEndTime() != null) entity.setEndTime(releaseEntity.getEndTime());
        return releaseRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        releaseRepository.delete(releaseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Could not find object with id = %d",id))
        ));
    }
}
