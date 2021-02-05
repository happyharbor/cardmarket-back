package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Address {
    String name;
    String extra;
    String street;
    @JsonProperty("zip")
    String postCode;
    String city;
    Country country;
}
