package be.dailycorebackend.api.controller;

import be.dailycorebackend.api.dto.CreateRoutineRequest;
import be.dailycorebackend.api.dto.RoutineResponse;
import be.dailycorebackend.api.dto.UpdateRoutineRequest;
import be.dailycorebackend.bll.service.RoutineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @GetMapping
    public List<RoutineResponse> getAllRoutines() {
        return routineService.getAllRoutines();
    }

    @GetMapping("/{id}")
    public RoutineResponse getRoutineById(@PathVariable Long id) {
        return routineService.getRoutineById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoutineResponse createRoutine(@Valid @RequestBody CreateRoutineRequest request) {
        return routineService.createRoutine(request);
    }

    @PutMapping("/{id}")
    public RoutineResponse updateRoutine(@PathVariable Long id, @Valid @RequestBody UpdateRoutineRequest request) {
        return routineService.updateRoutine(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(id);
    }
}
