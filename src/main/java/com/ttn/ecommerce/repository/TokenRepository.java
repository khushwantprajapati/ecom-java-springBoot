package com.ttn.ecommerce.repository;

import com.ttn.ecommerce.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByJwt(String jwt);

    Token findByEmail(String email);

    Token findByEmailAndJwt(String email, String jwt);
}
