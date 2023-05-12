package com.ttn.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private Integer quantityAvailable;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false, columnDefinition = "JSON")
    private String metadata;
    @Column(nullable = false)
    private String primaryImageName;
    private Boolean isActive = true;
    @OneToOne(mappedBy = "productVariation", cascade = CascadeType.ALL)
    private OrderProduct orderProduct;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @OneToMany(mappedBy = "productVariation", cascade = CascadeType.ALL)
    private Set<Cart> cart;


}
