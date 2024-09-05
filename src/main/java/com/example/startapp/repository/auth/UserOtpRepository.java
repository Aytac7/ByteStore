package com.example.startapp.repository.auth;

import com.example.startapp.entity.auth.User;
import com.example.startapp.entity.auth.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {
    UserOtp findByUser(User user);

    @Query("select otp from UserOtp otp where otp.otp = ?1 and otp.user = ?2")

    Optional<UserOtp> findByOtpAndUser(Integer otp, User user);
}