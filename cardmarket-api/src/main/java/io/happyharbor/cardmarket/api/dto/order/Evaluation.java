package io.happyharbor.cardmarket.api.dto.order;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Evaluation {
    int evaluationGrade;
    int itemDescription;
    int packaging;
    String comment;
    @JsonAlias("complaint")
    List<String> complaints;
}
