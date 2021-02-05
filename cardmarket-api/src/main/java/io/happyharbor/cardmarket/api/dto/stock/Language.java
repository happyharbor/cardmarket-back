package io.happyharbor.cardmarket.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Language {
    @JsonProperty("idLanguage")
    Integer languageId;
    String languageName;
}
