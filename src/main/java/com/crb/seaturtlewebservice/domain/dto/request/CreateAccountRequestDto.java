package com.crb.seaturtlewebservice.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.crb.seaturtlewebservice.constant.AccountConstant.REGEX_PASSWORD;
import static com.crb.seaturtlewebservice.constant.AccountConstant.REGEX_PHONE;

@Data
public class CreateAccountRequestDto {
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = REGEX_PASSWORD,
            message = "Password has 6 characters or more, including numbers, lowercase letters and uppercase letters")
    private String password;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = REGEX_PHONE, message = "Phone number has a maximum of 10 numbers and starts with 0")
    private String phone;
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;
    @NotBlank(message = "Confirm password is required")
    @Pattern(regexp = REGEX_PASSWORD,
            message = "Password has 6 characters or more, including numbers, lowercase letters and uppercase letters")
    private String confirmPassword;
    @NotBlank(message = "Role is required")
    private String roleName;
    private Boolean isActivated;
}
