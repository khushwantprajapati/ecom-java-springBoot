package com.ttn.ecommerce.contoller;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.dto.seller.SellerDto;
import com.ttn.ecommerce.dto.seller.SellerProfileDto;
import com.ttn.ecommerce.service.seller.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerService sellerServiceImpl;

    @PostMapping("/create")
    public ResponseEntity<?> saveSeller(@Valid @RequestBody SellerDto sellerDto) {
        return sellerServiceImpl.saveSeller(sellerDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> customerProfile() {
        return sellerServiceImpl.viewProfile();
    }

    @RequestMapping(value = "/profile/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerProfile(@RequestBody SellerProfileDto sellerProfileDto) {
        return sellerServiceImpl.updateUserProfile(sellerProfileDto);
    }

    @RequestMapping(value = "/password/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerPassword(@RequestBody PasswordDto passwordDto) {
        return sellerServiceImpl.updatePassword(passwordDto);
    }

    @RequestMapping(value = "/update/address", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateAddress(@RequestBody AddressDto addressDto) {
        return sellerServiceImpl.updateAddress(addressDto);
    }
}
