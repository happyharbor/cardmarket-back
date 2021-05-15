package io.happyharbor.cardmarket.client.dto.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.happyharbor.cardmarket.api.dto.stock.DeletedArticle;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class DeleteArticleResponse {
    @JsonProperty("deleted")
    List<DeletedArticle> articles = new ArrayList<>();
}
