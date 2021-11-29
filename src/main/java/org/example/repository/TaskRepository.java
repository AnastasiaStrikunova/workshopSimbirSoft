package org.example.repository;

import org.example.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {
    Integer countAllByEndTimeAfter(Date date);
    @Query(value = "SELECT * FROM task WHERE task.id_project = ?1", nativeQuery = true)
    List<TaskEntity> findAllByProjectEntity(Long id);
}
