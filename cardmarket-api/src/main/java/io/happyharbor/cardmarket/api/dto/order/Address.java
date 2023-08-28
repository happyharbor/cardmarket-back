package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Address {
    String name;
    String extra;
    String street;
    @JsonAlias("zip")
    String postCode;
    String city;
    Country country;
}
