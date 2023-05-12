package com.ttn.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private Boolean isCancellable;
    private Boolean isReturnable;
    private Boolean isActive;
    private Boolean isDeleted;


}
