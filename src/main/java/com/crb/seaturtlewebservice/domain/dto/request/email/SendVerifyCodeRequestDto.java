package com.crb.seaturtlewebservice.domain.dto.request.email;

import lombok.Data;

@Data
public class SendVerifyCodeRequestDto {
    private String to;
    private String verifyCode;
}
