package com.ttn.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProductVariationDto {


    private Long id;
    private Integer quantityAvailable;
    private Integer price;
    private String metadata;
    private String primaryImageName;
    private Boolean isActive;
    private Long productId;
    private Set<Long> metadataFieldValuesIds;

}
