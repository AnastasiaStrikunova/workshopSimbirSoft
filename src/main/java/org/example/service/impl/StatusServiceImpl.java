package org.example.service.impl;

import org.example.object.Status;
import org.example.entity.StatusEntity;
import org.example.exception.NotFoundException;
import org.example.mapper.StatusMapper;
import org.example.repository.StatusRepository;
import org.example.service.StatusService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public List<Status> findAll() {
        List statusEntityList = new ArrayList(statusRepository.findAll());
        List statusList = new ArrayList();
        for (Object o : statusEntityList) {
            statusList.add(StatusMapper.INSTANCE.StatusEntityToStatus((StatusEntity) o));
        }
        return statusList;
    }

    @Override
    public Status findById(Long id) {
        return StatusMapper.INSTANCE.StatusEntityToStatus(
                statusRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(
                                String.format("Could not find object with id = %d",id)
                        )
                )
        );
    }

    @Override
    public Status add(Status status) {
        StatusEntity statusEntity = StatusMapper.INSTANCE.StatusToStatusEntity(status);
        statusRepository.save(statusEntity);
        return StatusMapper.INSTANCE.StatusEntityToStatus(statusEntity);
    }

    @Override
    public Status change(Long id, Status status) {
        StatusEntity entity = StatusMapper.INSTANCE.StatusToStatusEntity(status);
        StatusEntity statusEntity = statusRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find object with id = %d",id)
                )
        );
        if (entity.getTitle() != null) statusEntity.setTitle(entity.getTitle());
        statusRepository.save(statusEntity);
        return StatusMapper.INSTANCE.StatusEntityToStatus(statusEntity);
    }

    @Override
    public void delete(Long id) {
        StatusEntity statusEntity = statusRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Could not find object with id = %d",id)
                )
        );
        statusRepository.delete(statusEntity);
    }
}
