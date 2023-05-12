package com.ttn.ecommerce.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartId implements Serializable {

    private Long customer;
    private Long productVariation;

    public CartId(Long customer, Long productVariation) {
        this.customer = customer;
        this.productVariation = productVariation;
    }

    public CartId() {

    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Long getProductVariation() {
        return productVariation;
    }

    public void setProductVariation(Long productVariation) {
        this.productVariation = productVariation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartId cartId = (CartId) o;
        return Objects.equals(customer, cartId.customer) && Objects.equals(productVariation, cartId.productVariation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, productVariation);
    }
}
