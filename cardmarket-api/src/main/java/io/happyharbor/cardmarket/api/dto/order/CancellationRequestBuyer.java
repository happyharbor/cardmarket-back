package io.happyharbor.cardmarket.api.dto.order;

import lombok.*;

import java.time.LocalDateTime;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class CancellationRequestBuyer {
    LocalDateTime date;
    String reason;
    String status;
    String answer;
}
