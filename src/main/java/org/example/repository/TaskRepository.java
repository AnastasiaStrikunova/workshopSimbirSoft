package org.example.repository;

import org.example.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findAllByTitle(String title);
    List<TaskEntity> findAllByIdProject(Long id);
}
