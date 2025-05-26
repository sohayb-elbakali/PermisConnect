package com.autoecole.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow all origins for development
        config.addAllowedOrigin("http://localhost:3000"); // React default port
        config.addAllowedOrigin("http://localhost:4200"); // Angular default port
        config.addAllowedOrigin("http://localhost:8082");
        config.addAllowedOrigin("http://localhost:8081"); // React Native port
        config.addAllowedOrigin("http://localhost:8080"); // Same server for Swagger UI


        // Allow all headers
        config.addAllowedHeader("*");
        
        // Allow credentials
        config.setAllowCredentials(true);
        
        // Expose the Authorization header
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}