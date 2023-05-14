package com.ttn.ecommerce.service.product;

import com.ttn.ecommerce.dto.ProductDto;
import com.ttn.ecommerce.dto.ProductResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<?> addProduct(ProductDto productDto);


    ResponseEntity<?> viewProduct(Long id);

    ResponseEntity<List<ProductResponseDto>> viewAllProduct(Integer max, Integer offset, String sort, String order);

    ResponseEntity<?> deleteProduct(Long id);

    ResponseEntity<?> updateProduct(Long productId, ProductDto productDto);

    ResponseEntity<?> viewProductCustomer(Long id);

    ResponseEntity<List<ProductDto>> getAllProductsByCategory(Long categoryId, Integer max, Integer offset, String sort, String order);

    ResponseEntity<List<ProductDto>> getSimilarProducts(Long productId);


    ResponseEntity<?> getProductAdmin(Long id);

    ResponseEntity<List<ProductResponseDto>> viewAllProducts(Integer max, Integer offset, String sort, String order, Long categoryId, Long sellerId);

    ResponseEntity<?> deactivateProduct(Long id);

    ResponseEntity<?> activateProduct(Long id);
}
