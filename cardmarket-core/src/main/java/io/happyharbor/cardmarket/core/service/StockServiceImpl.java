package io.happyharbor.cardmarket.core.service;

import io.happyharbor.cardmarket.api.dto.stock.MyArticle;
import io.happyharbor.cardmarket.api.helper.SameArticle;
import io.happyharbor.cardmarket.api.helper.SimilarArticle;
import io.happyharbor.cardmarket.api.service.ClientService;
import io.happyharbor.cardmarket.api.service.StockService;
import io.happyharbor.cardmarket.core.csv.CsvArticle;
import io.happyharbor.cardmarket.core.csv.CsvHelper;
import io.happyharbor.cardmarket.core.property.PriceProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Slf4j
public class StockServiceImpl implements StockService {

    private final ClientService clientService;
    private final Set<String> powerUsers;
    private final PriceProperties priceProperties;
    private final CsvHelper csvHelper;

    private static final long LOT_THRESHOLD = 4;
    private static final long MAX_LOTS = 5;
    private static final BigDecimal LOT_INCREASE = new BigDecimal("1.2");
    private static final BigDecimal MIN_PRICE = new BigDecimal("0.05");

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

        Map<SimilarArticle, List<BigDecimal>> articles = new HashMap<>();
        powerUsers.forEach(user -> {
            log.debug("Fetching articles for user {}...", user);
            Map<SimilarArticle, BigDecimal> userArticles = clientService.getUserArticles(user).join();
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

        updateArticles(articles);

        log.info("Prices Updated");
    }

    @Override
    public void updatePricesFromCsv() {
        log.debug("Update prices from csv starting...");

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

        updateArticles(powerSellersPrices);

        log.info("Prices Updated from csv");
    }

    @SneakyThrows
    @Override
    public void test() {
        // Left empty on purpose
    }

    private void updateArticles(final Map<SimilarArticle, List<BigDecimal>> powerSellersPrices) {
        Map<SimilarArticle, BigDecimal> articlePriceMap = mergePowerSellerPrices(powerSellersPrices);

        if (!Boolean.TRUE.equals(clientService.goOnVacation(true).join())) {
            log.warn("Account cannot go on vacation, aborting update articles");
            return;
        }

        val stock = clientService.getStock().join();
        val compactStock = stock.stream()
                .sorted(Comparator.comparing(MyArticle::getPrice))
                .collect(toMap(SimilarArticle::new, a -> a, (a1, a2) -> a1.toBuilder().count(a1.getCount() + a2.getCount()).build()))
                .values()
                .stream()
                .map(article -> setPrice(article, articlePriceMap))
                .map(this::splitLotsOfArticles)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(SameArticle::new, Function.identity()));

        val it = stock.iterator();
        while (it.hasNext()) {
            val next = it.next();
            val sameArticle = new SameArticle(next);
            if (compactStock.containsKey(sameArticle)) {
                it.remove();
                compactStock.remove(sameArticle);
            }
        }

        clientService.deleteArticles(stock).join();
        clientService.addArticles(new ArrayList<>(compactStock.values())).join();

        if (!Boolean.TRUE.equals(clientService.goOnVacation(false).join())) {
            log.warn("Account could not return from vacation");
        }
    }

    private Map<SimilarArticle, BigDecimal> mergePowerSellerPrices(final Map<SimilarArticle, List<BigDecimal>> articlesMap) {
        return articlesMap.entrySet()
                          .stream()
                          .collect(toMap(Map.Entry::getKey, e -> mergePrices(e.getValue())));
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

    private MyArticle setPrice(final MyArticle article, final Map<SimilarArticle, BigDecimal> articlePrice) {
        if (article.getProduct().getCollectorsNumber() == null) {
            return article; // only single cards are updated
        }

        if (!articlePrice.containsKey(new SimilarArticle(article))) {
            log.trace("Article is not found in any other user: {}", article);
            return article;
        }

        val price = articlePrice.get(new SimilarArticle(article));
        val newPrice = price.compareTo(MIN_PRICE) < 0 ? MIN_PRICE : price;

        if (article.getPrice().compareTo(newPrice) == 0) {
            return article;
        }

        if (article.getPrice().compareTo(priceProperties.getTooHighPriceThreshold()) > 0 &&
                article.getPrice().compareTo(newPrice.multiply(priceProperties.getChangePriceThreshold())) > 0) {
            log.debug("Article has too high price, should be changed manually: {}", article);
            return article;
        }

        return article.toBuilder().price(newPrice).build();
    }

    private List<MyArticle> splitLotsOfArticles(final MyArticle article) {
        if (article.getCount() <= LOT_THRESHOLD) {
            return Collections.singletonList(article);
        }

        long count = article.getCount();
        BigDecimal price = article.getPrice();
        val lotsArticles = new LinkedList<MyArticle>();
        var numberOfLots = 0;
        while (count > LOT_THRESHOLD && numberOfLots < MAX_LOTS - 1) {
            lotsArticles.add(article.toBuilder()
                    .price(price).count(LOT_THRESHOLD).build());
            price = price.multiply(LOT_INCREASE).setScale(2, RoundingMode.CEILING);
            count -= LOT_THRESHOLD;
            numberOfLots++;
        }
        if (count > 0) {
            lotsArticles.add(article.toBuilder()
                    .price(price)
                    .count(count)
                    .build());
        }
        return lotsArticles;
    }
}
