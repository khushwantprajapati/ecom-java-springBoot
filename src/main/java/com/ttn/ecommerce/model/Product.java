package com.ttn.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    private Boolean isCancellable = false;
    private Boolean isReturnable = false;
    private String Brand;
    private Boolean isActive = false;
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "seller_user_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductVariation> productVariation;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductReview> productReviews;


}
