package com.example.startapp.entity.auth;

import com.example.startapp.entity.common.Ad;
import com.example.startapp.entity.common.Favorite;
import com.example.startapp.entity.common.Image;
import com.example.startapp.enums.PhonePrefix;
import com.example.startapp.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    private String name;

    private String surname;

    private String username;

    @Column(unique = true)
    @Email(message = "Please enter email in proper format!")
    private String email;


    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{7,}$",
            message = "Password must be at least 7 characters long, contain at least one uppercase letter, one lowercase letter, and one number.")
    private String password;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;


    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean enabled;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @Column(name = "failed_attempt")
    private int failedAttempt;

    @Column(name = "lock_time")
    private Date lockTime;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    private boolean emailVerified;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Ad> ads;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    List<Favorite> favorites;

    @Enumerated(EnumType.STRING)
    PhonePrefix phonePrefix;

    String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_photo_id")
    private Image profilePhoto;

    public void setProfilePhoto(Image profilePhoto) {
        this.profilePhoto = profilePhoto;
        if (profilePhoto != null) {
            profilePhoto.setUser(this);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }


    public int getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(int failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if (lockTime == null) {
            return true;  // Account is not locked if lockTime is null
        }

        long lockTimeInMillis = lockTime.getTime();
        long currentTimeInMillis = System.currentTimeMillis();
        long lockDurationInMillis = 60 * 1000;  // 1 minute

        return lockTimeInMillis + lockDurationInMillis < currentTimeInMillis;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}