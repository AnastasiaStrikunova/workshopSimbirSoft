package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "DTO релиза (запрос)")
public class ReleaseRequestDto {
    private String version;
    private Date startTime;
    private Date endTime;

    public ReleaseRequestDto() {
    }

    public ReleaseRequestDto(String version, Date startTime, Date endTime) {
        this.version = version;
        this.startTime = startTime;
        this.endTime = endTime;
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
