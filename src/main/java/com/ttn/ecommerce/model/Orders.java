package com.ttn.ecommerce.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Orders {

    @Id
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private Integer amountPaid;
    @Column(nullable = false)
    private LocalDateTime dateCreated;
    @Column(nullable = false)
    private String paymentMethod;
    @Column(nullable = false)
    private String customerAddressCity;
    @Column(nullable = false)
    private String customerAddressState;
    @Column(nullable = false)
    private String customerAddressCountry;
    @Column(nullable = false)
    private String customerAddressAddressLine;
    @Column(nullable = false)
    private String customerAddressZipCode;
    private String customerAddressLabel;

    @ManyToOne
    @JoinColumn(name = "customerUserId")
    private Customer customers;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderProduct> orderProduct;

}
