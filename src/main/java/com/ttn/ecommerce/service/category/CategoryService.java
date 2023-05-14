package com.ttn.ecommerce.service.category;

import com.ttn.ecommerce.dto.category.CategoryDto;
import com.ttn.ecommerce.dto.category.CategoryListResponseDto;
import com.ttn.ecommerce.dto.metadata.MetadataFieldDto;
import com.ttn.ecommerce.dto.metadata.MetadataFieldValueDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    ResponseEntity<String> addMetadata(MetadataFieldDto metadataField);

    ResponseEntity<?> getMetadataField(int offset, int size);

    ResponseEntity<?> addCategory(CategoryDto categoryDto);

    ResponseEntity<?> getCategory(Long id);

    List<CategoryListResponseDto> getAllCategories(int pageOffset, int pageSize, String sortBy);

    ResponseEntity<?> updateCategory(Long categoryId, CategoryDto categoryDto);

    ResponseEntity<?> addMetaFieldValues
            (MetadataFieldValueDto metadataFieldValueDto);

    ResponseEntity<?> updateMetaFieldValues(MetadataFieldValueDto metadataFieldValueDto);


    ResponseEntity<List<CategoryDto>> getCategoriesCustomer(Long categoryId);

    ResponseEntity<List<CategoryDto>> getAllChildCategories();
}
