package io.happyharbor.cardmarket.api.dto.stock;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class InsertedArticle {
    boolean success;
    @JsonAlias("tried")
    NotAddedArticle notAddedArticle;
    @JsonAlias("idArticle")
    MyArticle addedArticle;
    String error;
}
