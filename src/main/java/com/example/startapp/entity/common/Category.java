package com.example.startapp.entity.common;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "categories")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @ManyToMany
    @JoinTable(name = "categories_brands",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "brand_id"))
    List<Brand> brands;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<Subcategory> subCategories;


    @OneToMany(mappedBy ="category",cascade = CascadeType.ALL )
    List<Ad> ads;

}
