package be.dailycorebackend.bll.service;

import be.dailycorebackend.api.exception.InvalidCredentialsException;
import be.dailycorebackend.dal.entity.User;
import be.dailycorebackend.dal.repository.UserRepository;
import be.dailycorebackend.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserPrincipal getCurrentUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new InvalidCredentialsException();
        }
        return principal;
    }

    public Long getCurrentUserId() {
        return getCurrentUserPrincipal().getId();
    }

    public User getCurrentUser() {
        return userRepository.findById(getCurrentUserId())
                .orElseThrow(InvalidCredentialsException::new);
    }
}
