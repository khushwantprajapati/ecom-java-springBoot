package com.ttn.ecommerce.dto.seller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerProfileDto {

    Long id;
    String firstName;
    String lastName;
    Boolean isActive;
    String companyContact;
    String companyName;
    String gst;

    private String city;

    private String state;

    private String country;

    private String addressLine;

    private Integer zipCode;

    private String label;

}
