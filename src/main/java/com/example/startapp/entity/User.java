package com.example.startapp.entity;

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

@Getter
@Setter
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @NotBlank(message = "The name field can't be blank")
    private String name;

    @NotBlank(message = "The username field can't be blank")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "The email field can't be blank")
    @Column(unique = true)
    @Email(message = "Please enter email in proper format!")
    private String email;

    @NotBlank(message = "The password field can't be blank")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{7,}$",
            message = "Password must be at least 7 characters long, contain at least one uppercase letter, one lowercase letter, and one number.")
    private String password;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;


    @Enumerated(EnumType.STRING)
    private UserRole role;

    @NotBlank(message = "The phoneNumber field can't be blank")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number must be between 10 and 15 digits, optionally starting with '+'. format=+994551212333")
    private String phoneNumber;

    @NotBlank(message = "The surname field can't be blank")
    private String surname;

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