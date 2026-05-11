package be.dailycorebackend.dal.repository;

import be.dailycorebackend.dal.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}