package be.dailycorebackend.bll.service;

import be.dailycorebackend.api.dto.CreateUserRequest;
import be.dailycorebackend.dal.entity.User;
import be.dailycorebackend.dal.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(CreateUserRequest request) {

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhoneNumber()
        );

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}