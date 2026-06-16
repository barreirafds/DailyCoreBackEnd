package be.dailycorebackend.dal.repository;

import be.dailycorebackend.dal.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t JOIN FETCH t.routine r WHERE t.id = :taskId AND r.user.id = :userId")
    Optional<Task> findByIdForUser(@Param("taskId") Long taskId, @Param("userId") Long userId);
}
