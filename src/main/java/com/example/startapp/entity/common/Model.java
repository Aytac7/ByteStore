package com.example.startapp.entity.common;

import com.example.startapp.entity.common.Brand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "models")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    Brand brand;


}
