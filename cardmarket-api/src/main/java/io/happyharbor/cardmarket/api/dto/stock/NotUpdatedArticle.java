package io.happyharbor.cardmarket.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class NotUpdatedArticle {
    @JsonProperty("tried")
    MyArticle article;
    String error;
}
