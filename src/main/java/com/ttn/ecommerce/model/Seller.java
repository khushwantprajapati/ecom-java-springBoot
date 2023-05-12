package com.ttn.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@PrimaryKeyJoinColumn(name = "userId")
public class Seller extends Users {

    @Column(nullable = false)
    private String gst;

    @Column(nullable = false)
    private String companyContact;

    @Column(nullable = false)
    private String companyName;


    @OneToOne(cascade = CascadeType.ALL)
    private Address address;
}
