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

    @NotBlank(message = "E-mail is mandatory")
    @Email(message = "E-mail is not valid")
    private String email;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#@$?]).{8,15}$",
            message = "Password must contain 8-15 characters" +
                    " with at least one uppercase letter, one lowercase letter,one special character and one digit")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "Contact number is mandatory")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Enter valid phone number.")
    private String contact;

}

