package io.happyharbor.cardmarket.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import lombok.*;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncreaseQuantityArticleResponse {
    @JsonProperty("article")
    List<MyArticle> increasedArticles;
    @JsonProperty("failed")
    List<MyArticle> notChangedArticles;
}
