package com.example.startapp.service.auth;


import com.example.startapp.dto.response.auth.MailBody;
import com.example.startapp.emailService.EmailService;
import com.example.startapp.entity.auth.ForgotPassword;
import com.example.startapp.entity.auth.User;
import com.example.startapp.exception.InvalidOtpException;
import com.example.startapp.repository.auth.ForgotPasswordRepository;
import com.example.startapp.repository.auth.UserRepository;
import com.example.startapp.dto.response.auth.ChangePassword;
import com.example.startapp.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.startapp.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;


import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ForgotPasswordService(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository, PasswordEncoder passwordEncoder,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil=jwtUtil;
    }


    public ResponseEntity<String> verifyEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "Please provide an valid email!" + email));

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request : " + otp)
                .subject("OTP for Forgot Password request")
                .build();

        ForgotPassword fp = forgotPasswordRepository.findByUser(user);

        if (fp != null) {
            fp.setOtp(otp);
            fp.setExpirationTime(new Date(System.currentTimeMillis() + 3 * 60 * 1000));
        } else {
            fp = ForgotPassword.builder()
                    .otp(otp)
                    .expirationTime(new Date(System.currentTimeMillis() + 3 * 60 * 1000))
                    .user(user)
                    .build();
        }
        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email sent for verification!");
    }


    public ResponseEntity<String> verifyOtp(Integer otp, String email) throws IOException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(), "Please provide an valid email!"));

        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
                .orElseThrow(() -> new InvalidOtpException(HttpStatus.BAD_REQUEST.name(), "Invalid OTP for email: " + email));

        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getEmail());


        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 200);
        response.put("message", "OTP verified");
        response.put("token", token);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(response);
        return ResponseEntity.ok(jsonResponse);




    }


    public ResponseEntity<String> changePasswordHandler(ChangePassword changePassword,
                                                       String token) {

        String email = jwtUtil.getEmailFromToken(token);  // Tokeni analiz et və emaili al
        if (email == null || !jwtUtil.validatePasswordResetToken(token)) {
            // Token etibarsızdırsa, səhv mesajı göndəririk
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return new ResponseEntity<>("Please enter the password again!, password and repeated password are not the same", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Password has been changed!");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(1_000, 10_000);
    }

}