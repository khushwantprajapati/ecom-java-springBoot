package com.ttn.ecommerce.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDto {
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#@$?]).{8,15}$",
            message = "Password must contain 8-15 characters" +
                    " with at least one uppercase letter, one lowercase letter,one special character and one digit")
    private String password;
    private String confirmPassword;

}

