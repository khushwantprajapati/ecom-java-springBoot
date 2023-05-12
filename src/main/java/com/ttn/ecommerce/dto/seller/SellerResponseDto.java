package com.ttn.ecommerce.dto.seller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerResponseDto {

    Long id;
    String email;
    String FirstName;
    String LastName;
    String companyName;
    String companyContact;
    Boolean isActive;
}
