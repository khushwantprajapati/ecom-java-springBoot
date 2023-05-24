package com.ttn.ecommerce.dto.customer;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerProfileDto {


    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "Enter a valid phone number.")
    private String contact;

    private Boolean isActive;
}
