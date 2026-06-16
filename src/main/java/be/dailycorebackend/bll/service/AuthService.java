package be.dailycorebackend.bll.service;

import be.dailycorebackend.api.dto.AuthResponse;
import be.dailycorebackend.api.dto.CreateUserRequest;
import be.dailycorebackend.api.dto.LoginRequest;
import be.dailycorebackend.api.dto.UpdateUserRequest;
import be.dailycorebackend.api.dto.UserResponse;
import be.dailycorebackend.api.exception.EmailAlreadyExistsException;
import be.dailycorebackend.api.exception.InvalidCredentialsException;
import be.dailycorebackend.dal.entity.User;
import be.dailycorebackend.dal.repository.UserRepository;
import be.dailycorebackend.security.UserPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhoneNumber()
        );

        User saved = userRepository.save(user);
        return buildAuthResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return buildAuthResponse(user);
    }

    public UserResponse getCurrentUser(UserPrincipal principal) {
        return UserResponse.from(
                userRepository.findById(principal.getId())
                        .orElseThrow(InvalidCredentialsException::new)
        );
    }

    public UserResponse updateProfile(UserPrincipal principal, UpdateUserRequest request) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(InvalidCredentialsException::new);

        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        return UserResponse.from(userRepository.save(user));
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        return new AuthResponse(token, UserResponse.from(user));
    }
}
