package com.ttn.ecommerce.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductReviewId implements Serializable {

    private Long customer;
    private Long product;

    public ProductReviewId() {

    }

    public ProductReviewId(Long customer, Long product) {
        this.customer = customer;
        this.product = product;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductReviewId that = (ProductReviewId) o;
        return Objects.equals(customer, that.customer) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, product);
    }
}
