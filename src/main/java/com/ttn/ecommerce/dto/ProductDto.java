package com.ttn.ecommerce.dto;

import com.ttn.ecommerce.model.Seller;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private String name;
    private String description;
    private String brand;
    private Seller seller;
    private Long category;
    private Boolean isCancellable;
    private Boolean isReturnable;



}
