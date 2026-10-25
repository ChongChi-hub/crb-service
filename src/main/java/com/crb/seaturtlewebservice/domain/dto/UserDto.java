package com.crb.seaturtlewebservice.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String roleName;
    private Boolean isActivated;
    private Boolean isDeleted;
    private LocalDateTime lastLogin;
}
