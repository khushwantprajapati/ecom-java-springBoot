package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.ChangePasswordDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.dto.category.CategoryDto;
import com.ttn.ecommerce.dto.customer.CustomerDto;
import com.ttn.ecommerce.dto.customer.CustomerProfileDto;
import com.ttn.ecommerce.dto.product.ProductDto;
import com.ttn.ecommerce.service.category.CategoryService;
import com.ttn.ecommerce.service.customer.CustomerService;
import com.ttn.ecommerce.service.product.ProductService;
import com.ttn.ecommerce.service.product.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerServiceImpl;
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        return customerServiceImpl.createAccount(customerDto);
    }

    @RequestMapping(value = "/activate", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<?> activateAccount(@RequestParam("token") String confirmationToken) {
        return customerServiceImpl.activateAccount(confirmationToken);
    }

    @PostMapping("/resend/activate/{email}")
    public ResponseEntity<?> resendActivationMail(@PathVariable String email) {
        return customerServiceImpl.resendActivationMail(email);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> customerProfile() {
        return customerServiceImpl.viewProfile();
    }

    @RequestMapping(value = "/profile/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerProfile(@Valid @RequestBody CustomerProfileDto customerDto) {
        return customerServiceImpl.updateUserProfile(customerDto);
    }

    @RequestMapping(value = "/password/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerPassword(@Valid @RequestBody ChangePasswordDto passwordDto) {
        return customerServiceImpl.updatePassword(passwordDto);
    }

    @PostMapping("/add/address")
    public ResponseEntity<?> addAddress(@Valid @RequestBody AddressDto addressDto) {
        return customerServiceImpl.createAddress(addressDto);
    }

    @GetMapping("/view/addresses")
    private ResponseEntity<?> viewAddresses(){
        return customerServiceImpl.getAllAddresses();
    }
    @RequestMapping(value = "/update/address/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateAddress(@Valid @PathVariable("id") Long id, @RequestBody AddressDto addressDto) {
        return customerServiceImpl.updateAddress(id, addressDto);
    }

    @DeleteMapping(value = "/delete/address/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable("id") Long id) {
        return customerServiceImpl.deleteAddress(id);
    }

    @GetMapping("/view/product/{id}")
    ResponseEntity<?> viewProductCustomer(@PathVariable Long id) {
        return productService.viewProductByCustomerById(id);
    }

    @GetMapping("/view/product/all/{categoryId}")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@PathVariable Long categoryId,
                                                                     @RequestParam(required = false) Integer max,
                                                                     @RequestParam(required = false) Integer offset,
                                                                     @RequestParam(required = false) String sort,
                                                                     @RequestParam(required = false) String order) {
        return productService.getAllProductsByCategoryByCustomer(categoryId, max, offset, sort, order);
    }

    @GetMapping("/view/product/similar/{id}")
    ResponseEntity<?> viewSimilarProduct(@PathVariable Long id) {
        return productService.getSimilarProductsByCustomerById(id);
    }


    @GetMapping("/view/category/all")
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(required = false) Long categoryId) {
        return categoryService.getCategoriesCustomer(categoryId);
    }
}
