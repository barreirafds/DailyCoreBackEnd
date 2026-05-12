package be.dailycorebackend.api.controller;

import be.dailycorebackend.api.dto.CreateTaskRequest;
import be.dailycorebackend.bll.service.TaskService;
import be.dailycorebackend.dal.entity.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public Task createTask(@RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
}