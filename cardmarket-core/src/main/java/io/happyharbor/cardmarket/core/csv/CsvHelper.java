package io.happyharbor.cardmarket.core.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import io.happyharbor.cardmarket.api.helper.GroupedArticle;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvHelper {

    private final ResourceLoader resourceLoader;

    @SneakyThrows
    public <T> List<T> readCsv(final File file, Class<? extends T> clazz) {
        return new CsvToBeanBuilder<T>(new FileReader(file)).withType(clazz).build().parse();
    }

    @SneakyThrows
    public List<File> loadCsvs() {
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                                                   .getResources("classpath*:users-articles/*.csv");
        return Arrays.stream(resources).map(resource -> {
            try {
                return resource.getFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
    }

    @SneakyThrows
    public void saveToCsv(String user, Map<GroupedArticle, BigDecimal> userArticles) {

        Writer writer = new FileWriter(String.format("%s.csv", user));
        StatefulBeanToCsv<Object> beanToCsv = new StatefulBeanToCsvBuilder<>(writer).build();
        beanToCsv.write(userArticles.entrySet().stream().map(e -> CsvArticle.builder()
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
                .collect(Collectors.toList()));
        writer.close();
    }
}
