package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmailIgnoreCase(String email);

    Boolean existsByEmail(String email);

    Optional<Customer> findById(Long id);


}
