package io.happyharbor.cardmarket.client.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class AddArticlesRequest {
    @JsonProperty("idProduct")
    Long productId;
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

    public static AddArticlesRequest of(MyArticle article) {
        return AddArticlesRequest.builder()
                .productId(article.getProductId())
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
