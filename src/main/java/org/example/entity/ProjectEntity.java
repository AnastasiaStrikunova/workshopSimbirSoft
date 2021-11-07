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
    @Column(name = "id_status")
    private Long idStatus;
    @Column(name = "id_user")
    private Long idUser;

    public ProjectEntity() {
    }

    public ProjectEntity(Long idProject, String title, Boolean isComplete, Long idStatus, Long idUser) {
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
