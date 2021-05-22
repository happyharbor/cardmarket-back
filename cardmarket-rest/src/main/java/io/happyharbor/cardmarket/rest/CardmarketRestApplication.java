package io.happyharbor.cardmarket.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("io.happyharbor.cardmarket")
@ConfigurationPropertiesScan("io.happyharbor.cardmarket.rest.property")
public class CardmarketRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardmarketRestApplication.class, args);
    }

}
