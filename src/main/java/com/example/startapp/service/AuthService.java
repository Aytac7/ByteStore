package com.example.startapp.service;

import com.example.startapp.dto.MailBody;
import com.example.startapp.enums.UserRole;
import com.example.startapp.entity.User;

import com.example.startapp.exception.PasswordInvalidException;
import com.example.startapp.exception.UserEmailExistsException;
import com.example.startapp.exception.UserNotFoundException;
import com.example.startapp.repository.UserRepository;
import com.example.startapp.dto.response.AuthResponse;
import com.example.startapp.dto.request.LoginRequest;
import com.example.startapp.dto.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthResponse register(RegisterRequest registerRequest) {
        if(!registerRequest.getPassword().equals(registerRequest.getRepeatPassword())){
            throw new PasswordInvalidException(HttpStatus.BAD_REQUEST.name(), "Passwords do not match!");
        }

        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new UserEmailExistsException(HttpStatus.BAD_REQUEST.name(), "Email Exists");
        }

        String verificationToken = UUID.randomUUID().toString();

        var user = User.builder()
                .name(registerRequest.getName())
                .surname(registerRequest.getSurname())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(UserRole.USER)
                .emailVerified(false)
                .verificationToken(verificationToken)
                .build();

        User savedUser = userRepository.save(user);

        String verificationUrl = "http://localhost:8080/api/auth/verify?token="+ verificationToken;
        MailBody mailBody=new MailBody(savedUser.getEmail(),"Email Verification",
                "Please click the link to verify your email: " + verificationUrl);

        emailService.sendSimpleMessage(mailBody);

        var accessToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                        )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(),"User not found!"));

        if (!user.isEmailVerified()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email not verified!");
        }

        if(!user.getPassword().equals(loginRequest.getPassword())){
            throw new UserNotFoundException(HttpStatus.NOT_FOUND.name(),"Invalid username or password");
        }


        var accessToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(loginRequest.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
    }
}