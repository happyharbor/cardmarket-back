package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class GetExpansionsResponse {
    @JsonAlias("expansion")
    List<Expansion> expansions;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<Link> links;
}
