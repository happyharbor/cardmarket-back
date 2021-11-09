package io.happyharbor.cardmarket.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.happyharbor.cardmarket.api.dto.MyArticle;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateArticlesRequest {
    @JsonProperty("idArticle")
    Long id;
    @JsonProperty("idLanguage")
    Integer languageId;
    String comments;
    Long count;

    BigDecimal price;
    String condition;
    Boolean isFoil;
    Boolean isSigned;
    Boolean isAltered;
    Boolean isPlayset;
    Boolean isFirstEd;

    public static UpdateArticlesRequest of(MyArticle article) {
        return UpdateArticlesRequest.builder()
                .id(article.getId())
                .languageId(article.getLanguage().getLanguageId())
                .comments(article.getComments())
                .count(article.getCount())
                .price(article.getPrice())
                .condition(article.getCondition())
                .isFoil(article.getIsFoil())
                .isSigned(article.getIsSigned())
                .isPlayset(article.getIsPlayset())
                .isFirstEd(article.getIsFirstEd())
                .build();
    }
}
