package org.example.dto;

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
}
