package uk.ac.ebi.intact.search.interactions.model.parameters;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.ac.ebi.intact.search.interactions.utils.NegativeFilterStatus;

import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
@Data
public class PagedInteractionSearchParameters extends InteractionSearchParameters {

    @Builder.Default
    protected Pageable pageable = PageRequest.of(0, 20);

    protected Sort sort;

    public static PagedInteractionSearchParametersBuilder<?,?> copyParameters(InteractionSearchParameters parameters) {
        return (PagedInteractionSearchParametersBuilder<?,?>) parameters.toBuilder();
    }
}
