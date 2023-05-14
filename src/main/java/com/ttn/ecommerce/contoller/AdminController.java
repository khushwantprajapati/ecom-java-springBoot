package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.dto.category.CategoryDto;
import com.ttn.ecommerce.dto.category.CategoryListResponseDto;
import com.ttn.ecommerce.dto.metadata.MetadataFieldDto;
import com.ttn.ecommerce.dto.metadata.MetadataFieldValueDto;
import com.ttn.ecommerce.dto.product.ProductResponseDto;
import com.ttn.ecommerce.service.admin.AdminService;
import com.ttn.ecommerce.service.category.CategoryService;
import com.ttn.ecommerce.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/customers")
    public ResponseEntity<?> customerList
            (@RequestParam Integer pageOffset, @RequestParam Integer pageSize, @RequestParam String sortBy) {
        return adminService.findAllCustomer(pageOffset, pageSize, sortBy);
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> sellerList
            (@RequestParam Integer pageOffset, @RequestParam Integer pageSize, @RequestParam String sortBy) {
        return adminService.findAllSeller(pageOffset, pageSize, sortBy);
    }

    @PutMapping("/customer/activate/{id}")
    public ResponseEntity<?> activateCustomer(@PathVariable Long id) {
        return adminService.activateUser(id);
    }

    @PutMapping("/customer/deactivate/{id}")
    public ResponseEntity<?> deactivateCustomer(@PathVariable Long id) {
        return adminService.deactivateUser(id);
    }


    @GetMapping("/view/product/{id}")
    ResponseEntity<?> viewProductAdmin(@PathVariable Long id) {
        return productService.viewProductByAdminById(id);
    }

    @GetMapping("/view/product/all")
    public ResponseEntity<List<ProductResponseDto>> viewAllProductAdmin(@RequestParam(required = false) Integer max,
                                                                        @RequestParam(required = false) Integer offset,
                                                                        @RequestParam(required = false) String sort,
                                                                        @RequestParam(required = false) String order,
                                                                        @RequestParam(required = false) Long categoryId,
                                                                        @RequestParam(required = false) Long sellerId) {
        return productService.viewAllProductByAdmin(max, offset, sort, order, categoryId, sellerId);
    }


    @PutMapping("/deactivate/product/{id}")
    public ResponseEntity<?> deactivateProduct(@PathVariable Long id) {
        return productService.deactivateProduct(id);
    }

    @PutMapping("/activate/product/{id}")
    public ResponseEntity<?> activateProduct(@PathVariable Long id) {
        return productService.activateProduct(id);
    }


    // category
    @PostMapping("/add/metadata/field")
    public ResponseEntity<String> addMetadata(@RequestBody MetadataFieldDto metadataFieldDto) {
        return categoryService.addMetadata(metadataFieldDto);
    }

    @GetMapping("/view/metadata/field")
    public ResponseEntity<?> getMetadata(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return categoryService.getMetadataField(offset, size);
    }

    @PostMapping("/add/category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @GetMapping("/view/category/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping("/view/category/all")
    public ResponseEntity<List<CategoryListResponseDto>> getAllCategories(@RequestParam Integer pageOffset, @RequestParam Integer pageSize, @RequestParam String sortBy) {

        List<CategoryListResponseDto> categories = categoryService.getAllCategories(pageOffset, pageSize, sortBy);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/update/category/{id}")
    private ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(id, categoryDto);
    }

    @PostMapping("/add/metadata/values")
    public ResponseEntity<?> addCategoryMetadataFieldValues(@RequestBody MetadataFieldValueDto metadataFieldValueDto) {
        return categoryService.addMetaFieldValues(metadataFieldValueDto);
    }

    @PutMapping("/update/metadata/values")
    private ResponseEntity<?> updateCategoryMetadataFieldValues(@RequestBody MetadataFieldValueDto metadataFieldValueDto) {
        return categoryService.updateMetaFieldValues(metadataFieldValueDto);
    }
}
