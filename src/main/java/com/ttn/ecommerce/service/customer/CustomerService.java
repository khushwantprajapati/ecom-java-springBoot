package com.ttn.ecommerce.service.customer;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.dto.customer.CustomerDto;
import com.ttn.ecommerce.dto.customer.CustomerProfileDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<String> createAccount(CustomerDto customerDto);

    void sendEmail(String email, String token);

    ResponseEntity<String> activateAccount(String activeToken);

    ResponseEntity<String> resendActivationMail(String email);

    ResponseEntity<?> viewProfile(HttpServletRequest request);

    ResponseEntity<?> updateUserProfile(CustomerProfileDto customerProfileDto, HttpServletRequest request);

    ResponseEntity<?> updatePassword(String accessToken, PasswordDto passwordDto);

    ResponseEntity<?> createAddress(AddressDto addressDto, String accessToken);

    ResponseEntity<?> updateAddress(Long id, AddressDto addressDto, String accessToken);

    ResponseEntity<?> deleteAddress(Long id, String accessToken);
}
