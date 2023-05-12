package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.CategoryMetadataField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryMetadataFieldRepository extends JpaRepository<CategoryMetadataField, Long> {

    Optional<Object> findByName(String name);

    Optional<CategoryMetadataField> findById(Long id);
}
