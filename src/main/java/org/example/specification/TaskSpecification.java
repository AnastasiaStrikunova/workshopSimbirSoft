package org.example.specification;

import org.example.entity.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class TaskSpecification {
    public static Specification<TaskEntity> filterByTitle(String title){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title));
    }
    public static Specification<TaskEntity> filterByPriority(String priority){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("priority"), priority);
    }
    public static Specification<TaskEntity> filterByAuthor(Long authorId){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("authorEntity").get("idUser"), authorId));
    }
    public static Specification<TaskEntity> filterByPerformer(Long performerId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("performerEntity").get("idUser"), performerId);
    }
    public static Specification<TaskEntity> filterByStartTime(Date startTime){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("startTime"), startTime));
    }
    public static Specification<TaskEntity> filterByEndTime(Date endTime){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("endTime"), endTime);
    }
    public static Specification<TaskEntity> filterByIdProject(Long projectId){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("projectEntity").get("idProject"), projectId));
    }
    public static Specification<TaskEntity> filterByIdStatus(Long statusId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("statusEntity").get("idStatus"), statusId);
    }
    public static Specification<TaskEntity> filterByIdRelease(Long releaseId){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("releaseEntity").get("idRelease"), releaseId);
    }
}
