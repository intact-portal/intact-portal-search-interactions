package uk.ac.ebi.intact.search.interactions.model.parameters;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import uk.ac.ebi.intact.search.interactions.utils.NegativeFilterStatus;

import java.util.Set;


@SuperBuilder(toBuilder = true)
@Jacksonized
@Data
public class InteractionGraphJSONParameters {
    @NonNull
    protected String query;
    protected boolean batchSearch;
    protected boolean advancedSearch;
    protected boolean intraSpeciesFilter;

    protected Set<String> interactorSpeciesFilter;
    protected Set<String> interactorTypesFilter;
    protected Set<String> interactionDetectionMethodsFilter;
    protected Set<String> interactionTypesFilter;
    protected Set<String> interactionHostOrganismsFilter;

    @Builder.Default
    protected NegativeFilterStatus negativeFilter = NegativeFilterStatus.POSITIVE_ONLY;
    protected boolean mutationFilter;
    protected boolean expansionFilter;

    @Builder.Default
    protected double minMIScore = 0;
    @Builder.Default
    protected double maxMIScore = 1;
}
