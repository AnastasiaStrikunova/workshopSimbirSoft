package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "DTO проекта (ответ)")
public class ProjectResponseDto {
    private Long idProject;
    private String title;
    private Boolean isComplete;
    private Long idStatus;
    private Long idUser;

    public ProjectResponseDto() {
    }

    public ProjectResponseDto(Long idProject, String title, Boolean isComplete, Long idStatus, Long idUser) {
        this.idProject = idProject;
        this.title = title;
        this.isComplete = isComplete;
        this.idStatus = idStatus;
        this.idUser = idUser;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectResponseDto that = (ProjectResponseDto) o;
        return Objects.equals(getIdProject(), that.getIdProject()) && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(isComplete, that.isComplete) && Objects.equals(getIdStatus(), that.getIdStatus()) && Objects.equals(getIdUser(), that.getIdUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdProject(), getTitle(), isComplete, getIdStatus(), getIdUser());
    }
}
