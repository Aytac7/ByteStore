package com.example.startapp.service.auth;

import com.example.startapp.dto.response.auth.MailBody;
import com.example.startapp.dto.request.auth.LoginRequest;
import com.example.startapp.dto.request.auth.RegisterRequest;
import com.example.startapp.dto.response.auth.AuthResponse;
import com.example.startapp.emailService.EmailService;
import com.example.startapp.entity.auth.User;
import com.example.startapp.entity.auth.UserOtp;
import com.example.startapp.enums.UserRole;
import com.example.startapp.exception.*;
import com.example.startapp.repository.auth.UserOtpRepository;
import com.example.startapp.repository.auth.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;


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
    private final EmailService emailService;
    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public String register(RegisterRequest registerRequest) {

        if (!registerRequest.getPassword().equals(registerRequest.getRepeatPassword())) {
            throw new PasswordInvalidException(HttpStatus.BAD_REQUEST.name(), "Kod uyğunlaşmır");
        }

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserEmailExistsException(HttpStatus.BAD_REQUEST.name(), "Email mövcuddur");
        }
        System.out.println("İstifadəçi məlumatları: " + registerRequest);

        User user = User.builder()
                .email(registerRequest.getEmail())
                .name(registerRequest.getName())
                .surname(registerRequest.getSurname())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .emailVerified(false)
                .enabled(false)
                .accountNonLocked(true)
                .failedAttempt(0)
                .lockTime(null)
                .build();

        User savedUser = userRepository.save(user);

        verifyEmail(savedUser.getEmail());

        return "Otp kodu mailinizə göndərildi";
    }

    public ResponseEntity<String> verifyEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "Zəhmət olmasa düzgün email hesabı qeyd edin" + email));

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
        return ResponseEntity.ok("Təsdiqləmə üçün mail göndərildi");
    }



    public AuthResponse confirmRegistration(String email, Integer otp) {
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

        String accessToken = jwtService.generateToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user.getEmail()).getRefreshToken();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "User not found!"));

        if (!user.isEmailVerified()) {
            throw new CustomLockedException("Hesabınız təsdiqlənməyib. Zəhmət olmasa emailinizi təsdiqləyin.");
        }
        if (!user.isEnabled()) {
            throw new CustomLockedException("Hesabınız aktiv deyil.");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.info("Kod yanlışdır: {}", user.getEmail());
            handleFailedAttempts(user);
            throw new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "Yanlış kod ya ad");
        }

        resetFailedAttempts(user.getEmail());
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
        System.out.println("Failed attempts reset for email: " + email);
    }

    public void lock(User user) {
        user.setAccountNonLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
        System.out.println("Lock time set for user: " + user.getEmail());
    }

    public boolean unlockWhenTimeExpired(User user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepository.save(user);
            System.out.println("User account unlocked: " + user.getEmail());
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
        return random.nextInt(1_000, 10_000);
    }

}
