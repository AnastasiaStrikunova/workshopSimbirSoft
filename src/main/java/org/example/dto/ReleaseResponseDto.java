package org.example.dto;

public class ReleaseResponseDto {
    private Long idRelease;
    private String version;
    private String startTime;
    private String endTime;

    public ReleaseResponseDto() {
    }

    public ReleaseResponseDto(Long idRelease, String version, String startTime, String endTime) {
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
