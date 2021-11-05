package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "DTO релиза (ответ)")
public class ReleaseResponseDto {
    private Long idRelease;
    private String version;
    private Date startTime;
    private Date endTime;

    public ReleaseResponseDto() {
    }

    public ReleaseResponseDto(Long idRelease, String version, Date startTime, Date endTime) {
        this.idRelease = idRelease;
        this.version = version;
        this.startTime = startTime;
        this.endTime = endTime;
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
