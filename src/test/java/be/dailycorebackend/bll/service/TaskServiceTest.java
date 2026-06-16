package be.dailycorebackend.bll.service;

import be.dailycorebackend.api.dto.CompleteTaskRequest;
import be.dailycorebackend.api.dto.CreateTaskRequest;
import be.dailycorebackend.api.dto.TaskResponse;
import be.dailycorebackend.dal.entity.Routine;
import be.dailycorebackend.dal.entity.Task;
import be.dailycorebackend.dal.entity.User;
import be.dailycorebackend.dal.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private RoutineService routineService;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private TaskService taskService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Diego", "diego@example.com", "encoded", "+351900000000");
        ReflectionTestUtils.setField(user, "id", 1L);
        lenient().when(currentUserService.getCurrentUserId()).thenReturn(1L);
    }

    @Test
    void createTask_addsTaskToRoutineAndReturnsResponse() {
        Routine routine = new Routine("Morning", "Start the day", user);
        ReflectionTestUtils.setField(routine, "id", 1L);

        when(routineService.findRoutineWithTasks(1L)).thenReturn(routine);

        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Brush teeth");

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            ReflectionTestUtils.setField(task, "id", 10L);
            return task;
        });

        TaskResponse response = taskService.createTask(1L, request);

        assertEquals(10L, response.getId());
        assertEquals("Brush teeth", response.getTitle());
        assertFalse(response.isCompleted());
        assertEquals(1, routine.getTasks().size());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void updateTaskCompleted_marksTaskAsCompleted() {
        Task task = new Task("Exercise", new Routine("Morning", null, user));
        ReflectionTestUtils.setField(task, "id", 5L);

        when(taskRepository.findByIdForUser(5L, 1L)).thenReturn(Optional.of(task));

        CompleteTaskRequest request = new CompleteTaskRequest();
        request.setCompleted(true);

        TaskResponse response = taskService.updateTaskCompleted(5L, request);

        assertTrue(response.isCompleted());
        assertTrue(task.isCompleted());
    }

    @Test
    void deleteTask_removesTaskFromRoutineAndDeletesFromRepository() {
        Routine routine = new Routine("Morning", null, user);
        Task task = new Task("Exercise", routine);
        routine.addTask(task);
        ReflectionTestUtils.setField(task, "id", 5L);

        when(taskRepository.findByIdForUser(5L, 1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(5L);

        assertTrue(routine.getTasks().isEmpty());
        verify(taskRepository).delete(task);
    }
}
