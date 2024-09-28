package com.example.startapp.config;

import com.example.startapp.enums.UserRole;
import com.example.startapp.repository.auth.UserRepository;
import com.example.startapp.service.auth.AuthFilterService;
import com.example.startapp.service.auth.JwtService;
import com.example.startapp.service.auth.OAuth2LoginSuccessHandler;
import com.example.startapp.service.auth.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserRepository userRepository;
    private final AuthFilterService authFilterService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**", "/forgotPassword/**", "/oauth2/**", "/login", "/register/**", "/ads/**", "/s3/upload", "/admin/**", "/favorites/**", "/categories/**", "/user/**").permitAll()
//                        .requestMatchers(permitSwagger).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//                )
//                .formLogin(AbstractHttpConfigurer::disable)
////                .oauth2Login(oauth2 -> oauth2
////                        .loginPage("/login")
////                        .defaultSuccessUrl("/home", true)
////                        .successHandler(oAuth2LoginSuccessHandler())
////                )
//                .addFilterBefore(authFilterService, SessionManagementFilter.class);
//
//        return http.build();
//
//    }
//    public static String[] permitSwagger = {
//            "swagger-ui/**",
//            "/v3/api-docs/**",
//            "/swagger-resources/**",
//            "/swagger-ui.html",
//            "/webjars/**"
//    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAll).permitAll()
                        .requestMatchers(ADMIN).hasAuthority(UserRole.ADMIN.name())
                        .requestMatchers(USER).hasAuthority(UserRole.USER.name()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getOutputStream().println("{ \"error\": \"Unauthorized\" }");
                        })
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .defaultSuccessUrl("/swagger-ui.html", true)
                        .successHandler(oAuth2LoginSuccessHandler())
                )
                .addFilterBefore(authFilterService, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler(userRepository, jwtService, refreshTokenService);
    }

    public static String[] permitAll = {
            "/oauth2/**",
            "/login",
            "/auth/**",
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/*",
            "/swagger-ui/**",
            "/security/v1/api-docs/**",
            "swagger/**",

            "/auth/register",
            "/auth/confirm-register/{otp}/{email}",
            "/auth/login",
            "/auth/refresh",

            "/forgotPassword/verifyMail/{email}",
            "/forgotPassword/verifyOtp/{otp}/{email}",
            "/forgotPassword/changePassword/{email}",

            "/feedbacks"


    };
    public static String[] ADMIN = {

    };
    public static String[] USER = {


    };

}