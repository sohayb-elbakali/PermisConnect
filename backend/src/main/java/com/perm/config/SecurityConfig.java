package com.perm.config;
import com.perm.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.perm.security.JwtTokenProvider;



@Configuration

@EnableWebSecurity

public class SecurityConfig {



    private final JwtTokenProvider jwtTokenProvider;



    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {

        this.jwtTokenProvider = jwtTokenProvider;

    }



    @Bean

    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        // Chemins Swagger
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**", "/swagger-resources/**", "/v3/api-docs/**", "/webjars/**").permitAll()
                        // For testing, allow specific endpoints without authentication (remove in production)
                        .requestMatchers(HttpMethod.POST, "/api/autoecoles/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/autoecoles/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/autoecoles/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/autoecoles/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/**").permitAll()// Added this line to permit admin creation
                        .requestMatchers(HttpMethod.GET, "/api/users/admins").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // Other security configurations...

}


