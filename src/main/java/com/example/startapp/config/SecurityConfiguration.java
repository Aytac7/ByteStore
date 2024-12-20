//package com.example.startapp.config;
//
//import com.example.startapp.repository.auth.UserRepository;
//import com.example.startapp.service.auth.AuthFilterService;
//import com.example.startapp.service.auth.JwtService;
//import com.example.startapp.service.auth.OAuth2LoginSuccessHandler;
//import com.example.startapp.service.auth.RefreshTokenService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.security.web.session.SessionManagementFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfiguration {
//
//    private final UserRepository userRepository;
//    private final AuthFilterService authFilterService;
//    private final JwtService jwtService;
//    private final RefreshTokenService refreshTokenService;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/ads/**", "/s3/upload", "/favorites/**").hasAuthority("USER")
//                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
//                        .requestMatchers(permitSwagger)
//                        .permitAll()
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .formLogin(AbstractHttpConfigurer::disable)
//                .addFilterBefore(authFilterService, SessionManagementFilter.class);
//
//        return http.build();
//    }
//
//    public static String[] permitSwagger = {
//            "/swagger-ui/**",
//            "/v3/api-docs/**",
//            "/swagger-resources/**",
//            "/swagger-ui.html",
//            "/webjars/**"
//    };
//
//    @Bean
//    public AuthenticationSuccessHandler oAuth2LoginSuccessHandler() {
//        return new OAuth2LoginSuccessHandler(userRepository, jwtService, refreshTokenService);
//    }
//}


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
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.core.convert.converter.Converter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserRepository userRepository;
    private final AuthFilterService authFilterService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

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

            "/ads/{adId}",
            "/ads/all",
            "/ads/new",
            "/ads/secondhand",
            "/ads/filter",
            "/ads/search/suggestions",
            "/ads/model/{modelId}",
            "/categories/**",
            "/feedbacks/add"


    };
    public static String[] ADMIN = {
            "/admin/**",
            "/ads/**"


    };
    public static String[] USER = {
            "/ads/create",
            "/ads/update/{adId}",
            "/ads/myAds",
            "/ads/delete/{adId}",
            "/favorites/toggle/{adId}",
            "/favorites/myFavs",
            "/user/update",
            "/user/delete",
            "/user/info",
            "/ads/secondhand",

    };

}