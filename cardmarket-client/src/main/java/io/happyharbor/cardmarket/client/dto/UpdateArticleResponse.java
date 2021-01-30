package io.happyharbor.cardmarket.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.happyharbor.cardmarket.api.dto.MyArticle;
import io.happyharbor.cardmarket.api.dto.NotUpdatedArticle;
import lombok.*;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateArticleResponse {
    List<MyArticle> updatedArticles;
    List<NotUpdatedArticle> notUpdatedArticles;
}
