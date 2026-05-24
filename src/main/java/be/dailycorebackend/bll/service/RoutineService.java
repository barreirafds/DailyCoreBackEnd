package be.dailycorebackend.bll.service;

import be.dailycorebackend.api.dto.CreateRoutineRequest;
import be.dailycorebackend.api.dto.RoutineResponse;
import be.dailycorebackend.api.dto.UpdateRoutineRequest;
import be.dailycorebackend.api.exception.ResourceNotFoundException;
import be.dailycorebackend.dal.entity.Routine;
import be.dailycorebackend.dal.repository.RoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoutineService {

    private final RoutineRepository routineRepository;

    public RoutineService(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    @Transactional(readOnly = true)
    public List<RoutineResponse> getAllRoutines() {
        return routineRepository.findAllByOrderByCreatedAtDesc().stream()
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
        Routine routine = new Routine(request.getTitle(), request.getDescription());
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
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Routine not found with id: " + id));
        routineRepository.delete(routine);
    }

    public Routine findRoutineWithTasks(Long id) {
        return routineRepository.findByIdWithTasks(id)
                .orElseThrow(() -> new ResourceNotFoundException("Routine not found with id: " + id));
    }
}
