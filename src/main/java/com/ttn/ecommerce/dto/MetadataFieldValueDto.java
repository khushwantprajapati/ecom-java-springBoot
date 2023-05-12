package com.ttn.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class MetadataFieldValueDto {
    private Long categoryId;
    private Long metadataId;
    private Set<String> values;
}
