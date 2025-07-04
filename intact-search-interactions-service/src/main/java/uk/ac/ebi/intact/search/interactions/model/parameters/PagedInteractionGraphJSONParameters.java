package uk.ac.ebi.intact.search.interactions.model.parameters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
@Data
public class PagedInteractionGraphJSONParameters extends InteractionGraphJSONParameters implements PagedParametersI {
    @Schema(description = "Page index, starting at 0", example = "0")
    @Builder.Default
    protected int page = 0;

    @Schema(description = "Amount of documents per page", example = "10")
    @Builder.Default
    protected int pageSize = 10;

    @Schema(description = "Fields by which to order the result query", example = "null")
    protected List<PagedInteractionSearchParameters.Order> sort;

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
