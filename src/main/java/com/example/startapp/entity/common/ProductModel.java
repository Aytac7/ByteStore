package com.example.startapp.entity.common;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Brand brand;

    @OneToMany(mappedBy = "productModel")
    private Set<Product> products;

    @OneToMany(mappedBy = "productModel")
    private List<AdEntity> ads;

    // Getters and setters
}
