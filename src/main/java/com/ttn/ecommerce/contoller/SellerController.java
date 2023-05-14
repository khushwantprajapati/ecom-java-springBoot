package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.ChangePasswordDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.dto.category.CategoryDto;
import com.ttn.ecommerce.dto.product.ProductDto;
import com.ttn.ecommerce.dto.product.ProductResponseDto;
import com.ttn.ecommerce.dto.seller.SellerDto;
import com.ttn.ecommerce.dto.seller.SellerProfileDto;
import com.ttn.ecommerce.service.category.CategoryService;
import com.ttn.ecommerce.service.product.ProductService;
import com.ttn.ecommerce.service.product.ProductServiceImpl;
import com.ttn.ecommerce.service.seller.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/register")
    public ResponseEntity<?> saveSeller(@Valid @RequestBody SellerDto sellerDto) {
        return sellerService.saveSeller(sellerDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> customerProfile() {
        return sellerService.viewProfile();
    }

    @RequestMapping(value = "/profile/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerProfile(@Valid @RequestBody SellerProfileDto sellerProfileDto) {
        return sellerService.updateUserProfile(sellerProfileDto);
    }

    @RequestMapping(value = "/password/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerPassword(@Valid @RequestBody ChangePasswordDto passwordDto) {
        return sellerService.updatePassword(passwordDto);
    }

    @RequestMapping(value = "/update/address", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateAddress(@Valid @RequestBody AddressDto addressDto) {
        return sellerService.updateAddress(addressDto);
    }
    @PostMapping("/add/product")
    ResponseEntity<?> addProduct(@RequestBody ProductDto productDto) {
        return productService.addProductSeller(productDto);
    }
    @GetMapping("/view/product/{id}")
    ResponseEntity<?> viewProduct(@PathVariable Long id) {
        return productService.viewProductById(id);
    }

    @GetMapping("/view/product/all")
    public ResponseEntity<List<ProductResponseDto>> viewAllProductsForAllSellers(
            @RequestParam(required = false) Integer max,
            @RequestParam(required = false) Integer offset,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order
    ) {
        return productService.viewAllProductSeller(max, offset, sort, order);
    }

    @DeleteMapping("/delete/product/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productService.deleteProductSeller(id);
    }

    @PostMapping("/update/product/{productId}")
    ResponseEntity<?> updateProduct(@PathVariable Long productId, @RequestBody ProductDto productDto) {
        return productService.updateProductSeller(productId, productDto);
    }

    @GetMapping("view/category/all")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        return categoryService.getAllChildCategories();
    }

}
