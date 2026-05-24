package be.dailycorebackend.api.dto;

import be.dailycorebackend.dal.entity.Task;

public class TaskResponse {

    private Long id;
    private String title;
    private boolean completed;

    public TaskResponse() {
    }

    public TaskResponse(Long id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.isCompleted());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }
}
