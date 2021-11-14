package org.example.object;

public class Status {
    private Long idStatus;
    private String title;

    public Status() {
    }

    public Status(Long idStatus, String title) {
        this.idStatus = idStatus;
        this.title = title;
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
