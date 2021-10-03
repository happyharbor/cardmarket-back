package io.happyharbor.cardmarket.api.dto.order;

import lombok.*;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class CancellationRequest {
    LocalDateTime date;
    String reason;
    Boolean rejected;
    String rejectAnswer;
}
