package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetailed {
    @JsonAlias("idProduct")
    Integer id;
    @JsonAlias("idMetaproduct")
    Integer metaproductId;
    Integer countReprints;
    String enName;
    String locName;
    @JsonAlias("localization")
    List<Localization> localizations;
    String website;
    String image;
    String gameName;
    String categoryName;
    @JsonAlias("idGame")
    Integer gameId;
    String number;
    String rarity;
    ProductDetailedExpansion expansion;
    PriceGuide priceGuide;
    Integer countArticles;
    Integer countFoils;
}
