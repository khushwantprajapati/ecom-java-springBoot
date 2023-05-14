package com.ttn.ecommerce.service.product;

import com.ttn.ecommerce.dto.product.ProductDto;
import com.ttn.ecommerce.dto.product.ProductResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<?> addProductSeller(ProductDto productDto);


    ResponseEntity<?> viewProductById(Long id);

    ResponseEntity<List<ProductResponseDto>> viewAllProductSeller(Integer max, Integer offset, String sort, String order);

    ResponseEntity<?> deleteProductSeller(Long id);

    ResponseEntity<?> updateProductSeller(Long productId, ProductDto productDto);

    ResponseEntity<?> viewProductByCustomerById(Long id);

    ResponseEntity<List<ProductDto>> getAllProductsByCategoryByCustomer(Long categoryId, Integer max, Integer offset, String sort, String order);

    ResponseEntity<List<ProductDto>> getSimilarProductsByCustomerById(Long productId);


    ResponseEntity<?> viewProductByAdminById(Long id);

    ResponseEntity<List<ProductResponseDto>> viewAllProductByAdmin(Integer max, Integer offset, String sort, String order, Long categoryId, Long sellerId);

    ResponseEntity<?> deactivateProduct(Long id);

    ResponseEntity<?> activateProduct(Long id);
}
