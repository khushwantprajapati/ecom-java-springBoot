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
    public ResponseEntity<?> customerProfile(@RequestParam("token") String activeToken) {
        return sellerServiceImpl.viewProfile(activeToken);
    }

    @RequestMapping(value = "/profile/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerProfile(@RequestBody SellerProfileDto sellerProfileDto, @RequestParam("token") String accessToken) {
        return sellerServiceImpl.updateUserProfile(sellerProfileDto, accessToken);
    }

    @RequestMapping(value = "/password/update", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateCustomerPassword(@RequestBody PasswordDto passwordDto, @RequestParam("token") String accessToken) {
        return sellerServiceImpl.updatePassword(accessToken, passwordDto);
    }

    @RequestMapping(value = "/update/address", method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateAddress(@RequestBody AddressDto addressDto, @RequestParam String token) {
        return sellerServiceImpl.updateAddress(addressDto, token);
    }
}
