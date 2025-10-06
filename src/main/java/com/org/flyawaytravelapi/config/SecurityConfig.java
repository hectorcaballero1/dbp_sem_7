package com.org.flyawaytravelapi.config;

import com.org.flyawaytravelapi.auth.components.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (UNPROTECTED)
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/flights/create").permitAll()
                        .requestMatchers("/flights/create-many").permitAll()
                        .requestMatchers("/cleanup").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers("/flights/{id}").permitAll()
                        .requestMatchers("/flights/book/{id}").permitAll()

                        // Endpoints protegidos (PROTECTED)
                        .requestMatchers("/flights/search").authenticated()
                        .requestMatchers("/flights/book").authenticated()

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
