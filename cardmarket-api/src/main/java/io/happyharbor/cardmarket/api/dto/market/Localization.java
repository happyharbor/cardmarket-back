package io.happyharbor.cardmarket.api.dto.market;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Localization {
    String name;
    @JsonProperty("idLanguage")
    Integer languageId;
    String languageName;
}
