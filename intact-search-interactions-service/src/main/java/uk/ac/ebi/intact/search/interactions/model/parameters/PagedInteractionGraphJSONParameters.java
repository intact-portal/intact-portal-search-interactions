package uk.ac.ebi.intact.search.interactions.model.parameters;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class PagedInteractionGraphJSONParameters extends InteractionGraphJSONParameters {

    @Builder.Default
    protected int page = 0;
    @Builder.Default
    protected int pageSize = 20;

    protected Sort sort;

    public static PagedInteractionGraphJSONParameters.PagedInteractionGraphJSONParametersBuilder<?, ?> copyParameters(InteractionGraphJSONParameters parameters) {
        return (PagedInteractionGraphJSONParameters.PagedInteractionGraphJSONParametersBuilder<?, ?>) parameters.toBuilder();
    }

    public PagedInteractionSearchParameters toPagedInteractionSearchParameters() {
        return PagedInteractionSearchParameters.builder()
                .query(query)
                .batchSearch(batchSearch)
                .advancedSearch(advancedSearch)
                .intraSpeciesFilter(intraSpeciesFilter)

                .interactorSpeciesFilter(interactorSpeciesFilter)
                .interactorTypesFilter(interactorTypesFilter)
                .interactionDetectionMethodsFilter(interactionDetectionMethodsFilter)
                .interactionTypesFilter(interactionTypesFilter)
                .interactionHostOrganismsFilter(interactionHostOrganismsFilter)

                .expansionFilter(expansionFilter)
                .mutationFilter(mutationFilter)
                .negativeFilter(negativeFilter)

                .page(page)
                .pageSize(pageSize)
                .sort(sort)

                .binaryInteractionIds(null)
                .interactorAcs(null)
                .build();
    }
}
