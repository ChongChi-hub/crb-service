package com.crb.seaturtlewebservice.domain.dto.response;

import lombok.Data;

@Data
public class AccountResponseDto {
    private String username;
    private String fullName;
    private String phone;
    private String email;
    private String role;
    private String isActivated;
}
