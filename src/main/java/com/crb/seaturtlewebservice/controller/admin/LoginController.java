package com.crb.seaturtlewebservice.controller.admin;

import com.crb.seaturtlewebservice.domain.dto.request.LoginRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.email.ForgotPasswordRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.email.SendEMailRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.service.EmailService;
import com.crb.seaturtlewebservice.service.LoginService;
import com.crb.seaturtlewebservice.service.RefreshLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final RefreshLoginService refreshLoginService;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<ResponsePayload> login(HttpServletRequest request,
                                                 @Valid @RequestBody(required = false) LoginRequestDto loginRequestDto) {
        ResponsePayload responsePayload;
        if (Optional.ofNullable(loginRequestDto).isEmpty()) {
            responsePayload = refreshLoginService.refreshLogin(request);
        } else {
            responsePayload = loginService.login(loginRequestDto);
        }
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping("/refresh-login")
    public ResponseEntity<ResponsePayload> refreshLogin(HttpServletRequest request) {
        ResponsePayload responsePayload = refreshLoginService.refreshLogin(request);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponsePayload> logout(HttpServletRequest request) {
        return new ResponseEntity<>(loginService.logout(request), HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponsePayload> sendVerifyCode(@RequestBody SendEMailRequestDto sendEMailRequestDto) {
        if (Optional.ofNullable(sendEMailRequestDto).isPresent()) {
            new ResponseEntity<>("Request not found", HttpStatus.BAD_REQUEST);
        }
        ResponsePayload responsePayload = emailService.sendVerifyCodeAndSave(sendEMailRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponsePayload> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) {
        if (Optional.ofNullable(forgotPasswordRequestDto).isPresent()) {
            new ResponseEntity<>("Request not found", HttpStatus.BAD_REQUEST);
        }
        ResponsePayload responsePayload = loginService.forgotPassword(forgotPasswordRequestDto);
        return new ResponseEntity<>(responsePayload, responsePayload.getStatus());
    }
}