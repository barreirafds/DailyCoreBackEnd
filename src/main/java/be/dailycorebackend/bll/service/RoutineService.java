package be.dailycorebackend.bll.service;

import be.dailycorebackend.api.dto.CreateRoutineRequest;
import be.dailycorebackend.api.dto.RoutineResponse;
import be.dailycorebackend.api.dto.UpdateRoutineRequest;
import be.dailycorebackend.api.exception.ResourceNotFoundException;
import be.dailycorebackend.dal.entity.Routine;
import be.dailycorebackend.dal.entity.User;
import be.dailycorebackend.dal.repository.RoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final CurrentUserService currentUserService;

    public RoutineService(RoutineRepository routineRepository, CurrentUserService currentUserService) {
        this.routineRepository = routineRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<RoutineResponse> getAllRoutines() {
        Long userId = currentUserService.getCurrentUserId();
        return routineRepository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(RoutineResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoutineResponse getRoutineById(Long id) {
        Routine routine = findRoutineWithTasks(id);
        return RoutineResponse.fromEntity(routine);
    }

    @Transactional
    public RoutineResponse createRoutine(CreateRoutineRequest request) {
        User user = currentUserService.getCurrentUser();
        Routine routine = new Routine(request.getTitle(), request.getDescription(), user);
        Routine saved = routineRepository.save(routine);
        return RoutineResponse.fromEntity(saved);
    }

    @Transactional
    public RoutineResponse updateRoutine(Long id, UpdateRoutineRequest request) {
        Routine routine = findRoutineWithTasks(id);
        routine.setTitle(request.getTitle());
        routine.setDescription(request.getDescription());
        return RoutineResponse.fromEntity(routine);
    }

    @Transactional
    public void deleteRoutine(Long id) {
        Long userId = currentUserService.getCurrentUserId();
        Routine routine = routineRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Routine not found with id: " + id));
        routineRepository.delete(routine);
    }

    public Routine findRoutineWithTasks(Long id) {
        Long userId = currentUserService.getCurrentUserId();
        return routineRepository.findByIdWithTasksForUser(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Routine not found with id: " + id));
    }
}
