package com.ttn.ecommerce.dto.seller;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerProfileDto {

    Long id;
    String firstName;
    String middleName;
    String lastName;
    Boolean isActive;
    String companyContact;
    String companyName;
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$", message = "GST must be a valid GST number")
    String gst;

    private String city;

    private String state;

    private String country;

    private String addressLine;

    @Pattern(regexp = "^\\d{5}(?:[-\\s]\\d{4})?$", message = "Zip code is not valid")
    private String zipCode;

    private String label;

}
