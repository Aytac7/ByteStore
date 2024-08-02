package com.example.startapp.repository.usermanagement;

import com.example.startapp.entity.usermanagement.ForgotPassword;
import com.example.startapp.entity.usermanagement.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    ForgotPassword findByUser(User user);

    @Query("select fp from ForgotPassword fp where fp.otp = ?1 and fp.user = ?2")

    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
}