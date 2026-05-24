package be.dailycorebackend.api.dto;

import be.dailycorebackend.dal.entity.Routine;
import be.dailycorebackend.dal.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

public class RoutineResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private List<TaskResponse> tasks;

    public RoutineResponse() {
    }

    public RoutineResponse(Long id, String title, String description, LocalDateTime createdAt, List<TaskResponse> tasks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.tasks = tasks;
    }

    public static RoutineResponse fromEntity(Routine routine) {
        List<TaskResponse> taskResponses = routine.getTasks().stream()
                .map(TaskResponse::fromEntity)
                .toList();

        return new RoutineResponse(
                routine.getId(),
                routine.getTitle(),
                routine.getDescription(),
                routine.getCreatedAt(),
                taskResponses
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<TaskResponse> getTasks() {
        return tasks;
    }
}
