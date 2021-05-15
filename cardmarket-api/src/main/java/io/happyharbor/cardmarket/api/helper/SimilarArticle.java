package io.happyharbor.cardmarket.api.helper;

import io.happyharbor.cardmarket.api.dto.stock.Language;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import io.happyharbor.cardmarket.api.dto.stock.OtherUserArticle;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
public class SimilarArticle {

    public SimilarArticle(final OtherUserArticle article) {
        this.productId = article.getProductId() ;
        this.language = article.getLanguage() ;
        this.currencyId = article.getCurrencyId() ;
        this.isFoil = article.getIsFoil() ;
        this.isSigned = article.getIsSigned() ;
        this.isAltered = article.getIsAltered() ;
        this.isPlayset = article.getIsPlayset() ;
        this.isFirstEd = article.getIsFirstEd() ;
    }

    public SimilarArticle(final MyArticle article) {
        this.productId = article.getProductId() ;
        this.language = article.getLanguage() ;
        this.currencyId = article.getCurrencyId() ;
        this.isFoil = article.getIsFoil() ;
        this.isSigned = article.getIsSigned() ;
        this.isAltered = article.getIsAltered() ;
        this.isPlayset = article.getIsPlayset() ;
        this.isFirstEd = article.getIsFirstEd() ;
    }

    Long productId;
    Language language;
    Integer currencyId;
    Boolean isFoil;
    Boolean isSigned;
    Boolean isAltered;
    Boolean isPlayset;
    Boolean isFirstEd;
}
