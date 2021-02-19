package io.happyharbor.cardmarket.api.helper;

import io.happyharbor.cardmarket.api.dto.stock.Language;
import io.happyharbor.cardmarket.api.dto.stock.OtherUserArticle;
import io.happyharbor.cardmarket.api.dto.stock.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GroupedArticleTest {

    @Test
    void test() {
        OtherUserArticle firstOtherUserArticle = OtherUserArticle.builder()
                .productId(1L)
                .language(Language.builder()
                        .languageId(1)
                        .languageName("English")
                        .build())
                .currencyId(1)
                .count(1L)
                .prices(List.of(
                        Price.builder()
                                .articlePrice(BigDecimal.ONE)
                                .currencyId(1)
                                .build(),
                        Price.builder()
                                .articlePrice(BigDecimal.ZERO)
                                .currencyId(2)
                                .build()))
                .condition("MT")
                .build();

        List<OtherUserArticle> otherUserArticles = List.of(
                firstOtherUserArticle,
                OtherUserArticle.builder()
                        .productId(1L)
                        .language(Language.builder()
                                .languageId(1)
                                .languageName("English")
                                .build())
                        .currencyId(1)
                        .count(1L)
                        .prices(List.of(
                                Price.builder()
                                        .articlePrice(BigDecimal.TEN)
                                        .currencyId(1)
                                        .build(),
                                Price.builder()
                                        .articlePrice(BigDecimal.ZERO)
                                        .currencyId(2)
                                        .build()))
                        .condition("MT")
                        .build());

        Map<GroupedArticle, BigDecimal> output = otherUserArticles.stream()
                .filter(a -> a.getCondition().equals("NM") || a.getCondition().equals("MT"))
                .collect(Collectors.toMap(GroupedArticle::new, article -> article.getPrices().stream().filter(p -> p.getCurrencyId() == 1).findAny()
                        .orElseThrow().getArticlePrice(), (o1, o2) -> o1.compareTo(o2) > 0 ? o2 : o1));

        assertEquals(Map.of(new GroupedArticle(firstOtherUserArticle), BigDecimal.ONE), output);
    }

    @Test
    void testNullWithTrue() {
        OtherUserArticle firstOtherUserArticle = OtherUserArticle.builder()
                .productId(1L)
                .language(Language.builder()
                        .languageId(1)
                        .languageName("English")
                        .build())
                .currencyId(1)
                .count(1L)
                .prices(List.of(
                        Price.builder()
                                .articlePrice(BigDecimal.ONE)
                                .currencyId(1)
                                .build(),
                        Price.builder()
                                .articlePrice(BigDecimal.ZERO)
                                .currencyId(2)
                                .build()))
                .condition("MT")
                .isFoil(true)
                .build();

        OtherUserArticle secondOtherUserArticle = OtherUserArticle.builder()
                .productId(1L)
                .language(Language.builder()
                        .languageId(1)
                        .languageName("English")
                        .build())
                .currencyId(1)
                .count(1L)
                .prices(List.of(
                        Price.builder()
                                .articlePrice(BigDecimal.TEN)
                                .currencyId(1)
                                .build(),
                        Price.builder()
                                .articlePrice(BigDecimal.ZERO)
                                .currencyId(2)
                                .build()))
                .condition("MT")
                .build();

        List<OtherUserArticle> otherUserArticles = List.of(firstOtherUserArticle, secondOtherUserArticle);

        Map<GroupedArticle, BigDecimal> output = otherUserArticles.stream()
                .filter(a -> a.getCondition().equals("NM") || a.getCondition().equals("MT"))
                .collect(Collectors.toMap(GroupedArticle::new, article -> article.getPrices().stream().filter(p -> p.getCurrencyId() == 1).findAny()
                        .orElseThrow().getArticlePrice(), (o1, o2) -> o1.compareTo(o2) > 0 ? o2 : o1));

        assertEquals(Map.of(new GroupedArticle(firstOtherUserArticle), BigDecimal.ONE, new GroupedArticle(secondOtherUserArticle), BigDecimal.TEN), output);
    }
}