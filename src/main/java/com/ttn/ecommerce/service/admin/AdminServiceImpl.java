package com.ttn.ecommerce.service.admin;

import com.ttn.ecommerce.dto.customer.CustomerResponseDto;
import com.ttn.ecommerce.dto.seller.SellerResponseDto;
import com.ttn.ecommerce.exception.GenericException;
import com.ttn.ecommerce.model.Customer;
import com.ttn.ecommerce.model.Seller;
import com.ttn.ecommerce.model.Users;
import com.ttn.ecommerce.repository.CustomerRepository;
import com.ttn.ecommerce.repository.SellerRepository;
import com.ttn.ecommerce.repository.UserRepository;
import com.ttn.ecommerce.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Override
    public ResponseEntity<List<CustomerResponseDto>> findAllCustomer(int pageOffset, int pageSize, String sortBy) {
        if (pageOffset < 0) {
            throw new GenericException("", HttpStatus.BAD_REQUEST);
        }
        if (pageSize <= 0) {
            throw new GenericException("", HttpStatus.BAD_REQUEST);
        }
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }

        PageRequest page = PageRequest.of(pageOffset, pageSize, Sort.Direction.ASC, sortBy);
        Page<Customer> customersPage = customerRepository.findAll(page);

        List<CustomerResponseDto> customerResponseDtoList = new ArrayList<>();

        customersPage.forEach(customer -> {
            CustomerResponseDto customerResponseDto = new CustomerResponseDto();
            customerResponseDto.setId(customer.getId());
            customerResponseDto.setEmail(customer.getEmail());
            customerResponseDto.setFirstName(customer.getFirstName());
            customerResponseDto.setLastName(customer.getLastName());
            customerResponseDto.setIsActive(customer.getIsActive());
            customerResponseDtoList.add(customerResponseDto);
        });

        return new ResponseEntity<>(customerResponseDtoList, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<List<SellerResponseDto>> findAllSeller(int pageOffset, int pageSize, String sortBy) {
        if (pageOffset < 0) {
            throw new GenericException("", HttpStatus.BAD_REQUEST);
        }
        if (pageSize <= 0) {
            throw new GenericException("", HttpStatus.BAD_REQUEST);
        }
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }
        PageRequest page = PageRequest.of(pageOffset, pageSize, Sort.Direction.ASC, sortBy);
        Page<Seller> sellersPage = sellerRepository.findAll(page);
        List<SellerResponseDto> sellerResponseDtoList = new ArrayList<>();
        sellersPage.forEach(seller -> {
            SellerResponseDto sellerResponseDto = new SellerResponseDto();
            sellerResponseDto.setId(seller.getId());
            sellerResponseDto.setEmail(seller.getEmail());
            sellerResponseDto.setFirstName(seller.getFirstName());
            sellerResponseDto.setLastName(seller.getLastName());
            sellerResponseDto.setIsActive(seller.getIsActive());
            sellerResponseDto.setCompanyName(seller.getCompanyName());
            sellerResponseDtoList.add(sellerResponseDto);
        });

        return new ResponseEntity<>(sellerResponseDtoList, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> activateUser(Long id) {
        Optional<Users> users = userRepository.findById(id);
        if (users.isPresent()) {
            if (users.get().getIsActive().equals(Boolean.TRUE)) {
                throw new GenericException("USer is already activated", HttpStatus.BAD_REQUEST);
            } else {
                users.get().setIsActive(Boolean.TRUE);
                userRepository.save(users.get());
                sendActivateEmail(users.get().getEmail());
                return ResponseEntity.ok("User has been activated.");
            }
        } else {
            throw new GenericException("User not found", HttpStatus.NOT_FOUND);
        }
    }


    public void sendActivateEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Account Activated");
        message.setText("User has been activated.");
        emailSenderService.sendEmail(message);
    }

    @Override
    public ResponseEntity<String> deactivateUser(Long id) {
        Optional<Users> users = userRepository.findById(id);
        if (users.isPresent()) {
            if (users.get().getIsActive().equals(Boolean.FALSE)) {
                throw new GenericException("User is already activated", HttpStatus.BAD_REQUEST);
            } else {
                users.get().setIsActive(Boolean.FALSE);
                userRepository.save(users.get());
                sendDeactivateEmail(users.get().getEmail());
                return ResponseEntity.ok("User has been deactivated.");
            }
        } else {
            throw new GenericException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    public void sendDeactivateEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Account Activated");
        message.setText("User has been deactivated.");
        emailSenderService.sendEmail(message);
    }


}






