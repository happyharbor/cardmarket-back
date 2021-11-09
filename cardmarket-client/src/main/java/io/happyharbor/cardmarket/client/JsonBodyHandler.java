package io.happyharbor.cardmarket.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class JsonBodyHandler<W> implements HttpResponse.BodyHandler<Supplier<W>> {

    private final ObjectMapper objectMapper;

    private Class<W> wClass;
    private TypeReference<W> typeReference;

    public JsonBodyHandler(Class<W> wClass) {
        this.wClass = wClass;
        objectMapper = createObjectMapper();
    }

    public JsonBodyHandler(TypeReference<W> typeReference) {
        this.typeReference = typeReference;
        objectMapper = createObjectMapper();
    }

    @Override
    public HttpResponse.BodySubscriber<Supplier<W>> apply(HttpResponse.ResponseInfo responseInfo) {
        if (wClass != null) {
            return asJSON(wClass);
        }
        return asJSON(typeReference);
    }

    private ObjectMapper createObjectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer =  new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        return JsonMapper.builder() // or different mapper for other format
                .addModule(module)
                .build();
    }

    private HttpResponse.BodySubscriber<Supplier<W>> asJSON(Class<W> targetType) {
        HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

        return HttpResponse.BodySubscribers.mapping(
                upstream,
                inputStream -> toSupplierOfType(inputStream, targetType));
    }

    private HttpResponse.BodySubscriber<Supplier<W>> asJSON(TypeReference<W> targetType) {
        HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

        return HttpResponse.BodySubscribers.mapping(
                upstream,
                inputStream -> toSupplierOfType(inputStream, targetType));
    }

    private Supplier<W> toSupplierOfType(InputStream inputStream, Class<W> targetType) {
        return () -> {
            try (InputStream stream = inputStream) {
                return objectMapper.readValue(stream, targetType);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }

    private Supplier<W> toSupplierOfType(InputStream inputStream, TypeReference<W> targetType) {
        return () -> {
            try (InputStream stream = inputStream) {
                return objectMapper.readValue(stream, targetType);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        };
    }
}
