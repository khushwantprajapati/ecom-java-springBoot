package com.ttn.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {
    @NotBlank(message = "Old password is mandatory")
    private String oldPassword;

    @NotBlank(message = "New password is mandatory")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#@$?]).{8,15}$",
            message = "Password must contain 8-15 characters" +
                    " with at least one uppercase letter, one lowercase letter, one special character and one digit")
    private String password;

    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;
}