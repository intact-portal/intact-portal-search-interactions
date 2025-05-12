package uk.ac.ebi.intact.search.interactions.model.parameters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
@Data
public class InteractionSearchParameters extends InteractionGraphJSONParameters {
    @Schema(description = "Filters to only include interactions among the provided set of binary interaction ids", example = "null")
    protected Set<Long> binaryInteractionIds;
    @Schema(description = "Filters to only include interactions involving the provided set of interactor ACs", example = "null")
    protected Set<String> interactorAcs;

    InteractionSearchParametersBuilder<?, ?> copyParameters(InteractionGraphJSONParameters parameter) {
        return (InteractionSearchParametersBuilder<?, ?>) parameter.toBuilder();
    }

}
