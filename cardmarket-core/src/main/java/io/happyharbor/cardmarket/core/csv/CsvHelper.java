package io.happyharbor.cardmarket.core.csv;

import com.opencsv.bean.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.List;
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
    public <T> void saveToCsv(final String filePath, final List<T> beans) {

        Writer writer = new FileWriter(filePath);
        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).build();
        beanToCsv.write(beans);
        writer.close();
    }

    @SneakyThrows
    public <T> OutputStream writeCsv(final List<T> beans) {
        OutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream);
        final StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                .build();
        beanToCsv.write(beans);
        writer.close();
        return outputStream;
    }
}
