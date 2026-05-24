package be.dailycorebackend.dal.repository;

import be.dailycorebackend.dal.entity.Routine;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    @EntityGraph(attributePaths = "tasks")
    List<Routine> findAllByOrderByCreatedAtDesc();

    @Query("SELECT r FROM Routine r LEFT JOIN FETCH r.tasks WHERE r.id = :id")
    Optional<Routine> findByIdWithTasks(@Param("id") Long id);
}
