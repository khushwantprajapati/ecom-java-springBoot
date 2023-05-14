package com.ttn.ecommerce.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ViewAllVariationDto {
    private Long productId;
    private String productName;
    private String brand;
    private String description;
    private List<ViewProductVariationDto> productVariations;
}
