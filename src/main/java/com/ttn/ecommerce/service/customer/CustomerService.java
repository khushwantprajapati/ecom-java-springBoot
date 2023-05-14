package com.ttn.ecommerce.service.customer;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.dto.customer.CustomerDto;
import com.ttn.ecommerce.dto.customer.CustomerProfileDto;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<String> createAccount(CustomerDto customerDto);

    void sendEmail(String email, String token);

    ResponseEntity<String> activateAccount(String activeToken);

    ResponseEntity<String> resendActivationMail(String email);

    ResponseEntity<?> viewProfile();

    ResponseEntity<?> updateUserProfile(CustomerProfileDto customerProfileDto);

    ResponseEntity<?> updatePassword(PasswordDto passwordDto);

    ResponseEntity<?> createAddress(AddressDto addressDto);

    ResponseEntity<?> updateAddress(Long id, AddressDto addressDto);

    ResponseEntity<?> deleteAddress(Long id);
}
