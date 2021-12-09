package org.example.entity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "release")
public class ReleaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "release_id_gen")
    @SequenceGenerator(name = "release_id_gen", sequenceName = "seq_release", allocationSize = 1)
    private Long idRelease;
    private String version;
    private Date startTime;
    private Date endTime;

    public ReleaseEntity() {
    }

    public ReleaseEntity(Long idRelease, String version, Date startTime, Date endTime) {
        this.idRelease = idRelease;
        this.version = version;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public ReleaseEntity(Long idRelease) {
        this.idRelease = idRelease;
    }

    public Long getIdRelease() {
        return idRelease;
    }

    public void setIdRelease(Long idRelease) {
        this.idRelease = idRelease;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}
