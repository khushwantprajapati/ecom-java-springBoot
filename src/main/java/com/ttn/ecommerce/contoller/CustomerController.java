package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.dto.customer.CustomerDto;
import com.ttn.ecommerce.dto.customer.CustomerProfileDto;
import com.ttn.ecommerce.service.customer.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerServiceImpl;

    @PostMapping("/create")
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
    public ResponseEntity<?> updateCustomerProfile(@RequestBody CustomerProfileDto customerProfileDto) {
        return customerServiceImpl.updateUserProfile(customerProfileDto);
    }

    @RequestMapping(value = "/password/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerPassword(@RequestBody PasswordDto passwordDto) {
        return customerServiceImpl.updatePassword(passwordDto);
    }

    @PostMapping("/add/address")
    public ResponseEntity<?> addAddress(@RequestBody AddressDto addressDto) {
        return customerServiceImpl.createAddress(addressDto);
    }

    @RequestMapping(value = "/update/address/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateAddress(@PathVariable("id") Long id, @RequestBody AddressDto addressDto) {
        return customerServiceImpl.updateAddress(id, addressDto);
    }

    @DeleteMapping(value = "/delete/address/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable("id") Long id) {
        return customerServiceImpl.deleteAddress(id);
    }

}
