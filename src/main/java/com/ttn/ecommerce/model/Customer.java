package com.ttn.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;


@Entity
@PrimaryKeyJoinColumn(name = "userId")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer extends Users {


    private String contact;


}
