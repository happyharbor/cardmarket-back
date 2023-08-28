package io.happyharbor.cardmarket.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OtherUserArticle {
    @JsonAlias("idArticle")
    Long id;
    @JsonAlias("idProduct")
    Long productId;
    Language language;
    String comments;
    BigDecimal price;
    @JsonAlias("idCurrency")
    Integer currencyId;
    String currencyCode;
    Long count;
    boolean inShoppingCart;
    List<Price> prices;
    String condition;
    Boolean isFoil;
    Boolean isSigned;
    Boolean isAltered;
    Boolean isPlayset;
    Boolean isFirstEd;
}
