package io.happyharbor.cardmarket.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Product {
    @JsonAlias("idGame")
    Integer gameId;
    @ToString.Exclude
    String image;
    String enName;
    @ToString.Exclude
    String locName;
    String expansion;
    @JsonAlias("nr")
    String collectorsNumber;
    @ToString.Exclude
    Integer expIcon;
    String rarity;
}
