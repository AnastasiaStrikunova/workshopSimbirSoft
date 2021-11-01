package org.example.dto;

public class UserRequestDto {
    private String name;
    private String priority;

    public UserRequestDto() {
    }

    public UserRequestDto(String name, String priority) {
        this.name = name;
        this.priority = priority;
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
