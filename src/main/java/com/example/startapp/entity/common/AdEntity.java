package com.example.startapp.entity.common;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ads")
public class AdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_model_id", nullable = false)
    private ProductModel productModel;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(length = 700)
    private String description;

    @ElementCollection
    @CollectionTable(name = "ad_images", joinColumns = @JoinColumn(name = "ad_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdCondition condition;

    @Column(nullable = false)
    private boolean validated;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


enum AdCondition {
    NEW,
    USED
}
