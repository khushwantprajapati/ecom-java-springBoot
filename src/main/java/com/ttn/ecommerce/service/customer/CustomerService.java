package com.ttn.ecommerce.service.customer;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.ChangePasswordDto;
import com.ttn.ecommerce.dto.MessageDto;
import com.ttn.ecommerce.dto.customer.CustomerDto;
import com.ttn.ecommerce.dto.customer.CustomerProfileDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {
    ResponseEntity<MessageDto> createAccount(CustomerDto customerDto);

    void sendEmail(String email, String token);

    ResponseEntity<String> activateAccount(String activeToken);

    ResponseEntity<String> resendActivationMail(String email);

    ResponseEntity<?> viewProfile();

    ResponseEntity<?> updateUserProfile(CustomerProfileDto customerDto);

    ResponseEntity<?> updatePassword(ChangePasswordDto passwordDto);

    ResponseEntity<?> createAddress(AddressDto addressDto);

    ResponseEntity<List<AddressDto>> getAllAddresses();

    ResponseEntity<?> updateAddress(Long id, AddressDto addressDto);

    ResponseEntity<?> deleteAddress(Long id);
}
