package be.dailycorebackend.bll.service;

import be.dailycorebackend.api.dto.CompleteTaskRequest;
import be.dailycorebackend.api.dto.CreateTaskRequest;
import be.dailycorebackend.api.dto.TaskResponse;
import be.dailycorebackend.api.dto.UpdateTaskRequest;
import be.dailycorebackend.api.exception.ResourceNotFoundException;
import be.dailycorebackend.dal.entity.Routine;
import be.dailycorebackend.dal.entity.Task;
import be.dailycorebackend.dal.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final RoutineService routineService;
    private final CurrentUserService currentUserService;

    public TaskService(
            TaskRepository taskRepository,
            RoutineService routineService,
            CurrentUserService currentUserService) {
        this.taskRepository = taskRepository;
        this.routineService = routineService;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public TaskResponse createTask(Long routineId, CreateTaskRequest request) {
        Routine routine = routineService.findRoutineWithTasks(routineId);

        Task task = new Task(request.getTitle(), routine);
        routine.addTask(task);

        Task saved = taskRepository.save(task);
        return TaskResponse.fromEntity(saved);
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
        Task task = findTaskById(taskId);
        task.setTitle(request.getTitle());
        task.setCompleted(request.isCompleted());
        return TaskResponse.fromEntity(task);
    }

    @Transactional
    public TaskResponse updateTaskCompleted(Long taskId, CompleteTaskRequest request) {
        Task task = findTaskById(taskId);
        task.setCompleted(request.isCompleted());
        return TaskResponse.fromEntity(task);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Task task = findTaskById(taskId);
        Routine routine = task.getRoutine();
        routine.removeTask(task);
        taskRepository.delete(task);
    }

    private Task findTaskById(Long taskId) {
        Long userId = currentUserService.getCurrentUserId();
        return taskRepository.findByIdForUser(taskId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
    }
}
