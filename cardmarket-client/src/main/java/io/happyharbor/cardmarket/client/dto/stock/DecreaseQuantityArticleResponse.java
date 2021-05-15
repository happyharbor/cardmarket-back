package io.happyharbor.cardmarket.client.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecreaseQuantityArticleResponse {
    @JsonProperty("article")
    List<MyArticle> decreasedArticles = new ArrayList<>();
}
