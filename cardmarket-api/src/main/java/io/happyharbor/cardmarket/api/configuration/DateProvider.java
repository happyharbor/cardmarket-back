package io.happyharbor.cardmarket.api.configuration;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class DateProvider {
    public LocalDateTime provideDateTime() {
        return LocalDateTime.now(ZoneId.of("UTC"));
    }
}
