package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldValues extends JpaRepository<CategoryMetadataFieldValues, Long> {
}
