package com.ttn.ecommerce.dto.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDto {

    Long id;
    String email;
    String FirstName;
    String LastName;
    String Contact;
    Boolean isActive;
}
