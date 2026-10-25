package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.entity.User;
import com.crb.seaturtlewebservice.repository.UserRepository;
import com.crb.seaturtlewebservice.security.UserPrincipal;
import com.crb.seaturtlewebservice.service.UserPrincipalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserPrincipalServiceImpl implements UserPrincipalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserPrincipalServiceImpl.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("UserPrincipalServiceImpl -> loadUserByUsername invoked");
        User user = findUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username or email: " + username);
        }
        return UserPrincipal.create(user);
    }

    private User findUser(String input) {
        if (input.contains("@")) {
            return userRepository.findByEmail(input)
                    .orElseThrow(() -> new EntityNotFoundException("User with email not found"));
        } else {
            return userRepository.findByUsername(input)
                    .orElseThrow(() -> new EntityNotFoundException("User with username not found"));
        }
    }
}

