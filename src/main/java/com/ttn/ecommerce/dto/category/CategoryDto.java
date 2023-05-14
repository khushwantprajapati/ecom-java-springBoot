package com.ttn.ecommerce.dto.category;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto {


    private String name;
    private Long parentCategoryId;

}
