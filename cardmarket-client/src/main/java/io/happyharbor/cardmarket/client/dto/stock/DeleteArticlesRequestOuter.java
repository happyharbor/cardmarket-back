package io.happyharbor.cardmarket.client.dto.stock;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JacksonXmlRootElement(localName = "request")
public class DeleteArticlesRequestOuter {
    @JacksonXmlElementWrapper(useWrapping = false)
    List<DeleteArticlesRequest> article;
}
