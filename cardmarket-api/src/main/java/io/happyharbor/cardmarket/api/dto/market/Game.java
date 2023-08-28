package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {
    @JsonProperty("idGame")
    Integer gameId;
    String name;
    String abbreviation;
}
