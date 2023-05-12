package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.dto.ProductDto;
import com.ttn.ecommerce.dto.ProductResponseDto;
import com.ttn.ecommerce.service.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/add/seller")
    ResponseEntity<?> addProduct(@RequestHeader String token, @RequestBody ProductDto productDto) {
        return productService.addProduct(token, productDto);
    }

//    @PostMapping("/add/variation")
//    public ResponseEntity<?> addProductVariation(@RequestBody ProductVariationDto productVariationDto) {
//    return productService.addProductVariation(productVariationDto);
//    }

    @PostMapping("/view/{id}/seller")
    ResponseEntity<?> viewProduct(@RequestHeader String token, @PathVariable Long id) {
        return productService.viewProduct(token, id);
    }

    @PostMapping("/all/seller")
    ResponseEntity<List<ProductResponseDto>> viewALl(HttpServletRequest request) {
        return productService.viewAllProduct(request);
    }

    @PostMapping("/delete/{id}/seller")
    ResponseEntity<?> deleteProduct(HttpServletRequest request, @PathVariable Long id) {
        return productService.deleteProduct(request, id);
    }

    @PostMapping("/update/{productId}/seller")
    ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        return productService.updateProduct(productId, productDto);
    }

    // customer

    @PostMapping("/view/{id}/customer")
    ResponseEntity<?> viewProductCustomer(@PathVariable Long id) {
        return productService.viewProductCustomer(id);
    }

    @GetMapping("/view/category/{categoryId}/customer")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@PathVariable Long categoryId) {
        return productService.getAllProductsByCategory(categoryId);
    }

    @GetMapping("/view/similar/{id}/customer")
    ResponseEntity<?> viewSimilarProduct(@PathVariable Long id) {
        return productService.getSimilarProducts(id);
    }

    @GetMapping("/view/{id}/admin")
    ResponseEntity<?> viewProductAdmin(@PathVariable Long id) {
        return productService.getProductAdmin(id);
    }

    @GetMapping("/view/all/admin")
    ResponseEntity<?> viewAllProductAdmin() {
        return productService.viewAllProducts();
    }

    @PutMapping("/admin/{id}/deactivate")
    public ResponseEntity<?> deactivateProduct(@PathVariable Long id) {
        return productService.deactivateProduct(id);
    }

    @PutMapping("/admin/{id}/activate")
    public ResponseEntity<?> activateProduct(@PathVariable Long id) {
        return productService.activateProduct(id);
    }

}
