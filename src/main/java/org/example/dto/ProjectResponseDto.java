package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.entity.StatusEntity;
import org.example.entity.UserEntity;

@Schema(description = "DTO проекта (ответ)")
public class ProjectResponseDto {
    private Long idProject;
    private String title;
    private Boolean isComplete;
    private StatusEntity status;
    private UserEntity user;

    public ProjectResponseDto() {
    }

    public ProjectResponseDto(Long idProject, String title, Boolean isComplete, StatusEntity status, UserEntity user) {
        this.idProject = idProject;
        this.title = title;
        this.isComplete = isComplete;
        this.status = status;
        this.user = user;
    }

    public Long getIdProject() {
        return idProject;
    }

    public void setIdProject(Long idProject) {
        this.idProject = idProject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
