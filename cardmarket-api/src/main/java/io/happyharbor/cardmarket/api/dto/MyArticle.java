package io.happyharbor.cardmarket.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyArticle {
    @JsonProperty("idArticle")
    Long id;
    @JsonProperty("idProduct")
    Long productId;
    Language language;
    String comments;
    BigDecimal price;
    @JsonProperty("idCurrency")
    Integer currencyId;
    String currencyCode;
    Long count;
    boolean inShoppingCart;
    Product product;
    String condition;
    Boolean isFoil;
    Boolean isSigned;
    Boolean isAltered;
    Boolean isPlayset;
    Boolean isFirstEd;
}
