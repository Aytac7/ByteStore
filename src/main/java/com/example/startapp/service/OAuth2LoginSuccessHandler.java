package com.example.startapp.service;

import com.example.startapp.dto.request.RegisterRequest;
import com.example.startapp.entity.User;
import com.example.startapp.enums.UserRole;
import com.example.startapp.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.sendRedirect("/home");

    }
//        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
//        String email = oauth2User.getAttribute("email");
//
//        Optional<User> existingUser = userRepository.findByEmail(email);
//        if (existingUser.isEmpty()) {
//            User newUser = new User();
//            newUser.setEmail(email);
//            newUser.setName(oauth2User.getAttribute("name"));
//            newUser.setSurname(oauth2User.getAttribute("surname"));
//            newUser.setPassword("");
//            newUser.setRole(UserRole.USER);
//            newUser.setEnabled(true);
//            newUser.setAccountNonLocked(true);
//            newUser.setEmailVerified(true);
//
//            try {
//                userRepository.save(newUser);
//            } catch (Exception e) {
//                e.printStackTrace();
//                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "User registration failed");
//                return;
//            }
//        }
//
//        response.sendRedirect("/home");
//    }
}
