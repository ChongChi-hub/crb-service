package com.crb.seaturtlewebservice.service;

import com.crb.seaturtlewebservice.domain.dto.request.email.SendEMailRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.email.SendVerifyCodeRequestDto;
import com.crb.seaturtlewebservice.payload.ResponsePayload;

public interface EmailService {
    void sendVerifyCode(SendVerifyCodeRequestDto sendVerifyCodeRequestDto);
    ResponsePayload sendVerifyCodeAndSave(SendEMailRequestDto sendEMailRequestDto);
}
