package io.happyharbor.cardmarket.core.csv;

import com.opencsv.bean.CsvBindByName;
import io.happyharbor.cardmarket.api.dto.stock.Language;
import io.happyharbor.cardmarket.api.helper.GroupedArticle;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class CsvArticle {
    @CsvBindByName
    Long productId;
    @CsvBindByName
    Integer languageId;
    @CsvBindByName
    String languageName;
    @CsvBindByName
    Integer currencyId;
    @CsvBindByName
    Boolean isFoil;
    @CsvBindByName
    Boolean isSigned;
    @CsvBindByName
    Boolean isAltered;
    @CsvBindByName
    Boolean isPlayset;
    @CsvBindByName
    Boolean isFirstEd;
    @CsvBindByName
    BigDecimal price;

    public GroupedArticle toGroupedArticle() {
        return GroupedArticle.builder()
                .productId(this.productId)
                .language(Language.builder()
                        .languageId(this.languageId)
                        .languageName(this.languageName)
                        .build())
                .currencyId(this.currencyId)
                .isFoil(this.isFoil)
                .isSigned(this.isSigned)
                .isAltered(this.isAltered)
                .isPlayset(this.isPlayset)
                .isFirstEd(this.isFirstEd)
                .build();
    }
}
