package com.example.startapp.service.auth;

import com.example.startapp.entity.auth.User;
import com.example.startapp.enums.UserRole;
import com.example.startapp.repository.auth.UserRepository;
import com.example.startapp.service.auth.JwtService;
import com.example.startapp.service.auth.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            User newUser = new User();
            newUser.setName(newUser.getName());
            newUser.setEmail(email);
            newUser.setRole(UserRole.USER);

            try {
                userRepository.save(newUser);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "User registration failed");
                return;
            }
        }
        User user = userRepository.findUserByEmail(email).orElseThrow();
        String refreshToken = refreshTokenService.createRefreshToken(email).getRefreshToken();
        String token = jwtService.generateToken(user);
        response.sendRedirect("/login?token=" + token + "&refreshToken=" + refreshToken);

    }
}
