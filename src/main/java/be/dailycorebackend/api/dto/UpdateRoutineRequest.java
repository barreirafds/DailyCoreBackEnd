package be.dailycorebackend.api.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateRoutineRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
