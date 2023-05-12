package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Seller findByEmailIgnoreCase(String email);

    Boolean existsByEmail(String email);


    Optional<Seller> findById(Long id);
}
