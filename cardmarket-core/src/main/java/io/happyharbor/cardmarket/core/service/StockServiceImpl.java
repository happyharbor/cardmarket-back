package io.happyharbor.cardmarket.core.service;

import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import io.happyharbor.cardmarket.api.helper.GroupedArticle;
import io.happyharbor.cardmarket.api.service.ClientService;
import io.happyharbor.cardmarket.api.service.StockService;
import io.happyharbor.cardmarket.core.csv.CsvArticle;
import io.happyharbor.cardmarket.core.csv.CsvHelper;
import io.happyharbor.cardmarket.core.property.PriceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class StockServiceImpl implements StockService {

    private final ClientService clientService;
    private final Set<String> powerUsers;
    private final PriceProperties priceProperties;
    private final CsvHelper csvHelper;

    public StockServiceImpl(final ClientService clientService,
                            @Value("${power-users}") final Set<String> powerUsers,
                            final PriceProperties priceProperties,
                            final CsvHelper csvHelper) {
        this.clientService = clientService;
        this.powerUsers = powerUsers;
        this.priceProperties = priceProperties;
        this.csvHelper = csvHelper;
    }

    @Override
    public void updatePrices() {
        log.debug("Update prices starting...");

        CompletableFuture<List<MyArticle>> stockFuture = clientService.getStock();

        Map<GroupedArticle, List<BigDecimal>> articles = new HashMap<>();
        powerUsers.forEach(user -> {
            log.debug("Fetching articles for user {}...", user);
            Map<GroupedArticle, BigDecimal> userArticles = clientService.getUserArticles(user).join();
            final List<CsvArticle> csvArticles = userArticles.entrySet().stream().map(e -> CsvArticle.builder()
                    .productId(e.getKey().getProductId())
                    .languageId(e.getKey().getLanguage().getLanguageId())
                    .languageName(e.getKey().getLanguage().getLanguageName())
                    .currencyId(e.getKey().getCurrencyId())
                    .isFoil(e.getKey().getIsFoil())
                    .isAltered(e.getKey().getIsAltered())
                    .isPlayset(e.getKey().getIsPlayset())
                    .isSigned(e.getKey().getIsSigned())
                    .isFirstEd(e.getKey().getIsFirstEd())
                    .price(e.getValue())
                    .build())
                    .collect(toList());
            final String filePath = String.format("%s.csv", user);
            csvHelper.saveToCsv(filePath, csvArticles);
            userArticles.forEach((k, v) -> articles.computeIfAbsent(k, a -> new LinkedList<>()).add(v));
        });

        Map<GroupedArticle, BigDecimal> articlePrice = articles.entrySet().stream().collect(toMap(
                Map.Entry::getKey,
                e -> upperTrimmedMean(e.getValue(), priceProperties.getPercentageOfPowersellers())
        ));

        List<MyArticle> stock = stockFuture.join();
        List<MyArticle> articlesToUpdate = getArticlesToUpdate(stock, articlePrice);
        clientService.updateArticles(articlesToUpdate).join();

        log.debug("Prices Updated");
    }

    @Override
    public void updatePricesFromCsv() {
        log.debug("Update prices from csv starting...");

        CompletableFuture<List<MyArticle>> stockFuture = clientService.getStock();

        Map<GroupedArticle, BigDecimal> articlePrice = csvHelper.loadCsvs().stream()
                .map(r -> csvHelper.readCsv(r, CsvArticle.class))
                .map(csvArticles -> csvArticles.stream()
                        .collect(toMap(CsvArticle::toGroupedArticle,
                                csvArticle -> {
                                    List<BigDecimal> list = new LinkedList<>();
                                    list.add(csvArticle.getPrice());
                                    return list;
                                }
                        )))
                .reduce((groupedArticleListMap, groupedArticleListMap2) -> {
                    groupedArticleListMap2.forEach((k, v) -> {
                        List<BigDecimal> bigDecimals = groupedArticleListMap.computeIfAbsent(k, k1 -> new LinkedList<>());
                        bigDecimals.addAll(v);
                    });
                    return groupedArticleListMap;
                })
                .orElseThrow()
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey,
                        e -> upperTrimmedMean(e.getValue(), priceProperties.getPercentageOfPowersellers())
                ));

        List<MyArticle> stock = stockFuture.join();
        List<MyArticle> articlesToUpdate = getArticlesToUpdate(stock, articlePrice);
        clientService.updateArticles(articlesToUpdate).join();

        log.debug("Prices Updated from csv");
    }

    private List<MyArticle> getArticlesToUpdate(List<MyArticle> stock, Map<GroupedArticle, BigDecimal> articlePrice) {

        return stock.stream()
                .filter(a -> a.getProduct().getCollectorsNumber() != null) // only singles
                .filter(a -> {
                    boolean containsArticle = articlePrice.containsKey(new GroupedArticle(a));
                    if (!containsArticle) {
                        log.debug("Article is not found to any other user: {}", a);
                    }
                    return containsArticle;
                })
                .filter(a -> {
                    BigDecimal cardmarketPrice = articlePrice.get(new GroupedArticle(a));
                    BigDecimal increaseRatio = a.getPrice().subtract(cardmarketPrice)
                            .divide(cardmarketPrice, RoundingMode.FLOOR);
                    if (increaseRatio.doubleValue() > priceProperties.getTooHighPriceThreshold()) {
                        log.info("Article has too high price, should be changed manually: {}", a);
                    }
                    return increaseRatio.compareTo(priceProperties.getChangePriceThreshold()) < 1;
                })
                .filter(a -> articlePrice.get(new GroupedArticle(a)).compareTo(a.getPrice()) != 0)
                .map(a -> a.toBuilder().price(articlePrice.get(new GroupedArticle(a))).build())
                .collect(Collectors.toList());
    }

    public BigDecimal upperTrimmedMean(List<BigDecimal> observations, BigDecimal percentage)
    {
        List<BigDecimal> sorted = observations.stream().sorted().collect(toList());
        var n = sorted.size();
        var k = percentage.multiply(new BigDecimal(n)).setScale(0, RoundingMode.UP).intValue();
        k = k == 0 ? 1 : k;

        var mean = BigDecimal.ZERO;
        n = n - k > 0 ? k : n;

        for (var i = 0; i < n; i++)
            mean = mean.add(sorted.get(i));

        return mean.divide(new BigDecimal(n), RoundingMode.FLOOR);
    }
}
