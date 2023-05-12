package com.ttn.ecommerce.model;

import com.ttn.ecommerce.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CategoryMetadataFieldValues {

    @EmbeddedId
    private FieldValuesId fieldValuesId;

    @ManyToOne
    @MapsId("category")
    private Category category;

    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> value;
    @ManyToOne
    @MapsId("categoryMetadataField")
    private CategoryMetadataField categoryMetadataField;
}
