package be.dailycorebackend.api.dto;

public class CreateTaskRequest {

    private String title;
    private boolean completed;
    private Long routineId;

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Long getRoutineId() {
        return routineId;
    }
}