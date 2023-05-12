package com.ttn.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressDto {


    private String city;

    private String state;

    private String country;

    private String addressLine;

    private Integer zipCode;

    private String label;

}
