package com.example.startapp.dto.request.auth;

import lombok.Data;

@Data
public class RefreshTokenRequest {

    private String refreshToken;
}