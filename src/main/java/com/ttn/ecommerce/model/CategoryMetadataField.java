package com.ttn.ecommerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CategoryMetadataField {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "categoryMetadataField", cascade = CascadeType.ALL)
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValues;


}
