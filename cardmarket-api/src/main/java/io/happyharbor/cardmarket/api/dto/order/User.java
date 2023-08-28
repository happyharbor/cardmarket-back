package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class User {
    @JsonAlias("idUser")
    Integer userId;
    String username;
    Integer isCommercial;
    Integer reputation;
    Long sellCount;
    boolean onVacation;
    SellerName name;
    Address address;
    String phone;
    String email;
    String vat;
    LocalDateTime registrationDate;
    Boolean isSeller;
    String legalInformation;
    Long unsentShipments;
    Integer shipsFast;
    Long soldItems;
    Integer avgShippingTime;
    Integer riskGroup;
    String lossPercentage;
}
