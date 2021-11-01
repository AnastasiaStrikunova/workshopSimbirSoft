package org.example.dto;

public class TaskResponseDto {
    private Long idTask;
    private String title;
    private String priority;
    private String author;
    private String performer;
    private String startTime;
    private String endTime;
    private Long idProject;
    private Long idStatus;
    private Long idRelease;

    public TaskResponseDto() {}

    public TaskResponseDto(Long idTask, String title, String priority, String author, String performer, String startTime, String endTime, Long idProject, Long idStatus, Long idRelease) {
        this.idTask = idTask;
        this.title = title;
        this.priority = priority;
        this.author = author;
        this.performer = performer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.idProject = idProject;
        this.idStatus = idStatus;
        this.idRelease = idRelease;
    }

    public Long getIdTask() {
        return idTask;
    }

    public void setIdTask(Long idTask) {
        this.idTask = idTask;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getIdProject() {
        return idProject;
    }

    public void setIdProject(Long idProject) {
        this.idProject = idProject;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public Long getIdRelease() {
        return idRelease;
    }

    public void setIdRelease(Long idRelease) {
        this.idRelease = idRelease;
    }
}
