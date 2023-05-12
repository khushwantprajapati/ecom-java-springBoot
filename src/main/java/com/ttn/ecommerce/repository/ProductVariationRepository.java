package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {
}
