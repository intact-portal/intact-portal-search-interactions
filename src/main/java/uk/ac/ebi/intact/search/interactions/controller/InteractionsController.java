package uk.ac.ebi.intact.search.interactions.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Elisabet Barrera
 */

@RestController
public class InteractionsController {


    @RequestMapping("/")
    public String SpringBootSolrExample() {
        return "Welcome to Spring Boot solr Example";
    }

}
