package uk.ac.ebi.intact.search.interactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionIndexService;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Elisabet Barrera
 */

@RestController
public class SearchInteractionController {

    private InteractionIndexService interactionIndexService;
    private InteractionSearchService interactionSearchService;

    @Autowired
    public SearchInteractionController(InteractionIndexService interactionIndexService,
                                       InteractionSearchService interactionSearchService) {
        this.interactionIndexService = interactionIndexService;
        this.interactionSearchService = interactionSearchService;
    }

    @GetMapping(value ="/interaction/findInteractions/{query}", produces = {APPLICATION_JSON_VALUE})
    public Page<SearchInteraction> findInteractions( @PathVariable String query) {
        return interactionSearchService.findInteractions(query);
    }

    @GetMapping(value = "/interaction/findInteractionWithFacet",
            params = {
                    "query",
                    "page",
                    "pageSize"
            },
            produces = {APPLICATION_JSON_VALUE})
     public SearchInteractionResult findInteractionWithFacet(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "detectionMethodFilter", required = false) Set<String> detectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "hostOrganismFilter", required = false) Set<String> hostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiscore",defaultValue = "0", required = false) double minMiscore,
            @RequestParam(value = "maxMiscore",defaultValue = "1", required = false) double maxMiscore,
            @RequestParam(value = "species", required = false) Set<String> species,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return new SearchInteractionResult(interactionSearchService.findInteractionWithFacet(
                query,
                detectionMethodFilter,
                interactionTypeFilter,
                hostOrganismFilter,
                isNegativeFilter,
                minMiscore,
                maxMiscore,
                species,
                interSpecies,
                page,
                pageSize));
    }

    @GetMapping(value = "/interaction/countInteractionResult",
            params = {
                    "query",
                    "interactorAc"
            },
            produces = {APPLICATION_JSON_VALUE})
    public long countInteractionResult(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "interactorAc") String interactorAc,
            @RequestParam(value = "detectionMethodFilter", required = false) Set<String> detectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "hostOrganismFilter", required = false) Set<String> hostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiscore",defaultValue = "0", required = false) double minMiscore,
            @RequestParam(value = "maxMiscore",defaultValue = "1", required = false) double maxMiscore,
            @RequestParam(value = "species", required = false) Set<String> species,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies) {
        return interactionSearchService.countInteractionResult(
                query,
                interactorAc,
                detectionMethodFilter,
                interactionTypeFilter,
                hostOrganismFilter,
                isNegativeFilter,
                minMiscore,
                maxMiscore,
                species,
                interSpecies);
    }

    @GetMapping(value = "/interaction/countTotal",
            produces = {APPLICATION_JSON_VALUE})
    public long countTotal() {
        return interactionSearchService.countTotal();
    }
}
