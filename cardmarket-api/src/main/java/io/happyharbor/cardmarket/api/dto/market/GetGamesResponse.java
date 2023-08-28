package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class GetGamesResponse {
    @JsonAlias("game")
    List<Game> games;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<Link> links;
}
