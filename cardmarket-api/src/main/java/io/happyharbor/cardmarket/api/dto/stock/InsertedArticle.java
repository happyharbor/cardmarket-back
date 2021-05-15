package io.happyharbor.cardmarket.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class InsertedArticle {
    boolean success;
    @JsonProperty("tried")
    NotAddedArticle notAddedArticle;
    @JsonProperty("idArticle")
    MyArticle addedArticle;
    String error;
}
