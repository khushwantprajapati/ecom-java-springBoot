package com.ttn.ecommerce.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.net.URI;
import java.util.Map;

@Getter
@Setter
public class ViewProductVariationDto {
    private Long productVariationID;
    private Long quantityAvailable;
    private Long price;
    private Map<String,String> metadata;
    private URI primaryImage;
}
