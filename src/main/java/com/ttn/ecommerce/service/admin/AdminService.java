package com.ttn.ecommerce.service.admin;

import com.ttn.ecommerce.dto.customer.CustomerResponseDto;
import com.ttn.ecommerce.dto.seller.SellerResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {


    ResponseEntity<List<CustomerResponseDto>> findAllCustomer(int pageOffset, int pageSize, String sortBy);

    ResponseEntity<List<SellerResponseDto>> findAllSeller(int pageOffset, int pageSize, String sortBy);


    ResponseEntity<?> activateUser(Long id);

    ResponseEntity<?> deactivateUser(Long id);


}
