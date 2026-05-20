package com.talentprobe.assessment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

                                        // Swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                                        // Auth
                        .requestMatchers("/auth/login").permitAll()
                                        // Candidate registration
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                                        // Candidate assessment flow (no token needed)
                        .requestMatchers(HttpMethod.GET, "/api/assignments/validate").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/attempt/start").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/attempt/*/submit").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/attempt/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/attempt/candidate/*").permitAll()
                                        // Candidate submissions (no token needed)
                        .requestMatchers(HttpMethod.POST, "/submissions").permitAll()
                        .requestMatchers(HttpMethod.GET, "/submissions/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/submissions/attempt/*").permitAll()
                                        // Everything else requires authentication
                        .anyRequest().hasRole("ADMIN")

                );
        return http.build();
    }
}