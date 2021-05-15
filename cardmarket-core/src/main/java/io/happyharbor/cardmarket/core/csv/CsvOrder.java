package io.happyharbor.cardmarket.core.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)
public class CsvOrder {
    @CsvBindByName
    Integer orderNum;
    @CsvBindByName
    String name;
    @CsvBindByName
    String extra;
    @CsvBindByName
    String street;
    @CsvBindByName
    String postCode;
    @CsvBindByName
    String city;
    @CsvBindByName
    String country;
}
