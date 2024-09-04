package com.example.nbiot.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    // Define the maximum age for the CORS configuration (48 hours in seconds)
    private static final long MAX_AGE = 48 * 60 * 60;

    @Bean
    public CorsFilter corsFilter() {
        // Create a source for the CORS configuration
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Create a new CORS configuration object
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 1. Allow all origins for cross-origin requests
        corsConfiguration.addAllowedOrigin("*");

        // 2. Allow all headers in cross-origin requests
        corsConfiguration.addAllowedHeader("*");

        // 3. Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        corsConfiguration.addAllowedMethod("*");

        // Set the maximum age for preflight requests
        corsConfiguration.setMaxAge(MAX_AGE);

        // 4. Apply the CORS configuration to all endpoints (/**)
        source.registerCorsConfiguration("/**", corsConfiguration);

        // Return the CorsFilter bean with the defined configuration
        return new CorsFilter(source);
    }
}


