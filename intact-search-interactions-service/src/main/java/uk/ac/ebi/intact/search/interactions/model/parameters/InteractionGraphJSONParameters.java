package uk.ac.ebi.intact.search.interactions.model.parameters;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class InteractionGraphJSONParameters implements SimpleSearchParametersI {
    @NonNull
    @Schema(description = "The query to perform, can be advanced search or normal token based. * supported", example = "*")
    protected String query;

    @Schema(description = "Whether the query is a batch search query", example = "false")
    protected boolean batchSearch;
    @Schema(description = "Whether the query is an advanced search query", example = "false")
    protected boolean advancedSearch;
    @Schema(description = "Apply interactor species filter to only allow X-X species and not X-Y when X is the species filter", example = "false")
    protected boolean intraSpeciesFilter;

    @Schema(description = "Filters interactions based on interactor species", example = "[]")
    protected Set<String> interactorSpeciesFilter;
    @Schema(description = "Filters interactions based on interactor types", example = "[]")
    protected Set<String> interactorTypesFilter;
    @Schema(description = "Filters interactions based on interaction detection method", example = "[]")
    protected Set<String> interactionDetectionMethodsFilter;
    @Schema(description = "Filters interactions based on interaction types", example = "[]")
    protected Set<String> interactionTypesFilter;
    @Schema(description = "Filters interactions based on interaction host organism", example = "[]")
    protected Set<String> interactionHostOrganismsFilter;

    @Schema(description = "Filters to get only negative interactions", example = "POSITIVE_ONLY")
    @Builder.Default
    protected NegativeFilterStatus negativeFilter = NegativeFilterStatus.POSITIVE_ONLY;
    @Schema(description = "Filters to get only interactions affected by mutation", example = "false")
    protected boolean mutationFilter;
    @Schema(description = "Filters to get only non expanded interactions", example = "false")
    protected boolean expansionFilter;

    @Schema(description = "Filters to get interaction with MI-score above value", example = "0")
    @Builder.Default
    protected double minMIScore = 0;
    @Schema(description = "Filters to get interaction with MI-score under value", example = "1")
    @Builder.Default
    protected double maxMIScore = 1;
}
