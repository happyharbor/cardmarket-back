package io.happyharbor.cardmarket.api.dto.market;

import lombok.*;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Link {
    String rel;
    String href;
    String method;
}
