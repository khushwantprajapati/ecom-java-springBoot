package com.ttn.ecommerce.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class Cart {

    @EmbeddedId
    private CartId cartId;

    @ManyToOne
    @MapsId("customer")
    private Customer customer;
    private Integer quantity;
    private Boolean isWishlistItem;

    @ManyToOne
    @MapsId("productVariation")
    private ProductVariation productVariation;
}
