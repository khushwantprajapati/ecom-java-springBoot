package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.dto.customer.CustomerDto;
import com.ttn.ecommerce.dto.customer.CustomerProfileDto;
import com.ttn.ecommerce.service.customer.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> customerProfile(HttpServletRequest request) {
        return customerServiceImpl.viewProfile(request);
    }

    @RequestMapping(value = "/profile/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerProfile(@RequestBody CustomerProfileDto customerProfileDto, HttpServletRequest request) {
        return customerServiceImpl.updateUserProfile(customerProfileDto, request);
    }

    @RequestMapping(value = "/password/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerPassword(@RequestBody PasswordDto passwordDto, @RequestParam("token") String accessToken) {
        return customerServiceImpl.updatePassword(accessToken, passwordDto);
    }

    @PostMapping("/add/address")
    public ResponseEntity<?> addAddress(@RequestBody AddressDto addressDto, @RequestParam("token") String accessToken) {
        return customerServiceImpl.createAddress(addressDto, accessToken);
    }

    @RequestMapping(value = "/update/address/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateAddress(@PathVariable("id") Long id, @RequestBody AddressDto addressDto, @RequestParam String token) {
        return customerServiceImpl.updateAddress(id, addressDto, token);
    }

    @DeleteMapping(value = "/delete/address/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable("id") Long id, @RequestParam String token) {
        return customerServiceImpl.deleteAddress(id, token);
    }

}
