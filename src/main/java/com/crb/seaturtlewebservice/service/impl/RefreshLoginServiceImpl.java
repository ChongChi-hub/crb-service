package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.constant.TokenConstant;
import com.crb.seaturtlewebservice.domain.dto.response.LoginResponseDto;
import com.crb.seaturtlewebservice.domain.entity.User;
import com.crb.seaturtlewebservice.domain.mapper.LoginMapper;
import com.crb.seaturtlewebservice.domain.utility.TokenUtility;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.UserRepository;
import com.crb.seaturtlewebservice.service.RefreshLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class RefreshLoginServiceImpl implements RefreshLoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshLoginServiceImpl.class);
    private final UserRepository userRepository;
    private final LoginMapper loginMapper;

    @Override
    public ResponsePayload refreshLogin(HttpServletRequest request) {
        LOGGER.info("RefreshLoginServiceImpl -> refreshLogin invoked");
        String authToken = request.getHeader("Authorization");
        if (Optional.ofNullable(authToken).isEmpty()) {
            return ResponsePayload.builder()
                    .message("Token is empty!")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String token = authToken.substring(TokenConstant.STARTING_LETTER_IN_BEARER_TOKEN);
        String username = TokenUtility.getUsernameFromJwt(token);
        User currentUser = userRepository.findByUsername(username).orElse(null);

        if (Optional.ofNullable(currentUser).isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String refreshToken = TokenUtility.generateToken(authentication);
            currentUser.setRememberToken(refreshToken);

            userRepository.save(currentUser);
            LoginResponseDto loginResponseDto = loginMapper.entityToDto(currentUser, token);
            return ResponsePayload.builder()
                    .message("Login successfully!")
                    .status(HttpStatus.OK)
                    .data(loginResponseDto)
                    .build();
        } else {
            return ResponsePayload.builder()
                    .message("User not found!")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

    }
}