package io.happyharbor.cardmarket.api.dto.order;

import lombok.*;

import java.time.OffsetDateTime;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Tmp {
    OffsetDateTime time;
}
