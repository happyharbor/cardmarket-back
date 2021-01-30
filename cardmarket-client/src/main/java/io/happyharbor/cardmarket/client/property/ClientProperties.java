package io.happyharbor.cardmarket.client.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.net.URL;

@ConfigurationProperties(prefix = "client")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class ClientProperties {
    private final URL host;
}
