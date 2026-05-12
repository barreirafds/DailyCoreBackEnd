package be.dailycorebackend.bll.service;

import be.dailycorebackend.api.dto.CreateTaskRequest;
import be.dailycorebackend.dal.entity.Routine;
import be.dailycorebackend.dal.entity.Task;
import be.dailycorebackend.dal.repository.RoutineRepository;
import be.dailycorebackend.dal.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final RoutineRepository routineRepository;

    public TaskService(TaskRepository taskRepository, RoutineRepository routineRepository) {
        this.taskRepository = taskRepository;
        this.routineRepository = routineRepository;
    }

    public Task createTask(CreateTaskRequest request) {
        Routine routine = routineRepository.findById(request.getRoutineId())
                .orElseThrow(() -> new RuntimeException("Routine not found"));

        Task task = new Task(
                request.getTitle(),
                request.isCompleted(),
                routine
        );

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}