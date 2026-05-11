package be.dailycorebackend.api.controller;

import be.dailycorebackend.api.dto.CreateRoutineRequest;
import be.dailycorebackend.bll.service.RoutineService;
import be.dailycorebackend.dal.entity.Routine;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping
    public Routine createRoutine(@RequestBody CreateRoutineRequest request) {
        return routineService.createRoutine(request);
    }

    @GetMapping
    public List<Routine> getAllRoutines() {
        return routineService.getAllRoutines();
    }
}