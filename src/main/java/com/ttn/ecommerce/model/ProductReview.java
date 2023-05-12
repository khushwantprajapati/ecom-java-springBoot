package com.ttn.ecommerce.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

@Entity
public class ProductReview {

    @EmbeddedId
    private ProductReviewId productReviewId;
    @ManyToOne
    @MapsId("customer")
    private Customer customer;

    private String review;
    private Integer rating;
    @ManyToOne
    @MapsId("product")
    private Product product;
}
