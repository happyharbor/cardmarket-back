package io.happyharbor.cardmarket.core.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CsvHelper {

    @SneakyThrows
    public <T> List<T> readCsv(final File file, Class<? extends T> clazz) {
        return new CsvToBeanBuilder<T>(new FileReader(file)).withType(clazz).build().parse();
    }

    @SneakyThrows
    public List<File> loadCsvs() {
        try (val paths = Files.walk(Paths.get("power-users-articles"))) {
            return paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        }
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
