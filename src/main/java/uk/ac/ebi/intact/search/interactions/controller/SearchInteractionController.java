package uk.ac.ebi.intact.search.interactions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.InteractionIndexService;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
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

    @PostMapping(value = "/interaction/datatables/{query}",
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getInteractionsDatatablesHandler(@PathVariable String query,
                                                                   HttpServletRequest request) throws IOException {
        Set<String> interactorTypeFilter = new HashSet<>();
        Set<String> speciesFilter = new HashSet<>();
        Set<String> interactionTypeFilter = new HashSet<>();
        Set<String> detectionMethodFilter = new HashSet<>();
        Set<String> hostOrganismFilter = new HashSet<>();

        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        if (request.getParameterValues("interactorType") != null) {
            interactorTypeFilter = new HashSet<>(Arrays.asList(request.getParameterValues("interactorType[]")));
        }
        if (request.getParameterValues("species") != null) {
            speciesFilter = new HashSet<>(Arrays.asList(request.getParameterValues("species[]")));
        }
        if (request.getParameterValues("interactionType") != null) {
            interactionTypeFilter = new HashSet<>(Arrays.asList(request.getParameterValues("interactionType[]")));
        }
        if (request.getParameterValues("detectionMethod") != null) {
            detectionMethodFilter = new HashSet<>(Arrays.asList(request.getParameterValues("detectionMethod[]")));
        }
        if (request.getParameterValues("hostOrganism") != null) {
            hostOrganismFilter = new HashSet<>(Arrays.asList(request.getParameterValues("hostOrganism[]")));
        }
        boolean negativeFilter = Boolean.parseBoolean(request.getParameter("negativeInteraction"));
        double minMiScoreFilter = Double.parseDouble(request.getParameter("miScoreMin"));
        double maxMiScoreFilter = Double.parseDouble(request.getParameter("miScoreMax"));

        FacetPage<SearchInteraction> searchInteraction = interactionSearchService.findInteractionWithFacet(query,
                detectionMethodFilter, interactionTypeFilter, hostOrganismFilter, negativeFilter, minMiScoreFilter, maxMiScoreFilter,
                speciesFilter, false, page, pageSize);

        SearchInteractionResult searchInteractionResult = new SearchInteractionResult(searchInteraction);

        JSONObject result = new JSONObject();
        result.put("draw", request.getParameter("draw"));
        result.put("recordsTotal", searchInteractionResult.getTotalElements());
        result.put("recordsFiltered", searchInteractionResult.getTotalElements());

        JSONArray data = new JSONArray();

        for (SearchInteraction interaction : searchInteractionResult.getContent()) {
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, interaction);
            data.add(writer);
        }

        result.put("data", data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", APPLICATION_JSON_VALUE);
        headers.add("X-Clacks-Overhead", "headers");

        return new ResponseEntity<>(result.toString(), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/interaction/countTotal",
            produces = {APPLICATION_JSON_VALUE})
    public long countTotal() {
        return interactionSearchService.countTotal();
    }

}
