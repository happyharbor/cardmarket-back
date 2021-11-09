package io.happyharbor.cardmarket.api.dto.order;

import lombok.*;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class CancellationRequestExtended {
    LocalDateTime date;
    String reason;
    String status;
    String answer;
}
