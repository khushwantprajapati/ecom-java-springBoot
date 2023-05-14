package com.ttn.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {

    private Long id;

    @NotBlank(message = "Address line is mandatory")
    private String addressLine;

    @NotBlank(message = "City is mandatory")
    private String city;

    @NotBlank(message = "State is mandatory")
    private String state;

    @NotBlank(message = "Country is mandatory")
    private String country;

    @NotBlank(message = "Zip code is mandatory")
    @Pattern(regexp = "^\\d{5}(?:[-\\s]\\d{4})?$", message = "Zip code is not valid")
    private String zipCode;

    private String label;

}

