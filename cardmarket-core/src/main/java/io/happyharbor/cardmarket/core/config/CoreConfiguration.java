package io.happyharbor.cardmarket.core.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConfigurationPropertiesScan("io.happyharbor.cardmarket.core.property")
@EnableScheduling
public class CoreConfiguration {
}
