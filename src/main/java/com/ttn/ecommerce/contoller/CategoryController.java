package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.dto.CategoryDto;
import com.ttn.ecommerce.dto.CategoryListResponseDto;
import com.ttn.ecommerce.dto.MetadataFieldDto;
import com.ttn.ecommerce.dto.MetadataFieldValueDto;
import com.ttn.ecommerce.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/add/metadata")
    public ResponseEntity<String> addMetadata(@RequestBody MetadataFieldDto metadataFieldDto) {
        return categoryService.addMetadata(metadataFieldDto);
    }

    @GetMapping("/view/metadata")
    public ResponseEntity<?> getMetadata(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return categoryService.getMetadataField(offset, size);
    }

    @PostMapping("/add/category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/by/id/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryListResponseDto>> getAllCategories(@RequestParam Integer pageOffset, @RequestParam Integer pageSize, @RequestParam String sortBy) {

        List<CategoryListResponseDto> categories = categoryService.getAllCategories(pageOffset, pageSize, sortBy);
        return ResponseEntity.ok(categories);
    }


    @PutMapping("/update/{id}")
    private ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(id, categoryDto);
    }

    @PostMapping("/add/values")
    public ResponseEntity<?> addCategoryMetadataFieldValues(@RequestBody MetadataFieldValueDto metadataFieldValueDto) {
        return categoryService.addMetaFieldValues(metadataFieldValueDto);
    }

    @PostMapping("/update/values")
    private ResponseEntity<?> updateCategoryMetadataFieldValues(@RequestBody MetadataFieldValueDto metadataFieldValueDto) {
        return categoryService.updateMetaFieldValues(metadataFieldValueDto);
    }


    @GetMapping("/view/all/customer")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(required = false) Long categoryId) {
        return categoryService.getCategoriesCustomer(categoryId);
    }

    @GetMapping("/all/seller")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        return categoryService.getAllChildCategories();
    }
}
