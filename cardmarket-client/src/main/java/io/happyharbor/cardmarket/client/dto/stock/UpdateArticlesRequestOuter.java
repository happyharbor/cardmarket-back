package io.happyharbor.cardmarket.client.dto.stock;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "request")
public class UpdateArticlesRequestOuter {
    @JacksonXmlElementWrapper(useWrapping = false)
    List<UpdateArticlesRequest> article;
}
