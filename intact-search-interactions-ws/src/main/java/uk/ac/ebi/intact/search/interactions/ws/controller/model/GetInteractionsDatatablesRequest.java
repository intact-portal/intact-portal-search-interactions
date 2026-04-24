package uk.ac.ebi.intact.search.interactions.ws.controller.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionSearchParameters;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
@Data
public class GetInteractionsDatatablesRequest extends PagedInteractionSearchParameters {

    @Schema(description = "This parameter is used to determine what kind of draw DataTables will perform", example = "0")
    @Builder.Default
    private int draw = 1;
}
