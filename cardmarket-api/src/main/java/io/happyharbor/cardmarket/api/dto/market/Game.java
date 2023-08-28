package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Game {
    @JsonAlias("idGame")
    Integer gameId;
    String name;
    String abbreviation;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<Link> links;
}
