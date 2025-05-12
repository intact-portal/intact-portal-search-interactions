package uk.ac.ebi.intact.search.interactions.model.parameters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
@Data
public class PagedInteractionSearchParameters extends InteractionSearchParameters implements PagedParametersI {

    @Schema(description = "Page index, starting at 0", example = "0")
    @Builder.Default
    private int page = 0;

    @Schema(description = "Amount of documents per page", example = "10")
    @Builder.Default
    private int pageSize = 10;

    @Schema(description = "Fields by which to order the result query")
    private List<Order> sort;

    public static PagedInteractionSearchParametersBuilder<?,?> copyParameters(InteractionSearchParameters parameters) {
        return (PagedInteractionSearchParametersBuilder<?,?>) parameters.toBuilder();
    }

    public Sort standardiseSort() {
        return Sort.by(sort.stream()
                .map(order -> new Sort.Order(order.direction, order.field))
                .collect(Collectors.toList())
        );
    }

    @Data
    public static class Order {
        @Schema(example = "DESC")
        Sort.Direction direction;
        @Schema(example = "intact_miscore")
        String field;
    }

}
