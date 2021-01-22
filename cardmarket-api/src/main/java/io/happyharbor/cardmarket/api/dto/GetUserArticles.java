package io.happyharbor.cardmarket.api.dto;

import lombok.*;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class GetUserArticles {
    List<Article> articles;
}
