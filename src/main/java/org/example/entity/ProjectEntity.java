package org.example.entity;


import javax.persistence.*;

@Entity
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_id_gen")
    @SequenceGenerator(name = "project_id_gen", sequenceName = "seq_project", allocationSize = 1)
    private Long idProject;
    @Column(name = "title")
    private String title;
    @Column(name = "is_complete")
    private Boolean isComplete;
    @OneToOne
    @JoinColumn(name = "id_status")
    private StatusEntity statusEntity;
    @OneToOne
    @JoinColumn(name = "id_user")
    private UserEntity userEntity;

    public ProjectEntity() {
    }

    public ProjectEntity(Long idProject, String title, Boolean isComplete, StatusEntity statusEntity, UserEntity userEntity) {
        this.idProject = idProject;
        this.title = title;
        this.isComplete = isComplete;
        this.statusEntity = statusEntity;
        this.userEntity = userEntity;
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
