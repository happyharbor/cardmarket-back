package io.happyharbor.cardmarket.api.dto.order;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.Month;
import java.util.Set;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class NoCardsInMonthsRequest {
    @NotEmpty(message = "Months cannot be empty")
    Set<Month> months;
}
