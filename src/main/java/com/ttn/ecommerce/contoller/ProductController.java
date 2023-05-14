package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.dto.ProductDto;
import com.ttn.ecommerce.dto.ProductResponseDto;
import com.ttn.ecommerce.service.product.ProductService;
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
    ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        return productService.addProduct(productDto);
    }


    @PostMapping("/view/{id}/seller")
    ResponseEntity<?> viewProduct(@PathVariable Long id) {
        return productService.viewProduct(id);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> viewAllProductsForAllSellers(
            @RequestParam(required = false) Integer max,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order
    ) {
        return productService.viewAllProduct(max, offset, sort, order);
    }


    @DeleteMapping("/delete/{id}/seller")
    ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
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
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@PathVariable Long categoryId,
                                                                     @RequestParam(required = false) Integer max,
                                                                     @RequestParam(required = false) Integer offset,
                                                                     @RequestParam(required = false) String sort,
                                                                     @RequestParam(required = false) String order) {
        return productService.getAllProductsByCategory(categoryId, max, offset, sort, order);
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
    public ResponseEntity<List<ProductResponseDto>> viewAllProductAdmin(@RequestParam(required = false) Integer max,
                                                                        @RequestParam(required = false) Integer offset,
                                                                        @RequestParam(required = false) String sort,
                                                                        @RequestParam(required = false) String order,
                                                                        @RequestParam(required = false) Long categoryId,
                                                                        @RequestParam(required = false) Long sellerId) {
        return productService.viewAllProducts(max, offset, sort, order, categoryId, sellerId);
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
