package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.entity.StatusEntity;
import org.example.entity.UserEntity;

@Schema(description = "DTO проекта (запрос)")
public class ProjectRequestDto {
    private String title;
    private Boolean isComplete;
    private StatusEntity statusEntity;
    private UserEntity userEntity;

    public ProjectRequestDto() {
    }

    public ProjectRequestDto(String title, Boolean isComplete, StatusEntity statusEntity, UserEntity userEntity) {
        this.title = title;
        this.isComplete = isComplete;
        this.statusEntity = statusEntity;
        this.userEntity = userEntity;
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

    public StatusEntity getStatusEntity() {
        return statusEntity;
    }

    public void setStatusEntity(StatusEntity statusEntity) {
        this.statusEntity = statusEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
