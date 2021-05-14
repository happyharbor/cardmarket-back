package io.happyharbor.cardmarket.core.service;

import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import io.happyharbor.cardmarket.api.helper.GroupedArticle;
import io.happyharbor.cardmarket.api.service.ClientService;
import io.happyharbor.cardmarket.api.service.StockService;
import io.happyharbor.cardmarket.core.csv.CsvArticle;
import io.happyharbor.cardmarket.core.csv.CsvHelper;
import io.happyharbor.cardmarket.core.property.PriceProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
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
            var filePath = String.format("%s%s.csv", "power-users-articles/", user);
            csvHelper.saveToCsv(filePath, csvArticles);
            userArticles.forEach((k, v) -> articles.computeIfAbsent(k, a -> new LinkedList<>()).add(v));
        });

        Map<GroupedArticle, BigDecimal> articlePriceMap = mergePowerSellerPrices(articles);

        List<MyArticle> stock = stockFuture.join();
        List<MyArticle> articlesToUpdate = getArticlesToUpdate(stock, articlePriceMap);
        clientService.updateArticles(articlesToUpdate).join();

        log.info("Prices Updated");
    }

    @Override
    public void updatePricesFromCsv() {
        log.debug("Update prices from csv starting...");

        CompletableFuture<List<MyArticle>> stockFuture = clientService.getStock();

        val powerSellersPrices = csvHelper.loadCsvs().stream()
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
                .orElseThrow();

        Map<GroupedArticle, BigDecimal> articlePriceMap = mergePowerSellerPrices(powerSellersPrices);

        List<MyArticle> stock = stockFuture.join();
        List<MyArticle> articlesToUpdate = getArticlesToUpdate(stock, articlePriceMap);
        clientService.updateArticles(articlesToUpdate).join();

        log.info("Prices Updated from csv");
    }

    private Map<GroupedArticle, BigDecimal> mergePowerSellerPrices(final Map<GroupedArticle, List<BigDecimal>> groupedArticleListMap1) {
        return groupedArticleListMap1
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey,
                        e -> mergePrices(e.getValue())
                ));
    }

    private List<MyArticle> getArticlesToUpdate(List<MyArticle> stock, Map<GroupedArticle, BigDecimal> articlePrice) {

        return stock.stream()
                .filter(myArticle -> myArticle.getProduct().getCollectorsNumber() != null) // only singles
                .filter(myArticle -> {
                    boolean containsArticle = articlePrice.containsKey(new GroupedArticle(myArticle));
                    if (!containsArticle) {
                        log.debug("Article is not found to any other user: {}", myArticle);
                    }
                    return containsArticle;
                })
                .filter(myArticle -> {
                    BigDecimal cardmarketPrice = articlePrice.get(new GroupedArticle(myArticle));
                    if (myArticle.getPrice().compareTo(priceProperties.getTooHighPriceThreshold()) > 0 &&
                            myArticle.getPrice().compareTo(cardmarketPrice.multiply(priceProperties.getChangePriceThreshold())) > 0) {
                        log.info("Article has too high price, should be changed manually: {}", myArticle);
                        return false;
                    }
                    return true;
                })
                .filter(a -> articlePrice.get(new GroupedArticle(a)).compareTo(a.getPrice()) != 0)
                .map(a -> a.toBuilder().price(articlePrice.get(new GroupedArticle(a))).build())
                .collect(Collectors.toList());
    }

    private BigDecimal mergePrices(List<BigDecimal> observations)
    {
        if (observations.size() == 1) {
            return observations.get(0);
        }

        val keptObservations = observations.stream()
                .sorted()
                .limit(priceProperties.getPricesToKeep())
                .collect(toList());

        return keptObservations.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(keptObservations.size()), RoundingMode.FLOOR);
    }
}
