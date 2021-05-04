package io.happyharbor.cardmarket.login.core.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "io.happyharbor.cardmarket.login.core.repository")
@EntityScan(basePackages = "io.happyharbor.cardmarket.login.core.entity")
@ConfigurationPropertiesScan("io.happyharbor.cardmarket.login.core.property")
public class LoginCoreConfiguration {
}
