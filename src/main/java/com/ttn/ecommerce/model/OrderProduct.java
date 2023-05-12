package com.ttn.ecommerce.model;

import jakarta.persistence.*;

@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Orders order;

    @OneToOne
    @JoinColumn(name = "productVariationId")
    private ProductVariation productVariation;

    @OneToOne(mappedBy = "orderProduct", cascade = CascadeType.ALL)
    private OrderStatus orderStatus;
}
