package com.ttn.ecommerce.dto.seller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerDto {

    @NotBlank(message = "E-mail is mandatory")
    @Email(message = "E-mail is not valid")
    private String email;

    private String firstName;

    private String lastName;

    @NotBlank(message = "Password in mandatory")
    @Pattern(regexp = "(?=^.{8,15}$)((?=.*\\d)|(?=.*\\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$"
            , message = "Password must contain at least 8 to 15 characters" +
            " with at least one uppercase letter, one lowercase letter, and one digit")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "GST number is mandatory")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$", message = "GST must be a valid GST number")
    private String gst;

    @NotBlank(message = "Company name is mandatory")
    private String companyName;

    private String companyContact;

    private String city;

    private String state;

    private String country;

    private String addressLine;

    private Integer zipCode;

    private String label;


}
