package uk.ac.ebi.intact.search.interactions.model.parameters;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@Data
public class PagedFormattedInteractionSearchParameters extends PagedInteractionSearchParameters {
    @Builder.Default
    protected InteractionExportFormat format = InteractionExportFormat.miJSON;

    public static PagedFormattedInteractionSearchParametersBuilder<?,?> copyParameters(PagedInteractionSearchParameters parameters) {
        return PagedFormattedInteractionSearchParameters.builder()
                .query(parameters.getQuery())
                .batchSearch(parameters.isBatchSearch())
                .advancedSearch(parameters.isAdvancedSearch())

                .interactionDetectionMethodsFilter(parameters.getInteractionDetectionMethodsFilter())
                .interactionTypesFilter(parameters.getInteractionTypesFilter())
                .interactionHostOrganismsFilter(parameters.getInteractionHostOrganismsFilter())
                .interactorTypesFilter(parameters.getInteractorTypesFilter())
                .interactorSpeciesFilter(parameters.getInteractorSpeciesFilter())

                .intraSpeciesFilter(parameters.isIntraSpeciesFilter())
                .expansionFilter(parameters.isExpansionFilter())
                .mutationFilter(parameters.isMutationFilter())
                .negativeFilter(parameters.getNegativeFilter())

                .maxMIScore(parameters.getMaxMIScore())
                .minMIScore(parameters.getMinMIScore())

                .binaryInteractionIds(parameters.getBinaryInteractionIds())
                .interactorAcs(parameters.getInteractorAcs())

                .page(parameters.getPage())
                .pageSize(parameters.getPageSize())
                .sort(parameters.getSort());
    }
}
