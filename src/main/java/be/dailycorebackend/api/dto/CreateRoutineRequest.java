package be.dailycorebackend.api.dto;

public class CreateRoutineRequest {
    private String title;
    private String description;
    private Long userId;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getUserId() {
        return userId;
    }
}