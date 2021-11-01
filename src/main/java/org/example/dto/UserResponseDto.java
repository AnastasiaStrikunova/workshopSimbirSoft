package org.example.dto;

public class UserResponseDto {
    private Long idUser;
    private String name;
    private String priority;

    public UserResponseDto() {
    }

    public UserResponseDto(Long idUser, String name, String priority) {
        this.idUser = idUser;
        this.name = name;
        this.priority = priority;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
