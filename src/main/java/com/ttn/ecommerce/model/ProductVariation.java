package com.ttn.ecommerce.model;

import com.ttn.ecommerce.service.ConverterService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private Long quantityAvailable;
    @Column(nullable = false)
    private Long price;
    @Convert(converter = ConverterService.class)
    private Map<String,String> metadata;

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
