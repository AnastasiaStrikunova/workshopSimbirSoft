package org.example.dto;

public class StatusRequestDto {
    private String title;

    public StatusRequestDto() {
    }

    public StatusRequestDto(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
