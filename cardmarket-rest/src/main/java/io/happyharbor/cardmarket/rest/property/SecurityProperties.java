package io.happyharbor.cardmarket.rest.property;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "security")
@ConstructorBinding
@Value
public class SecurityProperties {
    String[] allowedOrigins;
}
