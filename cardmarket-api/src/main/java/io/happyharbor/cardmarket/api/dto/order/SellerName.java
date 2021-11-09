package io.happyharbor.cardmarket.api.dto.order;

import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class SellerName {
    String company;
    String firstName;
    String lastName;
}
