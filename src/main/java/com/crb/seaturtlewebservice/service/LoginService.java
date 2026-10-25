package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.domain.dto.request.email.ForgotPasswordRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.LoginRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {
    ResponsePayload login(LoginRequestDto loginRequestDto);

    ResponsePayload logout(HttpServletRequest request);

    ResponsePayload forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto);
}