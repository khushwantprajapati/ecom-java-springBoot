package com.ttn.ecommerce.dto.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerProfileDto {

    Long id;
    String firstName;
    String middleName;
    String lastName;
    String contact;
    Boolean isActive;


}
