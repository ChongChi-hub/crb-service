package com.crb.seaturtlewebservice.domain.utility;

import com.crb.seaturtlewebservice.domain.entity.User;
import com.crb.seaturtlewebservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class SecurityUtility {
    private final UserRepository userRepository;


    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return userRepository.findByUsername(authentication.getName());
        }
        return Optional.empty();
    }
}