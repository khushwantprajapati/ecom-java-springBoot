package com.ttn.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String jwt;

    @Column(nullable = false)
    private Boolean valid = true;

    @Column(nullable = false)
    private Boolean isActive = true;
}


