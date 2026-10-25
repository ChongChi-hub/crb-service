package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.constant.TokenConstant;
import com.crb.seaturtlewebservice.domain.dto.request.LoginRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.email.ForgotPasswordRequestDto;
import com.crb.seaturtlewebservice.domain.dto.response.LoginResponseDto;
import com.crb.seaturtlewebservice.domain.entity.User;
import com.crb.seaturtlewebservice.domain.mapper.LoginMapper;
import com.crb.seaturtlewebservice.domain.utility.TokenUtility;
import com.crb.seaturtlewebservice.exception.PasswordNotMatchException;
import com.crb.seaturtlewebservice.exception.UserDeletedException;
import com.crb.seaturtlewebservice.exception.UserLockedException;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.UserRepository;
import com.crb.seaturtlewebservice.security.TokenProvider;
import com.crb.seaturtlewebservice.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class LoginServiceImpl implements LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final LoginMapper loginMapper;
    private final PasswordEncoder encoder;

    @Override
    public ResponsePayload login(LoginRequestDto loginRequestDto) {
        LOGGER.info("LoginServiceImpl -> login invoked");
        try {
            // Authenticate login info
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getUsername(),
                    loginRequestDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Generate token
            String token = tokenProvider.generateToken(authentication);
            // Create login response dto
            User currentUser = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found"));
            checkUserStatus(currentUser);
            LocalDateTime currentTime = LocalDateTime.now();
            currentUser.setLastLogin(currentTime);
            currentUser.setRememberToken(token);
            userRepository.save(currentUser);
            LoginResponseDto loginResponseDto = loginMapper.entityToDto(currentUser, token);

            return ResponsePayload.builder()
                    .message("Login successfully!")
                    .status(HttpStatus.OK)
                    .data(loginResponseDto)
                    .build();
        } catch (Exception e) {
            return ResponsePayload.builder()
                    .message("Login failed!")
                    .status(HttpStatus.UNAUTHORIZED)
                    .error(e.getMessage())
                    .build();
        }
    }

    private void checkUserStatus(User user) {
        if (!user.getIsActivated()) {
            throw new UserLockedException("Account is locked");
        } else if (user.getIsDeleted()) {
            throw new UserDeletedException("Account is deleted");
        }
    }

    @Override
    public ResponsePayload logout(HttpServletRequest request) {
        LOGGER.info("LoginServiceImpl -> logout invoked");
        String bearerToken = request.getHeader("Authorization");
        if (Optional.ofNullable(bearerToken).isEmpty()) {
            return ResponsePayload.builder()
                    .message("Token is empty!")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        String token = bearerToken.substring(TokenConstant.STARTING_LETTER_IN_BEARER_TOKEN);
        String username = TokenUtility.getUsernameFromJwt(token);
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (Optional.ofNullable(currentUser).isPresent()) {
            currentUser.setRememberToken(null);
            userRepository.save(currentUser);
            SecurityContextHolder.clearContext();
            return ResponsePayload.builder()
                    .message("Logout successfully!!!")
                    .status(HttpStatus.OK)
                    .build();
        } else {
            return ResponsePayload.builder()
                    .message("User not found!")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @Override
    public ResponsePayload forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
        LOGGER.info("LoginServiceImpl -> forgot password invoked");
        try {
            String email = forgotPasswordRequestDto.getEmail();
            String verifyCode = forgotPasswordRequestDto.getVerifyCode();
            String newPassword = forgotPasswordRequestDto.getNewPassword();
            String confirmPassword = forgotPasswordRequestDto.getConfirmPassword();
            if (Optional.ofNullable(newPassword).isPresent()) {
                if (!confirmPassword.equals(newPassword)) {
                    throw new PasswordNotMatchException("Password not match");
                }
            }
            Optional<User> optionalUser =
                    userRepository.findUserByEmailAndVerifyCodeAndVerifyCodeExpirationAfter(
                            email,
                            verifyCode,
                            LocalDateTime.now()
                    );
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setPassword(encoder.encode(newPassword));
                user.setVerifyCode(null);
                user.setVerifyCodeExpiration(null);
                userRepository.save(user);
            }
            return ResponsePayload.builder()
                    .message("Password recovery successfully")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            LOGGER.error("LoginServiceImpl -> Error: {}", e.getMessage());
            return ResponsePayload.builder()
                    .message("Password recovery failed")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }
}