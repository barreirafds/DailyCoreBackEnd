package be.dailycorebackend.bll.service;

import be.dailycorebackend.api.dto.CreateRoutineRequest;
import be.dailycorebackend.api.dto.RoutineResponse;
import be.dailycorebackend.api.exception.ResourceNotFoundException;
import be.dailycorebackend.dal.entity.Routine;
import be.dailycorebackend.dal.entity.User;
import be.dailycorebackend.dal.repository.RoutineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoutineServiceTest {

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private RoutineService routineService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Diego", "diego@example.com", "encoded", "+351900000000");
        ReflectionTestUtils.setField(user, "id", 1L);
        lenient().when(currentUserService.getCurrentUserId()).thenReturn(1L);
        lenient().when(currentUserService.getCurrentUser()).thenReturn(user);
    }

    @Test
    void createRoutine_savesAndReturnsResponse() {
        CreateRoutineRequest request = new CreateRoutineRequest();
        request.setTitle("Morning Routine");
        request.setDescription("Start the day");

        Routine saved = new Routine("Morning Routine", "Start the day", user);
        ReflectionTestUtils.setField(saved, "id", 1L);
        ReflectionTestUtils.setField(saved, "createdAt", LocalDateTime.of(2026, 6, 5, 8, 0));

        when(routineRepository.save(any(Routine.class))).thenReturn(saved);

        RoutineResponse response = routineService.createRoutine(request);

        assertEquals(1L, response.getId());
        assertEquals("Morning Routine", response.getTitle());
        assertEquals("Start the day", response.getDescription());
        assertEquals(LocalDateTime.of(2026, 6, 5, 8, 0), response.getCreatedAt());
        verify(routineRepository).save(any(Routine.class));
    }

    @Test
    void deleteRoutine_throwsWhenNotFound() {
        when(routineRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> routineService.deleteRoutine(99L));
    }
}
