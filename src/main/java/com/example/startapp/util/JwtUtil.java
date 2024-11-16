package com.example.startapp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public String generateToken(String username, String email) {
        return Jwts.builder()
                .setSubject(username)
                .claim("tokenType", "password_reset")
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(secretKey)
                .compact();
    }


    public boolean validatePasswordResetToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            String tokenType = claims.get("tokenType", String.class);
            if (tokenType == null || !"password_reset".equals(tokenType)) {
                throw new IllegalArgumentException("Invalid token type.");
            }


            Date expirationDate = claims.getExpiration();
            if (expirationDate.before(new Date())) {
                throw new IllegalArgumentException("Token has expired.");
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("email", String.class);
    }



}
