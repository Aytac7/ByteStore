package com.example.startapp.repository.usermanagement;

import com.example.startapp.entity.usermanagement.User;
import com.example.startapp.entity.usermanagement.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserOtpRepository extends JpaRepository<UserOtp, Integer> {
    UserOtp findByUser(User user);

    @Query("select otp from UserOtp otp where otp.otp = ?1 and otp.user = ?2")

    Optional<UserOtp> findByOtpAndUser(Integer otp, User user);
}