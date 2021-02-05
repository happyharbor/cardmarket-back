package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Order {
    @JsonProperty("idOrder")
    Integer orderId;
    Boolean isBuyer;
    User seller;
    User buyer;
    StateDto state;
    ShippingMethod shippingMethod;
    String trackingNumber;
    String temporaryEmail;
    Boolean isPresale;
    Address shippingAddress;
    String note;
    int articleCount;
    @JsonProperty("article")
    List<MyArticle> articles;
    BigDecimal articleValue;
    BigDecimal totalValue;
    @JsonProperty("idCurrency")
    Integer currencyId;
    String currencyCode;
}
