package com.crb.seaturtlewebservice.domain.dto.request.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static com.crb.seaturtlewebservice.constant.AccountConstant.REGEX_PASSWORD;

@Data
public class ForgotPasswordRequestDto {
    @NotBlank(message = "Verify code is required")
    private String verifyCode;
    @NotBlank(message = "Username is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = REGEX_PASSWORD,
            message = "Password has 6 characters or more, including numbers, lowercase letters and uppercase letters")
    private String newPassword;
    @NotBlank(message = "Confirm password is required")
    @Pattern(regexp = REGEX_PASSWORD,
            message = "Password has 6 characters or more, including numbers, lowercase letters and uppercase letters")
    private String confirmPassword;
}
