package be.dailycorebackend.dal.repository;

import be.dailycorebackend.dal.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
}