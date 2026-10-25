package com.crb.seaturtlewebservice.domain.dto.response;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String token;
    private String role;
}