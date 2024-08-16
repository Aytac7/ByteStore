package com.example.startapp.repository;

import com.example.startapp.entity.ForgotPassword;
import com.example.startapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Long> {

    ForgotPassword findByUser(User user);

    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")

    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
}