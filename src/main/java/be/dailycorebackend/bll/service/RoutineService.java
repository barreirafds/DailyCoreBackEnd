package be.dailycorebackend.bll.service;

import be.dailycorebackend.api.dto.CreateRoutineRequest;
import be.dailycorebackend.dal.entity.Routine;
import be.dailycorebackend.dal.entity.User;
import be.dailycorebackend.dal.repository.RoutineRepository;
import be.dailycorebackend.dal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;

    public RoutineService(RoutineRepository routineRepository, UserRepository userRepository) {
        this.routineRepository = routineRepository;
        this.userRepository = userRepository;
    }

    public Routine createRoutine(CreateRoutineRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Routine routine = new Routine(
                request.getTitle(),
                request.getDescription(),
                user
        );

        return routineRepository.save(routine);
    }

    public List<Routine> getAllRoutines() {
        return routineRepository.findAll();
    }
}