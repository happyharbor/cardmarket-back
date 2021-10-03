package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetailed {
    @JsonProperty("idProduct")
    Integer id;
    @JsonProperty("idMetaproduct")
    Integer metaproductId;
    Integer countReprints;
    String enName;
    String locName;
    @JsonProperty("localization")
    List<Localization> localizations;
    String website;
    String image;
    String gameName;
    String categoryName;
    @JsonProperty("idGame")
    Integer gameId;
    String number;
    String rarity;
    Expansion expansion;
    PriceGuide priceGuide;
    Integer countArticles;
    Integer countFoils;
}
