package com.example.startapp.service.auth;


import com.example.startapp.dto.response.auth.UserDTO;
import com.example.startapp.entity.auth.User;
import com.example.startapp.repository.auth.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final UserRepository userRepository;

    private static final String SECRET_KEY = "BF7FD11ACE545745B7BA1AF98B6F156D127BC7BB544BAB6A4FD74E4FC7";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }




//    public Boolean extractEnabled(String token) {
//        return extractClaim(token, claims -> claims.get("enabled", Boolean.class));
//    }



    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails) {
        User userEntity = (User) userDetails;
        extraClaims.put("userId", userEntity.getUserId());
        extraClaims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());

//        extraClaims.put("enabled", userEntity.getUserId());

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername().isEmpty() ? "user" : userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(30))))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public boolean isTokenValid(String token) {
        return (!isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public User getCurrentUserFromToken() {
        String token = getTokenFromSecurityContext();

        if (token == null) {
            throw new IllegalStateException("Token not found");
        }
        Long userId = extractClaim(token, claims -> claims.get("userId", Long.class));
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
    }

    public String getTokenFromSecurityContext() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
        if (authentication.getCredentials() instanceof String) {
            return (String) authentication.getCredentials();
        } else {
            throw new IllegalStateException("No JWT token found in SecurityContext");
        }
    }
    throw new IllegalStateException("Authentication not found in SecurityContext");
}

}