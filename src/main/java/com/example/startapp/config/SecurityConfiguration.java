package com.example.startapp.config;

import com.example.startapp.enums.UserRole;
import com.example.startapp.repository.auth.UserRepository;
import com.example.startapp.service.auth.AuthFilterService;
import com.example.startapp.service.auth.JwtService;
import com.example.startapp.service.auth.OAuth2LoginSuccessHandler;
import com.example.startapp.service.auth.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.SessionManagementFilter;


//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfiguration {
//
//    private final UserRepository userRepository;
//    private final AuthFilterService authFilterService;
//
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
//    @Bean
//    public AuthenticationSuccessHandler oAuth2LoginSuccessHandler() {
//        return new OAuth2LoginSuccessHandler(userRepository);
//    }
//}

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthFilterService authFilterService;
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

            "/v1/api/auth/register",
            "/v1/api/auth/verify-email/{email}",
            "/v1/api/auth/confirm-register/{otp}/{email}",
            "/v1/api/auth/login",
            "/v1/api/auth/refresh",

            "/forgotPassword/verifyMail/{email}",
            "/forgotPassword/verifyOtp/{otp}/{email}",
            "/forgotPassword/changePassword/{email}",

            "/v1/api/activity-categories",

            "/v1/api/activities",
            "/v1/api/activities/find/{id}",

            "/v1/api/home-hotels/find/{id}",
            "/v1/api/home-hotels",

            "/v1/api/notifications/find/{id}",
            "/v1/api/notifications",

            "/v1/api/regions",
            "/v1/api/regions/find/{id}",

            "/v1/api/restaurant",
            "/v1/api/restaurant/find/{id}",

            "/v1/api/room",
            "/v1/api/room/find/{id}",

            "/v1/api/roomReservations/reservation{id}",
            "/v1/api/roomReservations/reservationHistory{id}",

            "/v1/api/scenicSpot",
            "/v1/api/scenicSpot/find/{id}",

            "/v1/api/zone"
    };
    public static String[] ADMIN = {
            "/v1/api/activity-categories/create",
            "/v1/api/activity-categories/update/{id}",
            "/v1/api/activity-categories/delete/{id}",

            "/v1/api/activities/create",
            "/v1/api/activities/update/{id}",
            "/v1/api/activities/delete/{id}",

            "/v1/api/home-hotels/create",
            "/v1/api/home-hotels/update/{id}",
            "/v1/api/home-hotels/delete/{id}",

            "/v1/api/notifications/update/{id}",
            "/v1/api/notifications/delete/{id}",
            "/v1/api/notifications/create",

            "/v1/api/images/addPhoto",
            "/v1/api/images/delete/{id}",

            "/v1/api/regions/create",
            "/v1/api/regions/delete/{id}",

            "/v1/api/restaurant/create",
            "/v1/api/restaurant/update/{id}",
            "/v1/api/restaurant/delete/{id}",

            "/v1/api/room/create",
            "/v1/api/room/update/{id}",
            "/v1/api/room/delete/{id}",

            "/v1/api/scenicSpot/create",
            "/v1/api/scenicSpot/approve/{id}",
            "/v1/api/scenicSpot/delete/{id}",

            "/v1/api/zone/create",
            "/v1/api/zone/update/{id}",
            "/v1/api/zone/delete/{id}"
    };
    public static String[] USER = {

            "/v1/api/activities/like/{id}",
            "/v1/api/activities/averageRating/{id}",

            "/v1/api/home-hotels/rate/{id}",
            "/v1/api/home-hotels/like/{id}",

            "/v1/api/restaurant/like/{id}",
            "/v1/api/restaurant/rate/{id}",

            "/v1/api/review/upload",
            "/v1/api/review/home-hotel",
            "/v1/api/review/scenic-spot",
            "/v1/api/review/restaurant",

            "/v1/api/roomReservations/create",

            "/v1/api/scenicSpot/like/{id}",
            "/v1/api/scenicSpot/rate/{id}"
    };

}