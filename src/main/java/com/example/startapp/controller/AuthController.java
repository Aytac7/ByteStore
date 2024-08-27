package com.example.startapp.controller;


import com.example.startapp.dto.request.LoginRequest;
import com.example.startapp.service.auth.AuthService;
import com.example.startapp.service.auth.RefreshTokenService;
import com.example.startapp.dto.response.AuthResponse;
import com.example.startapp.dto.request.RefreshTokenRequest;
import com.example.startapp.dto.request.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid RegisterRequest request) {
        logger.debug("Register request received: {}", request);
        AuthResponse response = authService.register(request);
        logger.debug("Register response: {}", response);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/verify-email/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        authService.verifyEmail(email);
        return ResponseEntity.ok("verified email,  otp code sent");
    }

    @PostMapping("/confirm-register/{otp}/{email}")
    public ResponseEntity<String> confirmRegistration(@PathVariable String email,
                                                      @PathVariable Integer otp) {
        authService.confirmRegistration(email, otp);
        return ResponseEntity.ok("Registration confirmed!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse authResponse = refreshTokenService.refreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }

}