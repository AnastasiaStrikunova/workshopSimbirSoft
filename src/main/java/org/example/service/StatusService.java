package org.example.service;

import org.example.object.Status;

import java.util.List;

/**
 * Этот интерфейс указывает какие методы необходимы для реализации бизнес-логики <i>статусов</i>
 * @author Anastasia Strikunova
 */
public interface StatusService {
    List<Status> findAll();
    Status findById(Long id);
    Status add(Status status);
    Status change(Long id, Status status);
    void delete(Long id);
}
