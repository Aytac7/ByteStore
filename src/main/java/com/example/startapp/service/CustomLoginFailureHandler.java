package com.example.startapp.service;

import com.example.startapp.entity.User;
import com.example.startapp.exception.UserNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private AuthService authService;

    @Autowired
    public void setAuthService(@Lazy AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");

        try {
            User user = authService.getByEmail(email);

            if (user != null) {
                if (user.isEnabled() && user.isAccountNonLocked()) {
                    if (user.getFailedAttempt() < AuthService.MAX_FAILED_ATTEMPTS) {
                        authService.handleFailedAttempts(user);
                    } else {
                        authService.lock(user);
                        exception = new LockedException("Your account has been locked due to " + AuthService.MAX_FAILED_ATTEMPTS + " failed attempts. It will be unlocked after 24 hours.");
                    }
                } else if (!user.isAccountNonLocked()) {
                    if (authService.unlockWhenTimeExpired(user)) {
                        exception = new LockedException("Your account has been unlocked. Please try to login again.");
                    }
                }
            }
        } catch (UserNotFoundException e) {
            // Handle user not found case if necessary
        }

        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }
}
