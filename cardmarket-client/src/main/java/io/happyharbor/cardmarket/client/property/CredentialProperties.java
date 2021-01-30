package io.happyharbor.cardmarket.client.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "client.credentials")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class CredentialProperties {
    private final String appToken;
    private final String appSecret;
    private final String accessToken;
    private final String accessTokenSecret;
}
