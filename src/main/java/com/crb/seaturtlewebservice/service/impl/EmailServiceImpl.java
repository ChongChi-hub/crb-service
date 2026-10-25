package com.crb.seaturtlewebservice.service.impl;

import com.crb.seaturtlewebservice.domain.dto.request.email.SendEMailRequestDto;
import com.crb.seaturtlewebservice.domain.dto.request.email.SendVerifyCodeRequestDto;
import com.crb.seaturtlewebservice.domain.entity.User;
import com.crb.seaturtlewebservice.domain.utility.VerifyCodeGenerate;
import com.crb.seaturtlewebservice.payload.ResponsePayload;
import com.crb.seaturtlewebservice.repository.UserRepository;
import com.crb.seaturtlewebservice.service.EmailService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.crb.seaturtlewebservice.constant.VerifyCodeConstant.VERIFY_CODE_EXPIRATION_PLUS_MINUTE;

@Service
@AllArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final VerifyCodeGenerate verifyCodeGenerate;
    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void sendVerifyCode(SendVerifyCodeRequestDto sendVerifyCodeRequestDto) {
        LOGGER.info("EmailServiceImpl -> send verify code invoked");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(sendVerifyCodeRequestDto.getTo());
        message.setSubject("YOUR VERIFY CODE");
        message.setText("YOUR VERIFY CODE IS: " + sendVerifyCodeRequestDto.getVerifyCode());
        javaMailSender.send(message);
    }

    @Override
    public ResponsePayload sendVerifyCodeAndSave(SendEMailRequestDto sendEMailRequestDto) {
        LOGGER.info("EmailServiceImpl -> send verify code and save invoked");
        try {
            SendVerifyCodeRequestDto sendVerifyCodeRequestDto = new SendVerifyCodeRequestDto();
            sendVerifyCodeRequestDto.setTo(sendEMailRequestDto.getEmail());
            sendVerifyCodeRequestDto.setVerifyCode(verifyCodeGenerate.generateVerifyCode());
            sendVerifyCode(sendVerifyCodeRequestDto);
            saveVerifyCode(sendVerifyCodeRequestDto.getTo(), sendVerifyCodeRequestDto.getVerifyCode());
            return ResponsePayload.builder()
                    .message("Send verify code successfully")
                    .status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            LOGGER.error("EmailServiceImpl -> Error:{}", e.getMessage());
            return ResponsePayload.builder()
                    .message("Send verify code failed")
                    .status(HttpStatus.BAD_REQUEST)
                    .error(e.getMessage())
                    .build();
        }
    }

    private void saveVerifyCode(String email, String verifyCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email is not found"));
        user.setVerifyCode(verifyCode);
        user.setVerifyCodeExpiration(LocalDateTime.now().plusMinutes(VERIFY_CODE_EXPIRATION_PLUS_MINUTE));
        userRepository.save(user);
    }
}
