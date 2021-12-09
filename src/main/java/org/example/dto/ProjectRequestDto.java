package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO проекта (запрос)")
public class ProjectRequestDto {
    private String title;
    private Boolean isComplete;
    private Long idStatus;
    private Long idUser;

    public ProjectRequestDto() {
    }

    public ProjectRequestDto(String title, Boolean isComplete, Long idStatus, Long idUser) {
        this.title = title;
        this.isComplete = isComplete;
        this.idStatus = idStatus;
        this.idUser = idUser;
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

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
}
