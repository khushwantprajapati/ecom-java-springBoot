package com.ttn.ecommerce.service.seller;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.dto.seller.SellerDto;
import com.ttn.ecommerce.dto.seller.SellerProfileDto;
import org.springframework.http.ResponseEntity;

public interface SellerService {
    ResponseEntity<String> saveSeller(SellerDto sellerDto);

    ResponseEntity<?> viewProfile();

    ResponseEntity<?> updateUserProfile(SellerProfileDto sellerProfileDto);

    ResponseEntity<?> updatePassword(PasswordDto passwordDto);

    ResponseEntity<?> updateAddress(AddressDto addressDto);

}
