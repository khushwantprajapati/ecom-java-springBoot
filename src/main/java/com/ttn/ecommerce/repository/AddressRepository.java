package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.Address;
import com.ttn.ecommerce.model.Customer;
import com.ttn.ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findAddressByCustomerAndId(Customer customer, long id);

    Address findAddressBySeller(Seller seller);


}
