package com.ttn.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private String zipCode;
    private String label;

    @ManyToOne
    @JsonIgnore
    private Customer customer;

    @OneToOne
    @JsonIgnore
    private Seller seller;


}
