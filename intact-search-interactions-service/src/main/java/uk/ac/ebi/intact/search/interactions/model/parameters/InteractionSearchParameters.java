package uk.ac.ebi.intact.search.interactions.model.parameters;

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
    protected Set<Long> binaryInteractionIds;
    protected Set<String> interactorAcs;

    InteractionSearchParametersBuilder<?, ?> copyParameters(InteractionGraphJSONParameters parameter) {
        return (InteractionSearchParametersBuilder<?, ?>) parameter.toBuilder();
    }

}
