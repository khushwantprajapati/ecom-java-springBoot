package com.ttn.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @Size(max = 10, message = "Label cannot exceed 10 characters")
    private String label;


}
