package be.dailycorebackend.dal.repository;

import be.dailycorebackend.dal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}