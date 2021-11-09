package io.happyharbor.cardmarket.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @JsonProperty("idGame")
    Integer gameId;
    @ToString.Exclude
    String image;
    String enName;
    @ToString.Exclude
    String locName;
    String expansion;
    @JsonProperty("nr")
    String collectorsNumber;
    @ToString.Exclude
    Integer expIcon;
    String rarity;
}
