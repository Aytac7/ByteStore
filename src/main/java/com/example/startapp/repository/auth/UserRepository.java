package com.example.startapp.repository.auth;

import com.example.startapp.entity.auth.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findUserByEmail(String email);
    
    @Transactional
    @Modifying
    @Query("update User u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);


    @Modifying
    @Transactional
    @Query("update User u set u.failedAttempt = ?1 WHERE u.email = ?2")
    void updateFailedAttempts(int failAttempts, String email);

    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = ?1, u.lockTime = ?2 WHERE u.email = ?3")
    void updateLockStatus(boolean accountNonLocked, Date lockTime, String email);

    @Query("SELECT u FROM User u WHERE u.username = :username")
     User getUserByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmailAndEnabled(String email, Boolean enabled);
}