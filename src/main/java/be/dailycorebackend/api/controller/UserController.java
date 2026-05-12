package be.dailycorebackend.api.controller;

import be.dailycorebackend.api.dto.CreateUserRequest;
import be.dailycorebackend.bll.service.UserService;
import be.dailycorebackend.dal.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}