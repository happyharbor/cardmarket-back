package io.happyharbor.cardmarket.api.dto;

import lombok.*;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Article {
    Integer id;
}
