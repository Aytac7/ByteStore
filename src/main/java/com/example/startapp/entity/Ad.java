package com.example.startapp.entity;

import com.example.startapp.enums.AdStatus;
import com.example.startapp.enums.PhonePrefix;
import jakarta.mail.Multipart;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.ImageInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    Long price;

    @Column(nullable = false)
    String header;

    @Column(nullable = false)
    @Size(max = 700)
    String additionalInfo;

    @Column(nullable = false)
    Boolean isNew;

    @ManyToOne
    @JoinColumn
    User user;

    @ManyToOne
    @JoinColumn
    Category category;

    @ManyToOne
    @JoinColumn(nullable = false)
    Brand brand;

    @ManyToOne
    @JoinColumn(nullable = false)
    Model model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    AdStatus status;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    @Size(max = 10, message = "You can upload a maximum of 10 images.")
    List<Image> images ;


    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    List<Favorite> favorites;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    PhonePrefix phonePrefix;

    String phoneNumber;
}


