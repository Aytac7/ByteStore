package com.example.startapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "products")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    Brand brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<Image> images;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    List<Favorite> favorites;
}
