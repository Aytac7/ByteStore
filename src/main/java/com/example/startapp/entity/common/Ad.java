package com.example.startapp.entity.common;

import com.example.startapp.entity.auth.User;
import com.example.startapp.enums.AdStatus;
import com.example.startapp.enums.PhonePrefix;
import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ads")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)

    double price;

    @Column(nullable = false)
    String header;

    @Column(nullable = false)
    @Size(max = 700)
    String additionalInfo;

    @Column(nullable = false)
    Boolean isNew;

    @Size(max = 100)
    String rejectionReason;

    LocalDateTime statusChangedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AdStatus status;


    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    @Size(max = 10, message = "You can upload a maximum of 10 images.")
    private List<Image> images;


    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    List<Favorite> favorites;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    PhonePrefix phonePrefix;

    String phoneNumber;

    @Column(nullable = false)
    String city;

    @Transient
    boolean isFavorite;
}


