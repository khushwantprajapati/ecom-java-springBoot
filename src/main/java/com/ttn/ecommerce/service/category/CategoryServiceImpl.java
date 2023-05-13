package com.ttn.ecommerce.service.category;

import com.ttn.ecommerce.dto.CategoryDto;
import com.ttn.ecommerce.dto.CategoryListResponseDto;
import com.ttn.ecommerce.dto.MetadataFieldDto;
import com.ttn.ecommerce.dto.MetadataFieldValueDto;
import com.ttn.ecommerce.exception.GenericException;
import com.ttn.ecommerce.model.Category;
import com.ttn.ecommerce.model.CategoryMetadataField;
import com.ttn.ecommerce.model.CategoryMetadataFieldValues;
import com.ttn.ecommerce.model.FieldValuesId;
import com.ttn.ecommerce.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMetadataFieldRepository metadataFieldRepository;

    @Autowired
    CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    FieldValues fieldValues;

    @Override
    public ResponseEntity<String> addMetadata(MetadataFieldDto metadataField) {
        // Create a new CategoryMetadataField object
        CategoryMetadataField metadata = new CategoryMetadataField();

        // Check if a metadata field with the same name already exists
        if (metadataFieldRepository.findByName(metadataField.getName()).isPresent()) {
            // If it exists, throw a GenericException with a message indicating the duplicate field name
            throw new GenericException("Metadata field with name '"
                    + metadataField.getName() + "' already exists.", HttpStatus.BAD_REQUEST);
        }

        // Set the name of the new metadata field
        metadata.setName(metadataField.getName());

        // Save the new metadata field to the repository
        metadataFieldRepository.save(metadata);

        // Return a success response with the ID of the newly created metadata field
        return ResponseEntity.status(HttpStatus.CREATED).body("Metadata field with ID "
                + metadata.getId() + " created successfully.");
    }



    @Override
    public ResponseEntity<List<MetadataFieldDto>> getMetadataField(int offset, int size) {
        // Get a list of all metadata fields with pagination and sorting
        Page<CategoryMetadataField> metadataFields = metadataFieldRepository.findAll(PageRequest.of(offset, size, Sort.by(Sort.Direction.ASC, "id")));

        // Create a list to store DTO objects
        List<MetadataFieldDto> metadataFieldDtoList = new ArrayList<>();

        // Iterate through the metadata fields and create DTO objects
        for (CategoryMetadataField metadataField : metadataFields) {
            MetadataFieldDto metadataFieldDto = new MetadataFieldDto();
            metadataFieldDto.setName(metadataField.getName());
            metadataFieldDtoList.add(metadataFieldDto);
        }

        // Return the list of DTO objects in the response
        return ResponseEntity.ok().body(metadataFieldDtoList);
    }


    @Override
    public ResponseEntity<?> addCategory(CategoryDto categoryDto) {

        // Check if category name already exists at root level
        Category existingCategory = categoryRepository.findByNameAndParentCategoryIsNull(categoryDto.getName());
        if (existingCategory != null) {
            return ResponseEntity.badRequest().body("Category name already exists at root level.");
        }

        // Check if parent category is associated with any existing product
        if (categoryDto.getParentCategoryId() != null) {
            Long parentId = categoryDto.getParentCategoryId();
            boolean isParentAssociatedWithProduct = productRepository.existsByCategoryId(parentId);
            if (isParentAssociatedWithProduct) {
                return ResponseEntity.badRequest().body("Parent category is associated with existing products.");
            }
        }

        // Create new category
        Category category = new Category();
        category.setName(categoryDto.getName());

        // Set parent category if provided
        if (categoryDto.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDto.getParentCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
            category.setParentCategory(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }


    @Override
    public ResponseEntity<?> getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(category);
    }

    @Override
    public List<CategoryListResponseDto> getAllCategories(int pageOffset, int pageSize, String sortBy) {
        if (pageOffset < 0) {
            throw new GenericException("", HttpStatus.BAD_REQUEST);
        }
        if (pageSize <= 0) {
            throw new GenericException("", HttpStatus.BAD_REQUEST);
        }
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }
        PageRequest page = PageRequest.of(pageOffset, pageSize, Sort.Direction.ASC, sortBy);

        Page<Category> categoriesPage = categoryRepository.findAll(page);

        List<CategoryListResponseDto> categoryList = new ArrayList<>();

        for (Category category : categoriesPage.getContent()) {
            CategoryListResponseDto categoryDto = new CategoryListResponseDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoryList.add(categoryDto);
        }

        return categoryList;
    }

    @Override
    public ResponseEntity<?> updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        if (categoryDto.getParentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDto.getParentCategoryId()).orElse(null);
            if (parentCategory == null) {
                return ResponseEntity.badRequest().body("Parent category not found");
            }
            category.setParentCategory(parentCategory);
        }
        categoryRepository.save(category);
        return ResponseEntity.ok().body(category);
    }


    @Override
    public ResponseEntity<?> addMetaFieldValues(MetadataFieldValueDto metadataFieldValueDto) {

        Category category = categoryRepository.findById(metadataFieldValueDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));


        CategoryMetadataField categoryMetadataField = metadataFieldRepository.findById(metadataFieldValueDto.getMetadataId())
                .orElseThrow(() -> new EntityNotFoundException("Metadata Field not found"));


        FieldValuesId fieldValuesId = new FieldValuesId(metadataFieldValueDto.getCategoryId(), metadataFieldValueDto.getMetadataId());
        if (categoryMetadataFieldValuesRepository.existsByFieldValuesId(fieldValuesId)) {
            return ResponseEntity.badRequest().body("Values for this Category-Metadata Field combination already exist.");
        }

        // Create a new CategoryMetaDataFieldValues object and save it
        CategoryMetadataFieldValues catFieldValues = new CategoryMetadataFieldValues();
        catFieldValues.setCategory(category);
        catFieldValues.setCategoryMetadataField(categoryMetadataField);
        catFieldValues.setFieldValuesId(fieldValuesId);
        catFieldValues.setValue(metadataFieldValueDto.getValues().stream().toList());
        categoryMetadataFieldValuesRepository.save(catFieldValues);

        return ResponseEntity.ok().body("Metadata Field Values saved ");

    }

    @Override
    public ResponseEntity<?> updateMetaFieldValues(MetadataFieldValueDto metadataFieldValueDto) {

        categoryRepository.findById(metadataFieldValueDto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        metadataFieldRepository.findById(metadataFieldValueDto.getMetadataId())
                .orElseThrow(() -> new EntityNotFoundException("Metadata Field not found"));

        FieldValuesId catConnector = new FieldValuesId(metadataFieldValueDto.getCategoryId(), metadataFieldValueDto.getMetadataId());

        CategoryMetadataFieldValues existingCatFieldValues = categoryMetadataFieldValuesRepository.findByFieldValuesId(catConnector);
        if (existingCatFieldValues == null) {
            return ResponseEntity.badRequest().body("Metadata Field Values not found for this Category-Metadata Field combination.");
        }
        existingCatFieldValues.setValue(metadataFieldValueDto.getValues().stream().toList());
        categoryMetadataFieldValuesRepository.save(existingCatFieldValues);
        return ResponseEntity.ok().body("Metadata Field Values updated successfully.");
    }

    // customer
    @Override
    public ResponseEntity<List<CategoryDto>> getCategoriesCustomer(Long categoryId) {
        List<Category> categories;
        if (categoryId == null) {
            // no category ID provided, so return all root level categories
            categories = categoryRepository.findAllByParentCategoryIsNull();
        } else {
            // category ID provided, so return all immediate child categories
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            if (categoryOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Category category = categoryOptional.get();
            categories = category.getChildCategories();
        }

        // convert categories to DTOs
        List<CategoryDto> categoryDtos = categories.stream()
                .map(category -> {
                    CategoryDto categoryDto = new CategoryDto();
                    categoryDto.setName(category.getName());
                    if (category.getParentCategory() != null) {
                        categoryDto.setParentCategoryId(category.getParentCategory().getId());
                    }
                    return categoryDto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(categoryDtos);
    }

}
