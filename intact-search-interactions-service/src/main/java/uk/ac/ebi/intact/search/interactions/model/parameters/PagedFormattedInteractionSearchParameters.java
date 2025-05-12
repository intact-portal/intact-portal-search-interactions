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
        return (PagedFormattedInteractionSearchParametersBuilder<?,?>) parameters.toBuilder();
    }
}
