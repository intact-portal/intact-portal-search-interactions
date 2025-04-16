package uk.ac.ebi.intact.search.interactions.model;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder(toBuilder = true)
@Jacksonized
@Data
public class SimpleInteractionQueryParameters {

    @NonNull
    private String query;
    private boolean advancedSearch;
    private int pageSize;
    private int pageNumber;
}

