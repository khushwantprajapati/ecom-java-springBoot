package com.ttn.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
public class ProductVariationRequest {
    private Long productId;
    private Map<String, String> metadataFieldValues;
    private int quantity;
    private Long price;

}
