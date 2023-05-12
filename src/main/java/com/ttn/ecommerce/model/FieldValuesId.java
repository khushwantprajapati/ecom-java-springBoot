package com.ttn.ecommerce.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FieldValuesId implements Serializable {

    private Long category;
    private Long categoryMetadataField;

    public FieldValuesId() {

    }

    public FieldValuesId(Long category, Long categoryMetadataField) {
        this.category = category;
        this.categoryMetadataField = categoryMetadataField;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Long getCategoryMetadataField() {
        return categoryMetadataField;
    }

    public void setCategoryMetadataField(Long categoryMetadataField) {
        this.categoryMetadataField = categoryMetadataField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldValuesId fieldValuesId = (FieldValuesId) o;
        return Objects.equals(category, fieldValuesId.category) && Objects.equals(categoryMetadataField, fieldValuesId.categoryMetadataField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, categoryMetadataField);
    }
}
