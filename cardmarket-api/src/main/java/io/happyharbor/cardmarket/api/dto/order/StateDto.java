package io.happyharbor.cardmarket.api.dto.order;

import lombok.*;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class StateDto {
    OrderState state;
    LocalDateTime dateBought;
    LocalDateTime datePaid;
    LocalDateTime dateSent;
    LocalDateTime dateReceived;
    LocalDateTime dateCanceled;
    String reason;
    Integer wasMergedInto;
}
