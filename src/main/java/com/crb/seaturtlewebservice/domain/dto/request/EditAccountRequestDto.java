package com.crb.seaturtlewebservice.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.crb.seaturtlewebservice.constant.AccountConstant.REGEX_PASSWORD;
import static com.crb.seaturtlewebservice.constant.AccountConstant.REGEX_PHONE;

@Data
public class EditAccountRequestDto {
    private String username;
    private String fullName;
    @Email(message = "Invalid email")
    private String email;
    @Pattern(regexp = REGEX_PHONE, message = "Phone number has a maximum of 10 numbers and starts with 0")
    private String phone;
    private String roleName;
    private Boolean isActivated;
    @Pattern(regexp = REGEX_PASSWORD,
            message = "Password has 6 characters or more, including numbers, lowercase letters and uppercase letters")
    private String confirmPassword;
}
