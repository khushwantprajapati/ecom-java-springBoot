package com.ttn.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JsonIgnoreProperties(value = {"childCategories"})
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"parentCategory"})
    private List<Category> childCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Product> products;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {"category", "categoryMetadataField"})
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValues;


}
