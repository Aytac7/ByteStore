package com.example.startapp.controller;


import com.example.startapp.dto.request.LoginRequest;
import com.example.startapp.dto.request.UserOtpRequest;
import com.example.startapp.service.AuthService;
import com.example.startapp.service.RefreshTokenService;
import com.example.startapp.dto.response.AuthResponse;
import com.example.startapp.dto.request.RefreshTokenRequest;
import com.example.startapp.dto.request.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;


    public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;

    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register( @Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
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
    public ResponseEntity<AuthResponse> login( @Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse authResponse = refreshTokenService.refreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }
}