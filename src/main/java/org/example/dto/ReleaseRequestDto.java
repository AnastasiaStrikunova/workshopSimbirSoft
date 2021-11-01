package org.example.dto;

public class ReleaseRequestDto {
    private String version;
    private String startTime;
    private String endTime;

    public ReleaseRequestDto() {
    }

    public ReleaseRequestDto(String version, String startTime, String endTime) {
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
}
