package uk.ac.ebi.intact.search.interactions.model.parameters;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder(toBuilder = true)
@Jacksonized
@Data
public class SimpleInteractionQueryParameters implements PagedParametersI, SimpleSearchParametersI {
    @NonNull
    @Schema(description = "The query to perform, can be advanced search or normal token based. * supported", example = "*")
    private String query;
    @Schema(description = "Whether the query is an advanced search query", example = "false")
    private boolean advancedSearch;
    @Schema(description = "Page index, starting at 0", example = "0")
    @Builder.Default
    private int page = 0;
    @Schema(description = "Amount of documents per page", example = "10")
    @Builder.Default
    private int pageSize = 10;

    @Override
    public boolean isBatchSearch() {
        return false;
    }
}

