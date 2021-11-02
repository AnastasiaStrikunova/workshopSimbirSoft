package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO статуса (запрос)")
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
