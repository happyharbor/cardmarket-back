package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Order {
    @JsonAlias("idOrder")
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
    @JsonAlias("article")
    List<MyArticle> articles;
    BigDecimal articleValue;
    BigDecimal totalValue;
    @JsonAlias("idCurrency")
    Integer currencyId;
    String currencyCode;
    CancellationRequest cancellationRequest;
    CancellationRequests cancellationRequests;
    Evaluation evaluation;
}
