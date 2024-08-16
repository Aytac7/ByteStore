package com.example.startapp.repository;

import com.example.startapp.entity.ForgotPassword;
import com.example.startapp.entity.User;
import com.example.startapp.entity.UserOtp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {
    UserOtp findByUser(User user);

    @Query("select otp from UserOtp otp where otp.otp = ?1 and otp.user = ?2")

    Optional<UserOtp> findByOtpAndUser(Integer otp, User user);
}