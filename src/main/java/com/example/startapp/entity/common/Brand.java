package com.example.startapp.entity.common;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "brands")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "categories_brands",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Model> models;


    //    @ManyToMany(mappedBy = "brands")
//    List<Category> categories;
//
//    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
//    List<Model> models;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brand brand = (Brand) o;
        return Objects.equals(id, brand.id) && Objects.equals(name, brand.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
