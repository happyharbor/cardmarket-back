package io.happyharbor.cardmarket.login.core.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "security.jwt.token")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class JwtTokenProperties {
    private final String secretKey;
    /**
     * expire token length in seconds
     */
    private final Long expireLength;
}
