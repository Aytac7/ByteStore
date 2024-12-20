package com.example.startapp.entity.auth;

import com.example.startapp.entity.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "user_otp")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fpid;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private User user;
}