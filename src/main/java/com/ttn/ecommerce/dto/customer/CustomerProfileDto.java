package com.ttn.ecommerce.dto.customer;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerProfileDto {

    Long id;
    String firstName;
    String middleName;
    String lastName;
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Enter valid phone number.")
    String contact;
    Boolean isActive;


}
