package io.happyharbor.cardmarket.client.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.util.Random;

@Configuration
@ConfigurationPropertiesScan("io.happyharbor.cardmarket.client.property")
public class ClientConfiguration {
    @Bean
    public HttpClient client() {
        return HttpClient.newBuilder()
                .build();
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
