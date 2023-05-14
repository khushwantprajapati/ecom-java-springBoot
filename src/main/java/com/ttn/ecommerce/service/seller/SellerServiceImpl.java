package com.ttn.ecommerce.service.seller;

import com.ttn.ecommerce.dto.AddressDto;
import com.ttn.ecommerce.dto.PasswordDto;
import com.ttn.ecommerce.dto.seller.SellerDto;
import com.ttn.ecommerce.dto.seller.SellerProfileDto;
import com.ttn.ecommerce.enums.Authority;
import com.ttn.ecommerce.exception.GenericException;
import com.ttn.ecommerce.model.Address;
import com.ttn.ecommerce.model.Role;
import com.ttn.ecommerce.model.Seller;
import com.ttn.ecommerce.repository.AddressRepository;
import com.ttn.ecommerce.repository.RoleRepository;
import com.ttn.ecommerce.repository.SellerRepository;
import com.ttn.ecommerce.repository.TokenRepository;
import com.ttn.ecommerce.service.EmailSenderService;
import com.ttn.ecommerce.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    AddressRepository addressRepository;


    public ResponseEntity<String> saveSeller(SellerDto sellerDto) {
        if (!sellerDto.getPassword().equals(sellerDto.getConfirmPassword())) {
            throw new GenericException("Password and confirm password don't match.", HttpStatus.BAD_REQUEST);
        }

        if (sellerRepository.existsByEmail(sellerDto.getEmail())) {
            throw new GenericException("Email already exists.", HttpStatus.BAD_REQUEST);
        }

        Seller seller = new Seller();
        seller.setEmail(sellerDto.getEmail());
        seller.setFirstName(sellerDto.getFirstName());
        seller.setLastName(sellerDto.getLastName());
        seller.setPassword(passwordEncoder.encode(sellerDto.getPassword()));
        seller.setGst(sellerDto.getGst());
        seller.setCompanyName(sellerDto.getCompanyName());
        seller.setCompanyContact(sellerDto.getCompanyContact());

        Address address = new Address();
        address.setAddressLine(sellerDto.getAddressLine());
        address.setCity(sellerDto.getCity());
        address.setCountry(sellerDto.getCountry());
        address.setState(sellerDto.getState());
        address.setLabel(sellerDto.getLabel());
        address.setZipCode(sellerDto.getZipCode());
        address.setSeller(seller);
        seller.setAddress(address);

        Role role = roleRepository.findByAuthority(Authority.SELLER);
        seller.setRole(List.of(role));


        sellerRepository.save(seller);
        addressRepository.save(address);


        return ResponseEntity.status(HttpStatus.OK).body("Thank you for registering as a seller!");
    }


    @Override
    public ResponseEntity<?> viewProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        SellerProfileDto sellerProfileDto = new SellerProfileDto();

        sellerProfileDto.setId(seller.getId());
        sellerProfileDto.setFirstName(seller.getFirstName());
        sellerProfileDto.setLastName(seller.getLastName());
        sellerProfileDto.setIsActive(seller.getIsActive());
        sellerProfileDto.setCompanyName(seller.getCompanyName());
        sellerProfileDto.setCompanyContact(seller.getCompanyContact());
        sellerProfileDto.setGst(seller.getGst());

        Address address = addressRepository.findAddressBySeller(seller);

        sellerProfileDto.setCity(address.getCity());
        sellerProfileDto.setCountry(address.getCountry());
        sellerProfileDto.setLabel(address.getLabel());
        sellerProfileDto.setState(address.getState());
        sellerProfileDto.setAddressLine(address.getAddressLine());
        sellerProfileDto.setZipCode(address.getZipCode());

        return ResponseEntity.status(HttpStatus.OK).body(sellerProfileDto);
    }

    @Override
    public ResponseEntity<?> updateUserProfile(SellerProfileDto sellerProfileDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Seller seller = sellerRepository.findByEmailIgnoreCase(email);

        if (seller == null) {
            throw new GenericException("Customer not found", HttpStatus.NOT_FOUND);
        }

        if (sellerProfileDto.getFirstName() != null) {
            seller.setFirstName(sellerProfileDto.getFirstName());
        }
        if (sellerProfileDto.getLastName() != null) {
            seller.setLastName(sellerProfileDto.getLastName());
        }
        if (sellerProfileDto.getCompanyName() != null) {
            seller.setCompanyName(sellerProfileDto.getCompanyName());
        }
        if (sellerProfileDto.getCompanyContact() != null) {
            seller.setCompanyContact(sellerProfileDto.getCompanyContact());
        }
        if (sellerProfileDto.getGst() != null) {
            seller.setGst(sellerProfileDto.getGst());
        }

        sellerRepository.save(seller);
        return ResponseEntity.status(HttpStatus.OK).body("Seller updated");
    }

    @Override
    public ResponseEntity<?> updatePassword(PasswordDto passwordDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);
        if (seller == null) {
            throw new GenericException("Customer not found", HttpStatus.UNAUTHORIZED);
        }

        if (!passwordDto.getPassword().equals(passwordDto.getConfirmPassword())) {
            throw new GenericException("Password and confirm password do not match.", HttpStatus.BAD_REQUEST);
        }

        seller.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        sellerRepository.save(seller);
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
    }


    @Override
    public ResponseEntity<?> updateAddress(AddressDto addressDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepository.findByEmailIgnoreCase(email);

        if (seller == null) {
            throw new GenericException("Customer not found", HttpStatus.UNAUTHORIZED);
        }

        Address address = addressRepository.findAddressBySeller(seller);
        if (address == null) {
            throw new GenericException("Address not found", HttpStatus.NOT_FOUND);

        }
        address.setAddressLine(addressDto.getAddressLine());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setCountry(addressDto.getCountry());
        address.setZipCode(addressDto.getZipCode());

        Address savedAddress = addressRepository.save(address);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }


}
