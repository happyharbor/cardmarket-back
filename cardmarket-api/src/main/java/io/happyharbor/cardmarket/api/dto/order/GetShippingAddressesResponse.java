package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.happyharbor.cardmarket.api.dto.Link;
import io.happyharbor.cardmarket.api.dto.stock.OtherUserArticle;
import lombok.*;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class GetShippingAddressesResponse {
    @JsonProperty("article")
    List<OtherUserArticle> articles;
    List<Link> links;
}
