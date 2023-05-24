package com.ttn.ecommerce.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDto {

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format. Please provide a valid email address.")
    private String email;

    @NotBlank(message = "First name is required.")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Password is required.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#@$?]).{8,15}$",
            message = "Password must be 8-15 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character (#@$?).")
    private String password;

    @NotBlank(message = "Confirm password is required.")
    private String confirmPassword;

    @NotBlank(message = "Contact number is required.")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Invalid phone number format. Please enter a 10-digit number.")
    private String contact;

}

