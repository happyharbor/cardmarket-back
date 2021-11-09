package io.happyharbor.cardmarket.client.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "client.oauth")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class OauthProperties {
    private final String signature;
    private final String version;
}
