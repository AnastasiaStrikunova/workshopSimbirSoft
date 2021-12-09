package org.example.entity;

import javax.persistence.*;

@Entity
@Table(name = "status")
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_id_gen")
    @SequenceGenerator(name = "status_id_gen", sequenceName = "seq_status", allocationSize = 1)
    private Long idStatus;
    @Column(name = "title")
    private String title;

    public StatusEntity() {
    }

    public StatusEntity(Long idStatus, String title) {
        this.idStatus = idStatus;
        this.title = title;
    }

    public StatusEntity(Long idStatus) {
        this.idStatus = idStatus;
    }

    public Long getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
