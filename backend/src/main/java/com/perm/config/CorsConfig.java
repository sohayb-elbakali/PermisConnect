package com.perm.config;

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

        // Specify allowed origins explicitly since we're enabling credentials
        config.addAllowedOrigin("http://localhost:3000"); // React web app
        config.addAllowedOrigin("http://localhost:8082"); // Frontend running on port 8082
        config.addAllowedOrigin("http://localhost:19006"); // Expo web
        config.addAllowedOrigin("http://localhost:19000"); // Expo development
        config.addAllowedOrigin("http://localhost:19001"); // Expo development alt
        config.addAllowedOrigin("http://localhost:19002"); // Expo devtools
        config.addAllowedOrigin("exp://localhost:19000"); // Expo mobile

        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
