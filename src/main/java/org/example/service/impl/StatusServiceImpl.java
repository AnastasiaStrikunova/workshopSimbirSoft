package org.example.service.impl;

import org.example.object.Status;
import org.example.entity.StatusEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.StatusMapper;
import org.example.repository.StatusRepository;
import org.example.service.StatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Transactional(readOnly = true)
    public List<Status> findAll() {
        List<StatusEntity> statusEntityList = new ArrayList<>(statusRepository.findAll());
        List<Status> statusList = new ArrayList<>();
        for (StatusEntity statusEntity : statusEntityList) {
            statusList.add(StatusMapper.INSTANCE.StatusEntityToStatus(statusEntity));
        }
        return statusList;
    }

    @Transactional(readOnly = true)
    public Status findById(Long id) {
        return StatusMapper.INSTANCE.StatusEntityToStatus(
                statusRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find status with id = %d",id)
                        )
                )
        );
    }

    @Transactional
    public Status add(Status status) {
        StatusEntity statusEntity = StatusMapper.INSTANCE.StatusToStatusEntity(status);
        statusRepository.save(statusEntity);
        return StatusMapper.INSTANCE.StatusEntityToStatus(statusEntity);
    }

    @Transactional
    public Status change(Long id, Status status) {
        StatusEntity entity = StatusMapper.INSTANCE.StatusToStatusEntity(status);
        StatusEntity statusEntity = statusRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find status with id = %d",id)
                )
        );
        if (entity.getTitle() != null) statusEntity.setTitle(entity.getTitle());
        statusRepository.save(statusEntity);
        return StatusMapper.INSTANCE.StatusEntityToStatus(statusEntity);
    }

    @Transactional
    public void delete(Long id) {
        StatusEntity statusEntity = statusRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find status with id = %d",id)
                )
        );
        statusRepository.delete(statusEntity);
    }
}
