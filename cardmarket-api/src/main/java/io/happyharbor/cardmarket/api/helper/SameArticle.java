package io.happyharbor.cardmarket.api.helper;

import io.happyharbor.cardmarket.api.dto.stock.Language;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
public class SameArticle {

    public SameArticle(final MyArticle article) {
        this.productId = article.getProductId();
        this.language = article.getLanguage();
        this.currencyId = article.getCurrencyId();
        this.isFoil = article.getIsFoil();
        this.count = article.getCount();
        this.price = article.getPrice();
        this.isSigned = article.getIsSigned();
        this.isAltered = article.getIsAltered();
        this.isPlayset = article.getIsPlayset();
        this.isFirstEd = article.getIsFirstEd();
    }

    Long productId;
    Language language;
    Integer currencyId;
    Long count;
    BigDecimal price;
    Boolean isFoil;
    Boolean isSigned;
    Boolean isAltered;
    Boolean isPlayset;
    Boolean isFirstEd;
}
