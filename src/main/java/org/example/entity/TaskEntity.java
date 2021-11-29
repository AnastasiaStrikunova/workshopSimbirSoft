package org.example.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_id_gen")
    @SequenceGenerator(name = "task_id_gen", sequenceName = "seq_task", allocationSize = 1)
    private Long idTask;
    @Column(name = "title")
    private String title;
    @Column(name = "priority")
    private String priority;
    @OneToOne
    @JoinColumn(name = "author")
    private UserEntity authorEntity;
    @OneToOne
    @JoinColumn(name = "performer")
    private UserEntity performerEntity;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @OneToOne
    @JoinColumn(name = "id_project")
    private ProjectEntity projectEntity;
    @OneToOne
    @JoinColumn(name = "id_status")
    private StatusEntity statusEntity;
    @OneToOne
    @JoinColumn(name = "id_release")
    private ReleaseEntity releaseEntity;

    public TaskEntity() {
    }

    public TaskEntity(Long idTask, String title, String priority, UserEntity authorEntity, UserEntity performerEntity, Date startTime, Date endTime, ProjectEntity projectEntity, StatusEntity statusEntity, ReleaseEntity releaseEntity) {
        this.idTask = idTask;
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
