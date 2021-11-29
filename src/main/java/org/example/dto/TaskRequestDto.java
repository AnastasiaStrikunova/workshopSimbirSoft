package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.entity.ProjectEntity;
import org.example.entity.ReleaseEntity;
import org.example.entity.StatusEntity;
import org.example.entity.UserEntity;

import javax.validation.constraints.NotNull;
import java.util.Date;

@NotNull
@Schema(description = "DTO задачи (запрос)")
public class TaskRequestDto {
    private String title;
    private String priority;
    private UserEntity authorEntity;
    @Schema(description = "Исполнитель", example = "Anastasia")
    private UserEntity performerEntity;
    private Date startTime;
    private Date endTime;
    private ProjectEntity projectEntity;
    private StatusEntity statusEntity;
    private ReleaseEntity releaseEntity;

    public TaskRequestDto() {}

    public TaskRequestDto(String title, String priority, UserEntity authorEntity, UserEntity performerEntity, Date startTime, Date endTime, ProjectEntity projectEntity, StatusEntity statusEntity, ReleaseEntity releaseEntity) {
        this.title = title;
        this.priority = priority;
        this.authorEntity = authorEntity;
        this.performerEntity = performerEntity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.projectEntity = projectEntity;
        this.statusEntity = statusEntity;
        this.releaseEntity = releaseEntity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public UserEntity getAuthorEntity() {
        return authorEntity;
    }

    public void setAuthorEntity(UserEntity authorEntity) {
        this.authorEntity = authorEntity;
    }

    public UserEntity getPerformerEntity() {
        return performerEntity;
    }

    public void setPerformerEntity(UserEntity performerEntity) {
        this.performerEntity = performerEntity;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public ProjectEntity getProjectEntity() {
        return projectEntity;
    }

    public void setProjectEntity(ProjectEntity projectEntity) {
        this.projectEntity = projectEntity;
    }

    public StatusEntity getStatusEntity() {
        return statusEntity;
    }

    public void setStatusEntity(StatusEntity statusEntity) {
        this.statusEntity = statusEntity;
    }

    public ReleaseEntity getReleaseEntity() {
        return releaseEntity;
    }

    public void setReleaseEntity(ReleaseEntity releaseEntity) {
        this.releaseEntity = releaseEntity;
    }
}
