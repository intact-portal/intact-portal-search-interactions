package uk.ac.ebi.intact.search.interactions.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.intact.search.interactions.model.Interaction;
import uk.ac.ebi.intact.search.interactions.service.InteractionIndexService;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;

/**
 * @author Elisabet Barrera
 */

@RestController
@RequestMapping("/interactions")
public class InteractionsController {

    private InteractionIndexService interactionIndexService;
    private InteractionSearchService interactionSearchService;

    @Autowired
    public InteractionsController(InteractionIndexService interactionIndexService,
                                      InteractionSearchService interactionSearchService) {
        this.interactionIndexService = interactionIndexService;
        this.interactionSearchService = interactionSearchService;
    }

    @RequestMapping("/")
    public String SpringBootSolrExample() {
        return "Welcome to Spring Boot solr Example";
    }

    @RequestMapping("/findInteractions/{query}")
    public Page<Interaction> findInteractions(
            @PathVariable String query) {
        return this.interactionSearchService.findInteractions(query);
    }

}
