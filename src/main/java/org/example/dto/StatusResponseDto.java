package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO задачи (ответ)")
public class StatusResponseDto {
    private Long idStatus;
    private String title;

    public StatusResponseDto() {
    }

    public StatusResponseDto(Long idStatus, String title) {
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
