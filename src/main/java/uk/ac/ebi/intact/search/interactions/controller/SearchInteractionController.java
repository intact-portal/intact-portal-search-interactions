package uk.ac.ebi.intact.search.interactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionResult;
import uk.ac.ebi.intact.search.interactions.service.InteractionIndexService;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;

import java.util.Set;

/**
 * @author Elisabet Barrera
 */

@RestController
@RequestMapping("/interaction")
public class SearchInteractionController {

    private InteractionIndexService interactionIndexService;
    private InteractionSearchService interactionSearchService;

    @Autowired
    public SearchInteractionController(InteractionIndexService interactionIndexService,
                                       InteractionSearchService interactionSearchService) {
        this.interactionIndexService = interactionIndexService;
        this.interactionSearchService = interactionSearchService;
    }

    @RequestMapping("/")
    public String SpringBootSolrExample() {
        return "Welcome to Spring Boot solr Example";
    }

    @RequestMapping("/findInteractions/{query}")
    public Page<SearchInteraction> findInteractions(
            @PathVariable String query) {
        return this.interactionSearchService.findInteractions(query);
    }

    @RequestMapping(value = "/findInteractionWithFacet",
            params = {
                    "query",
                    "page",
                    "pageSize"
            },
            method = RequestMethod.GET)
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
        return this.interactionSearchService.findInteractionWithFacet(query, detectionMethodFilter,
                interactionTypeFilter,hostOrganismFilter,isNegativeFilter,minMiscore,maxMiscore,species,interSpecies,
                page, pageSize);
    }

}
