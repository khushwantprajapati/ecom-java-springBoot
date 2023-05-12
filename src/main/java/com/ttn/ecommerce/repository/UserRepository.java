package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {


    Optional<Users> findByEmailIgnoreCase(String email);

    Users findByEmail(String email);


}
