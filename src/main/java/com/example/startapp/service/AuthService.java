package com.example.startapp.service;

import com.example.startapp.dto.MailBody;
import com.example.startapp.dto.request.LoginRequest;
import com.example.startapp.dto.request.RegisterRequest;
import com.example.startapp.dto.response.AuthResponse;
import com.example.startapp.emailService.EmailService;
import com.example.startapp.entity.User;
import com.example.startapp.entity.UserOtp;
import com.example.startapp.enums.PhonePrefix;
import com.example.startapp.enums.UserRole;
import com.example.startapp.exception.*;
//import com.example.startapp.mapper.UserMapper;
//import com.example.startapp.mapper.UserMapper;
import com.example.startapp.repository.UserOtpRepository;
import com.example.startapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import static java.lang.System.in;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    public static final int MAX_FAILED_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 60 * 1000;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserOtpRepository userOtpRepository;
//    private final UserMapper userMapper;
    private final EmailService emailService;

    @Transactional
    public AuthResponse register(RegisterRequest registerRequest) {

        if (!registerRequest.getPassword().equals(registerRequest.getRepeatPassword())) {
            throw new PasswordInvalidException(HttpStatus.BAD_REQUEST.name(), "Passwords do not match!");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserEmailExistsException(HttpStatus.BAD_REQUEST.name(), "Email Exists");
        }

        if (userRepository.existsByPhonePrefixAndPhoneNumber(registerRequest.getPhonePrefix(), registerRequest
                .getPhoneNumber())) {
            throw new DuplicatePhoneNumberException("A user with this phone number already exists.");
        }
        System.out.println("User Details: " + registerRequest);



        User user = User.builder()
                .email(registerRequest.getEmail())
                .name(registerRequest.getName())
                .surname(registerRequest.getSurname())
                .username(registerRequest.getUsername())
                .phonePrefix(registerRequest.getPhonePrefix())
                .phoneNumber(registerRequest.getPhoneNumber())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .emailVerified(false)
                .enabled(false)
                .accountNonLocked(true)
                .failedAttempt(0)
                .lockTime(null)
                .build();
//        User user = userMapper.mapToUser(registerRequest);
//        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
//        user.setRole(UserRole.USER);
//        user.setEmailVerified(false);
//        user.setEnabled(false);
//        user.setAccountNonLocked(true);
//        user.setFailedAttempt(0);
//        user.setLockTime(null);
        User savedUser = userRepository.save(user);

        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public ResponseEntity<String> verifyEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "Please provide an valid email!" + email));

        Integer otp = otpGenerator();

        MailBody mailBody = new MailBody(email, "OTP for Registration",
                "This is the OTP for your registration : " + otp);

        UserOtp byUser = userOtpRepository.findByUser(user);

        if (byUser != null) {

            byUser.setOtp(otp);
            byUser.setExpirationTime(new Date(System.currentTimeMillis() + 3 * 60 * 1000));
        } else {
            byUser = UserOtp.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 3 * 60 * 1000))
                    .user(user)
                    .build();
        }

        emailService.sendSimpleMessage(mailBody);
        userOtpRepository.save(byUser);
        return ResponseEntity.ok("Email sent for verification!");
    }

    public void confirmRegistration(String email, Integer otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "Email not found: " + email));

        UserOtp userOtp = userOtpRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new InvalidOtpException(HttpStatus.BAD_REQUEST.name(), "Invalid OTP for email: " + email));

        if (userOtp.getExpirationTime().before(Date.from(Instant.now()))) {
            userOtpRepository.deleteById(userOtp.getFpid());
            throw new InvalidOtpException(HttpStatus.UNAUTHORIZED.name(), "OTP has expired!");
        }

        user.setEmailVerified(true);
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "User not found!"));

        if (!user.isAccountNonLocked()) {
            if (unlockWhenTimeExpired(user)) {
                log.info("User account unlocked: {}", user.getEmail());
                throw new CustomLockedException("Your account has been unlocked. Please try to login again.");
            } else {
                log.info("User account is locked: {}", user.getEmail());
                throw new CustomLockedException("Your account is locked due to multiple failed login attempts. Try again later.");
            }
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.info("Invalid password for user: {}", user.getEmail());
            handleFailedAttempts(user);
            throw new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "Invalid username or password");
        }

        resetFailedAttempts(user.getEmail());
        log.info("User login successful: {}", user.getEmail());

        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }


    public void handleFailedAttempts(User user) {
        int newFailAttempts = user.getFailedAttempt() + 1;

        System.out.println("Current failed attempts for user: " + user.getFailedAttempt());
        System.out.println("Incrementing failed attempts to: " + newFailAttempts);

        userRepository.updateFailedAttempts(newFailAttempts, user.getEmail());

        user = userRepository.findByEmail(user.getEmail()).orElse(user);
        System.out.println("Failed attempts after update: " + user.getFailedAttempt());

        if (newFailAttempts >= MAX_FAILED_ATTEMPTS) {
            lock(user);
            System.out.println("User account locked"); // Debug log
            throw new CustomLockedException("Your account has been locked due to " + MAX_FAILED_ATTEMPTS + " failed attempts. It will be unlocked after 1 minute.");
        }
    }

    public void resetFailedAttempts(String email) {
        userRepository.updateFailedAttempts(0, email);
        System.out.println("Failed attempts reset for email: " + email); // Debug log
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
        System.out.println("Lock time set for user: " + user.getEmail()); // Debug log
    }

    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepository.save(user);
            System.out.println("User account unlocked: " + user.getEmail()); // Debug log
            return true;
        }

        return false;
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "User not found!"));
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000, 999_999);
    }
}
