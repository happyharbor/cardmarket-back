package io.happyharbor.cardmarket.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class NotAddedArticle {
    @JsonAlias("idProduct")
    Long productId;
    @JsonAlias("idLanguage")
    int languageId;
    Object comments;
    Long count;
    BigDecimal price;
    String condition;
    Boolean isFoil;
    Boolean isSigned;
    Object isAltered;
    Boolean isPlayset;
    Object isFirstEd;
}
