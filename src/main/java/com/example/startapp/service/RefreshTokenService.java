package com.example.startapp.service;

import com.example.startapp.dto.response.AuthResponse;
import com.example.startapp.entity.RefreshToken;
import com.example.startapp.entity.User;
import com.example.startapp.exception.RefreshTokenExpiredException;
import com.example.startapp.exception.RefreshTokenNotFoundException;
import com.example.startapp.exception.UserNotFoundException;
import com.example.startapp.repository.RefreshTokenRepository;
import com.example.startapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final RefreshTokenRepository refreshTokenRepository;



    public RefreshToken createRefreshToken(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException(HttpStatus.NOT_FOUND.name(),"User not found with email : " + username));

        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null) {
            long refreshTokenValidity = 30 * 1000;
            refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();

            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken) {
        RefreshToken refToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException(HttpStatus.NOT_FOUND.name(),"Refresh token not found!"));

        if (refToken.getExpirationTime().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refToken);
            throw new RefreshTokenExpiredException(HttpStatus.UNAUTHORIZED.name(),"Refresh Token expired");
        }

        return refToken;
    }

    public AuthResponse refreshToken(String refreshToken) {
        RefreshToken refToken = verifyRefreshToken(refreshToken);
        User user = refToken.getUser();

        String accessToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refToken.getRefreshToken())
                .build();
    }
}