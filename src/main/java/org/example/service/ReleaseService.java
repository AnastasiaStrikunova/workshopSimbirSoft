package org.example.service;


import org.example.entity.ReleaseEntity;

import java.util.List;

public interface ReleaseService {
    List<ReleaseEntity> findAll();
    ReleaseEntity findById(Long id);
    ReleaseEntity add(ReleaseEntity releaseEntity);
    ReleaseEntity change(Long id, ReleaseEntity releaseEntity);
    void delete(Long id);
}
