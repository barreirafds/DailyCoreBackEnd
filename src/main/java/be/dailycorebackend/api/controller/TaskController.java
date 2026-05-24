package be.dailycorebackend.api.controller;

import be.dailycorebackend.api.dto.CompleteTaskRequest;
import be.dailycorebackend.api.dto.CreateTaskRequest;
import be.dailycorebackend.api.dto.TaskResponse;
import be.dailycorebackend.api.dto.UpdateTaskRequest;
import be.dailycorebackend.bll.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/api/routines/{routineId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@PathVariable Long routineId, @Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(routineId, request);
    }

    @PutMapping("/api/tasks/{taskId}")
    public TaskResponse updateTask(@PathVariable Long taskId, @Valid @RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(taskId, request);
    }

    @PatchMapping("/api/tasks/{taskId}/complete")
    public TaskResponse updateTaskCompleted(@PathVariable Long taskId, @RequestBody CompleteTaskRequest request) {
        return taskService.updateTaskCompleted(taskId, request);
    }

    @DeleteMapping("/api/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}
