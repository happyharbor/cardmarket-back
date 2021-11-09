package io.happyharbor.cardmarket.client.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
@ConfigurationPropertiesScan("io.happyharbor.cardmarket.client.property")
public class ClientConfiguration {
    @Bean
    public HttpClient client() {
        return HttpClient.newBuilder()
                .build();
    }

    @Bean
    public XmlMapper mapper() {
        XmlMapper mapper = new XmlMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }
}
