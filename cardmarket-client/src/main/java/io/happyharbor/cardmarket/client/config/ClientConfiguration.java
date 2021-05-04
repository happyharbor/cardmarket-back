package io.happyharbor.cardmarket.client.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
@ConfigurationPropertiesScan("io.happyharbor.cardmarket.client.property")
public class ClientConfiguration {
    @Bean
    public HttpClient client() {
        return HttpClient.newBuilder()
                .build();
    }
}
