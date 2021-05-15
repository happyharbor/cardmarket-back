package io.happyharbor.cardmarket.client.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteArticlesRequest {
    @JsonProperty("idArticle")
    Long id;
    Long count;

    public static DeleteArticlesRequest of(MyArticle article) {
        return DeleteArticlesRequest.builder()
                .id(article.getId())
                .count(article.getCount())
                .build();
    }
}
