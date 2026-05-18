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
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users","/assessments/**","/auth/**","/questions/**","/api/assignments/**","/api/attempt/**","/submissions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users","/assessments/**","/auth/**","/questions/**","/api/assignments/**","/api/attempt/**","/submissions/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/users","/assessments/**","/auth/**","/questions/**","/api/assignments/**","/api/attempt/**","/submissions/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/users","/assessments/**","/auth/**","/questions/**","/api/assignments/**","/api/attempt/**","/submissions/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}