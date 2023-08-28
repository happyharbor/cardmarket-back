package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Expansion {
    @JsonAlias("idExpansion")
    Integer expansionId;
    String enName;
    @JsonAlias(value = "localization")
    List<Localization> localizations;
    String abbreviation;
    Integer icon;
    LocalDateTime releaseDate;
    Boolean isReleased;
    @JsonAlias("idGame")
    Integer gameId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<Link> links;
}
