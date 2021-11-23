package org.example.specification;

import org.example.entity.TaskEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class TaskSpecification {
    public static Specification<TaskEntity> filterByTitle(String title){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title));
    }
    public static Specification<TaskEntity> filterByPriority(String priority){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority);
    }
    public static Specification<TaskEntity> filterByAuthor(Long author){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("author"), author));
    }
    public static Specification<TaskEntity> filterByPerformer(Long performer){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("performer"), performer);
    }
    public static Specification<TaskEntity> filterByStartTime(Date startTime){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("startTime"), startTime));
    }
    public static Specification<TaskEntity> filterByEndTime(Date endTime){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("endTime"), endTime);
    }
    public static Specification<TaskEntity> filterByIdProject(Long idProject){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("idProject"), idProject));
    }
    public static Specification<TaskEntity> filterByIdStatus(Long idStatus){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("idStatus"), idStatus);
    }
    public static Specification<TaskEntity> filterByIdRelease(Long idRelease){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("idRelease"), idRelease);
    }
}
