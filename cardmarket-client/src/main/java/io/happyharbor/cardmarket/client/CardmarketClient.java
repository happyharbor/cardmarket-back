package io.happyharbor.cardmarket.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.happyharbor.cardmarket.client.property.ClientProperties;
import io.happyharbor.cardmarket.client.property.CredentialProperties;
import io.happyharbor.cardmarket.client.property.OauthProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardmarketClient {

    private final ClientProperties clientProperties;
    private final CredentialProperties credentialProperties;
    private final OauthProperties oauthProperties;
    private final HttpClient client;
    private final XmlMapper xmlMapper = xmlMapper();
    private final Random random;

    public <T> CompletableFuture<T> sendGetRequest(final Map<String, String> queryMap, final String endpoint, final TypeReference<T> typeReference) {
        HttpRequest request = generateGetRequest(queryMap, endpoint);
        return sendRequest(request, typeReference);
    }

    public <T> CompletableFuture<T> sendGetRequest(final String endpoint, final TypeReference<T> typeReference) {
        return sendGetRequest(Collections.emptyMap(), endpoint, typeReference);
    }

    public <T, U> CompletableFuture<T> sendPutRequest(final Map<String, String> queryMap,
                                                                             final String endpoint,
                                                                             final TypeReference<T> typeReference,
                                                                             final U payload) {
        HttpRequest request = generatePutRequest(queryMap, endpoint, payload);

        return sendRequest(request, typeReference);
    }

    public <T, U> CompletableFuture<T> sendPutRequest(final String endpoint, final TypeReference<T> typeReference,
                                                      final U payload) {
        return sendPutRequest(Collections.emptyMap(), endpoint, typeReference, payload);
    }

    public <T, U> CompletableFuture<T> sendPostRequest(final Map<String, String> queryMap,
                                                      final String endpoint,
                                                      final TypeReference<T> typeReference,
                                                      final U payload) {
        HttpRequest request = generatePostRequest(queryMap, endpoint, payload);

        return sendRequest(request, typeReference);
    }

    public <T, U> CompletableFuture<T> sendPostRequest(final String endpoint, final TypeReference<T> typeReference,
                                                      final U payload) {
        return sendPostRequest(Collections.emptyMap(), endpoint, typeReference, payload);
    }

    public <T, U> CompletableFuture<T> sendDeleteRequest(final Map<String, String> queryMap,
                                                       final String endpoint,
                                                       final TypeReference<T> typeReference,
                                                       final U payload) {
        HttpRequest request = generateDeleteRequest(queryMap, endpoint, payload);

        return sendRequest(request, typeReference);
    }

    public <T, U> CompletableFuture<T> sendDeleteRequest(final String endpoint, final TypeReference<T> typeReference,
                                                       final U payload) {
        return sendDeleteRequest(Collections.emptyMap(), endpoint, typeReference, payload);
    }

    private XmlMapper xmlMapper() {
        val mapper = new XmlMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }

    private HttpRequest generateGetRequest(final Map<String, String> queryMap, final String endpoint) {

        final Pair<String, String> urlAuthHeader = generateHeader(queryMap, endpoint, "GET");

        return httpBuilder(urlAuthHeader)
                .GET()
                .build();
    }

    @SneakyThrows
    private <T> HttpRequest generatePutRequest(final Map<String, String> queryMap, final String endpoint, final T payload) {

        final Pair<String, String> urlAuthHeader = generateHeader(queryMap, endpoint, "PUT");

        val xml = xmlMapper.writeValueAsString(payload);

        return httpBuilder(urlAuthHeader)
                .PUT(HttpRequest.BodyPublishers.ofString(xml))
                .build();
    }

    @SneakyThrows
    private <T> HttpRequest generatePostRequest(final Map<String, String> queryMap, final String endpoint, final T payload) {

        final Pair<String, String> urlAuthHeader = generateHeader(queryMap, endpoint, "POST");

        val xml = xmlMapper.writeValueAsString(payload);

        return httpBuilder(urlAuthHeader)
                .POST(HttpRequest.BodyPublishers.ofString(xml))
                .build();
    }

    @SneakyThrows
    private <T> HttpRequest generateDeleteRequest(final Map<String, String> queryMap, final String endpoint, final T payload) {

        final Pair<String, String> urlAuthHeader = generateHeader(queryMap, endpoint, "DELETE");

        val xml = xmlMapper.writeValueAsString(payload);

        return httpBuilder(urlAuthHeader)
                .method("DELETE", HttpRequest.BodyPublishers.ofString(xml))
                .build();
    }

    private HttpRequest.Builder httpBuilder(final Pair<String, String> urlAuthHeader) {
        return HttpRequest.newBuilder()
                .uri(URI.create(urlAuthHeader.getLeft()))
                .timeout(Duration.ofMinutes(1))
                .headers("Authorization", "OAuth " + urlAuthHeader.getRight());
    }

    @SneakyThrows
    private Pair<String, String> generateHeader(final Map<String, String> queryMap, final String endpoint, String method) {
        Map<String, String> headers = new TreeMap<>(queryMap);

        String url = clientProperties.getHost() + endpoint;
        headers.put("oauth_consumer_key", credentialProperties.getAppToken());
        headers.put("oauth_token", credentialProperties.getAccessToken());
        headers.put("oauth_nonce", Double.toString(random.nextDouble()));
        headers.put("oauth_timestamp", Long.toString(System.currentTimeMillis()));
        headers.put("oauth_signature_method", oauthProperties.getSignature());
        headers.put("oauth_version", oauthProperties.getVersion());

        val sb = new StringBuilder();
        sb.append(method)
                .append("&")
                .append(URLEncoder.encode(url, UTF_8))
                .append("&");

        final String headersStr = headers.entrySet()
                .stream()
                .map(e -> URLEncoder.encode(e.getKey(), UTF_8) + "=" + URLEncoder.encode(e.getValue(), UTF_8))
                .collect(Collectors.joining("&"));
        sb.append(URLEncoder.encode(headersStr, UTF_8));

        headers.put("realm", url);

        String signingKey = URLEncoder.encode(credentialProperties.getAppSecret(), UTF_8) +
                "&" +
                URLEncoder.encode(credentialProperties.getAccessTokenSecret(), UTF_8);

        val mac = Mac.getInstance("HmacSHA1");
        val secretKeySpec = new SecretKeySpec(signingKey.getBytes(UTF_8), "HmacSHA1");
        mac.init(secretKeySpec);
        final byte[] bytes = mac.doFinal(sb.toString().getBytes(UTF_8));
        val signature = Base64.getEncoder().encodeToString(bytes);
        headers.put("oauth_signature", signature);

        final String authSignature = headers.entrySet()
                .stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"")
                .collect(Collectors.joining(", "));

        final String query = queryMap.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
        url = StringUtils.isEmpty(query) ? url : url + "?" + query;

        return new ImmutablePair<>(url, authSignature);
    }

    private <T> CompletableFuture<T> sendRequest(HttpRequest request, TypeReference<T> typeReference) {
        return client.sendAsync(request, new JsonBodyHandler<>(typeReference))
                .thenApply((HttpResponse<Supplier<T>> supplierHttpResponse) -> {
                    if (supplierHttpResponse.statusCode() >= 200 && supplierHttpResponse.statusCode() < 300) {
                        return supplierHttpResponse.body().get();
                    }
                    log.warn("Request {} has failed with error: {}", request, supplierHttpResponse.statusCode());
                    return null;
                });
    }
}
