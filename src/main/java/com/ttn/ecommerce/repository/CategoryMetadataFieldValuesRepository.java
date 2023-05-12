package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.CategoryMetadataFieldValues;
import com.ttn.ecommerce.model.FieldValuesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMetadataFieldValuesRepository extends JpaRepository<CategoryMetadataFieldValues, Long> {
    boolean existsByFieldValuesId(FieldValuesId fieldValuesId);

    CategoryMetadataFieldValues findByFieldValuesId(FieldValuesId fieldValuesId);


}
