package com.ttn.ecommerce.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ProductVariationDto {


    private Long id;
    private Long quantityAvailable;
    private Long price;
    private Map<String,String> metadata;
    private String primaryImageName;
    private Boolean isActive;
    private Long productId;
    private Set<Long> metadataFieldValuesIds;

}
