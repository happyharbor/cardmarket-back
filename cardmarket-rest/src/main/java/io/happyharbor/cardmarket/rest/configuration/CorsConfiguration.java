package io.happyharbor.cardmarket.rest.configuration;

import io.happyharbor.cardmarket.rest.property.SecurityProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class CorsConfiguration {

    private final SecurityProperties securityProperties;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(securityProperties.getAllowedOrigins())
                        .allowedMethods("GET", "HEAD", "POST", "PUT");
            }
        };
    }
}
