package io.happyharbor.cardmarket.api.dto.account;

import lombok.*;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class UserName {
    String firstName;
    String lastName;
}
