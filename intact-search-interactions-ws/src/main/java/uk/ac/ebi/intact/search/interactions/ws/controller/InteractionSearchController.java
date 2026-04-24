package uk.ac.ebi.intact.search.interactions.ws.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.parameters.InteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.service.ChildInteractorSearchService;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;
import uk.ac.ebi.intact.search.interactions.utils.NegativeFilterStatus;
import uk.ac.ebi.intact.search.interactions.ws.controller.model.ChildInteractorSearchResult;
import uk.ac.ebi.intact.search.interactions.ws.controller.model.GetInteractionsDatatablesRequest;
import uk.ac.ebi.intact.search.interactions.ws.controller.model.InteractionFacetsSearchResult;
import uk.ac.ebi.intact.search.interactions.ws.controller.model.InteractionSearchResult;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;

import static org.springframework.http.MediaType.*;

/**
 * @author Elisabet Barrera
 */

@CrossOrigin(origins = "*")
@RestController
public class InteractionSearchController {

    private final InteractionSearchService interactionSearchService;
    private final ChildInteractorSearchService childInteractorSearchService;

    @Autowired
    public InteractionSearchController(InteractionSearchService interactionSearchService,
                                       ChildInteractorSearchService childInteractorSearchService) {
        this.interactionSearchService = interactionSearchService;
        this.childInteractorSearchService = childInteractorSearchService;
    }

    @Hidden
    @CrossOrigin(origins = "*")
    @GetMapping("/")
    public RedirectView swagger() {
        return new RedirectView("swagger-ui.html");
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/findInteractions/{query}", produces = {APPLICATION_JSON_VALUE})
    public Page<SearchInteraction> findInteractions(
            @PathVariable String query,
            Pageable pageable) {
        return interactionSearchService.findInteractions(query, pageable);
    }

    @Deprecated
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/findInteractionFacets", produces = {APPLICATION_JSON_VALUE})
    public InteractionFacetsSearchResult findInteractionFacets(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "advancedSearch", required = false) boolean advancedSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypesFilter", required = false) Set<String> interactorTypesFilter,
            @RequestParam(value = "interactionDetectionMethodsFilter", required = false) Set<String> interactionDetectionMethodsFilter,
            @RequestParam(value = "participantDetectionMethodsFilter", required = false) Set<String> participantDetectionMethodsFilter,
            @RequestParam(value = "interactionTypesFilter", required = false) Set<String> interactionTypesFilter,
            @RequestParam(value = "interactionHostOrganismsFilter", required = false) Set<String> interactionHostOrganismsFilter,
            @RequestParam(value = "negativeFilter", required = false, defaultValue = "POSITIVE_ONLY") NegativeFilterStatus negativeFilter,
            @RequestParam(value = "mutationFilter", required = false) boolean mutationFilter,
            @RequestParam(value = "expansionFilter", required = false) boolean expansionFilter,
            @RequestParam(value = "minMIScore", defaultValue = "0", required = false) double minMIScore,
            @RequestParam(value = "maxMIScore", defaultValue = "1", required = false) double maxMIScore,
            @RequestParam(value = "intraSpeciesFilter", required = false) boolean intraSpeciesFilter,
            @RequestParam(value = "binaryInteractionIds", required = false) Set<Long> binaryInteractionIds,
            @RequestParam(value = "interactorAcs", required = false) Set<String> interactorAcs) {

        return this.findInteractionFacetsFromBody(
                InteractionSearchParameters.builder()
                        .query(query)
                        .batchSearch(batchSearch)
                        .advancedSearch(advancedSearch)
                        .interactorSpeciesFilter(interactorSpeciesFilter)
                        .interactorTypesFilter(interactorTypesFilter)
                        .interactionDetectionMethodsFilter(interactionDetectionMethodsFilter)
                        .participantDetectionMethodsFilter(participantDetectionMethodsFilter)
                        .interactionTypesFilter(interactionTypesFilter)
                        .interactionHostOrganismsFilter(interactionHostOrganismsFilter)
                        .negativeFilter(negativeFilter)
                        .mutationFilter(mutationFilter)
                        .expansionFilter(expansionFilter)
                        .minMIScore(minMIScore)
                        .maxMIScore(maxMIScore)
                        .intraSpeciesFilter(intraSpeciesFilter)
                        .binaryInteractionIds(binaryInteractionIds)
                        .interactorAcs(interactorAcs)
                        .build()
        );
    }

    @CrossOrigin(origins = "*")
    @PostMapping(
            value = "/findInteractionFacets/body",
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE}
    )
    public InteractionFacetsSearchResult findInteractionFacetsFromBody(@RequestBody InteractionSearchParameters parameters) {
        return new InteractionFacetsSearchResult(
                interactionSearchService.findInteractionFacets(parameters)
        );
    }

    @Deprecated
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/findInteractionWithFacet", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<String> findInteractionWithFacet(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "advancedSearch", required = false) boolean advancedSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypesFilter", required = false) Set<String> interactorTypesFilter,
            @RequestParam(value = "interactionDetectionMethodsFilter", required = false) Set<String> interactionDetectionMethodsFilter,
            @RequestParam(value = "participantDetectionMethodsFilter", required = false) Set<String> participantDetectionMethodsFilter,
            @RequestParam(value = "interactionTypesFilter", required = false) Set<String> interactionTypesFilter,
            @RequestParam(value = "interactionHostOrganismsFilter", required = false) Set<String> interactionHostOrganismsFilter,
            @RequestParam(value = "negativeFilter", required = false, defaultValue = "POSITIVE_ONLY") NegativeFilterStatus negativeFilter,
            @RequestParam(value = "mutationFilter", required = false) boolean mutationFilter,
            @RequestParam(value = "expansionFilter", required = false) boolean expansionFilter,
            @RequestParam(value = "minMIScore", defaultValue = "0", required = false) double minMIScore,
            @RequestParam(value = "maxMIScore", defaultValue = "1", required = false) double maxMIScore,
            @RequestParam(value = "intraSpeciesFilter", required = false) boolean intraSpeciesFilter,
            @RequestParam(value = "binaryInteractionIds", required = false) Set<Long> binaryInteractionIds,
            @RequestParam(value = "interactorAcs", required = false) Set<String> interactorAcs,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) throws IOException {

        return this.findInteractionWithFacetFromBody(
                PagedInteractionSearchParameters.builder()
                        .query(query)
                        .batchSearch(batchSearch)
                        .advancedSearch(advancedSearch)
                        .interactorSpeciesFilter(interactorSpeciesFilter)
                        .interactorTypesFilter(interactorTypesFilter)
                        .interactionDetectionMethodsFilter(interactionDetectionMethodsFilter)
                        .participantDetectionMethodsFilter(participantDetectionMethodsFilter)
                        .interactionTypesFilter(interactionTypesFilter)
                        .interactionHostOrganismsFilter(interactionHostOrganismsFilter)
                        .negativeFilter(negativeFilter)
                        .mutationFilter(mutationFilter)
                        .expansionFilter(expansionFilter)
                        .minMIScore(minMIScore)
                        .maxMIScore(maxMIScore)
                        .intraSpeciesFilter(intraSpeciesFilter)
                        .binaryInteractionIds(binaryInteractionIds)
                        .interactorAcs(interactorAcs)
                        .page(page)
                        .pageSize(pageSize)
                        .build()
        );
    }

    @CrossOrigin(origins = "*")
    @PostMapping(
            value = "/findInteractionWithFacet/body",
            produces = {APPLICATION_JSON_VALUE},
            consumes = {APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<String> findInteractionWithFacetFromBody(@RequestBody PagedInteractionSearchParameters parameters) throws IOException {
        InteractionSearchResult interactionSearchResult = new InteractionSearchResult(
                interactionSearchService.findInteractionWithFacet(parameters)
        );

        JSONObject result = new JSONObject();

        result.put("recordsTotal", interactionSearchResult.getTotalElements());
        result.put("recordsFiltered", interactionSearchResult.getTotalElements());

        StringWriter writer = new StringWriter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(writer, interactionSearchResult);

        result.put("data", writer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", APPLICATION_JSON_VALUE);
        headers.add("X-Clacks-Overhead", "headers");

        return new ResponseEntity<>(result.toString(), headers, HttpStatus.OK);
    }

    @Deprecated
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/list", produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getInteractionsDatatablesHandler(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "advancedSearch", required = false) boolean advancedSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypesFilter", required = false) Set<String> interactorTypesFilter,
            @RequestParam(value = "interactionDetectionMethodsFilter", required = false) Set<String> interactionDetectionMethodsFilter,
            @RequestParam(value = "participantDetectionMethodsFilter", required = false) Set<String> participantDetectionMethodsFilter,
            @RequestParam(value = "interactionTypesFilter", required = false) Set<String> interactionTypesFilter,
            @RequestParam(value = "interactionHostOrganismsFilter", required = false) Set<String> interactionHostOrganismsFilter,
            @RequestParam(value = "negativeFilter", required = false, defaultValue = "POSITIVE_ONLY") NegativeFilterStatus negativeFilter,
            @RequestParam(value = "mutationFilter", required = false) boolean mutationFilter,
            @RequestParam(value = "expansionFilter", required = false) boolean expansionFilter,
            @RequestParam(value = "minMIScore", defaultValue = "0", required = false) double minMIScore,
            @RequestParam(value = "maxMIScore", defaultValue = "1", required = false) double maxMIScore,
            @RequestParam(value = "intraSpeciesFilter", required = false) boolean intraSpeciesFilter,
            @RequestParam(value = "binaryInteractionIds", required = false) Set<Long> binaryInteractionIds,
            @RequestParam(value = "interactorAcs", required = false) Set<String> interactorAcs,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "draw") int draw) throws IOException {
        GetInteractionsDatatablesRequest parameters = GetInteractionsDatatablesRequest.builder()
                .query(query)
                .batchSearch(batchSearch)
                .advancedSearch(advancedSearch)
                .interactorSpeciesFilter(interactorSpeciesFilter)
                .interactorTypesFilter(interactorTypesFilter)
                .interactionDetectionMethodsFilter(interactionDetectionMethodsFilter)
                .participantDetectionMethodsFilter(participantDetectionMethodsFilter)
                .interactionTypesFilter(interactionTypesFilter)
                .interactionHostOrganismsFilter(interactionHostOrganismsFilter)
                .negativeFilter(negativeFilter)
                .mutationFilter(mutationFilter)
                .expansionFilter(expansionFilter)
                .minMIScore(minMIScore)
                .maxMIScore(maxMIScore)
                .intraSpeciesFilter(intraSpeciesFilter)
                .binaryInteractionIds(binaryInteractionIds)
                .interactorAcs(interactorAcs)
                .page(page)
                .pageSize(pageSize)
                .draw(draw)
                .build();

        return getInteractionsDatatablesHandlerFromBody(parameters);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/list/body", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getInteractionsDatatablesHandlerFromBody(
            @RequestBody GetInteractionsDatatablesRequest parameters
    ) throws IOException {
        FacetPage<SearchInteraction> searchInteraction = interactionSearchService.findInteractionWithFacet(
                parameters
        );

        InteractionSearchResult interactionSearchResult = new InteractionSearchResult(searchInteraction);

        JSONObject result = new JSONObject();
        result.put("draw", parameters.getDraw());
        result.put("recordsTotal", interactionSearchResult.getTotalElements());
        result.put("recordsFiltered", interactionSearchResult.getTotalElements());

        JSONArray data = new JSONArray();

        for (SearchInteraction interaction : interactionSearchResult.getContent()) {
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

    @Deprecated
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/interactors/list", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<String> getInteractorsDatatablesHandler(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "advancedSearch", required = false) boolean advancedSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypesFilter", required = false) Set<String> interactorTypesFilter,
            @RequestParam(value = "interactionDetectionMethodsFilter", required = false) Set<String> interactionDetectionMethodsFilter,
            @RequestParam(value = "participantDetectionMethodsFilter", required = false) Set<String> participantDetectionMethodsFilter,
            @RequestParam(value = "interactionTypesFilter", required = false) Set<String> interactionTypesFilter,
            @RequestParam(value = "interactionHostOrganismsFilter", required = false) Set<String> interactionHostOrganismsFilter,
            @RequestParam(value = "negativeFilter", required = false, defaultValue = "POSITIVE_ONLY") NegativeFilterStatus negativeFilter,
            @RequestParam(value = "mutationFilter", required = false) boolean mutationFilter,
            @RequestParam(value = "expansionFilter", required = false) boolean expansionFilter,
            @RequestParam(value = "minMIScore", defaultValue = "0", required = false) double minMIScore,
            @RequestParam(value = "maxMIScore", defaultValue = "1", required = false) double maxMIScore,
            @RequestParam(value = "intraSpeciesFilter", required = false) boolean intraSpeciesFilter,
            @RequestParam(value = "binaryInteractionIds", required = false) Set<Long> binaryInteractionIds,
            @RequestParam(value = "interactorAcs", required = false) Set<String> interactorAcs,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "draw", defaultValue = "1") int draw) throws IOException {


        GetInteractionsDatatablesRequest parameters = GetInteractionsDatatablesRequest.builder()
                .query(query)
                .batchSearch(batchSearch)
                .advancedSearch(advancedSearch)
                .interactorSpeciesFilter(interactorSpeciesFilter)
                .interactorTypesFilter(interactorTypesFilter)
                .interactionDetectionMethodsFilter(interactionDetectionMethodsFilter)
                .participantDetectionMethodsFilter(participantDetectionMethodsFilter)
                .interactionTypesFilter(interactionTypesFilter)
                .interactionHostOrganismsFilter(interactionHostOrganismsFilter)
                .negativeFilter(negativeFilter)
                .mutationFilter(mutationFilter)
                .expansionFilter(expansionFilter)
                .minMIScore(minMIScore)
                .maxMIScore(maxMIScore)
                .intraSpeciesFilter(intraSpeciesFilter)
                .binaryInteractionIds(binaryInteractionIds)
                .interactorAcs(interactorAcs)
                .page(page)
                .pageSize(pageSize)
                .draw(draw)
                .build();

        return getInteractorsDatatablesHandlerFromBody(parameters);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/interactors/list/body", produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
    private ResponseEntity<String> getInteractorsDatatablesHandlerFromBody(
            @RequestBody GetInteractionsDatatablesRequest parameters) throws IOException {
        GroupPage<SearchChildInteractor> searchInteractors = childInteractorSearchService.findInteractorsWithGroup(parameters);

        long numGroups = childInteractorSearchService.countInteractorsWithGroup(parameters);

        for (SearchChildInteractor searchInteractor : searchInteractors.getContent()) {
            Long interactionCount = interactionSearchService.countInteractionResult(searchInteractor.getInteractorAc(), parameters);
            searchInteractor.setInteractionSearchCount(interactionCount);
        }

        ChildInteractorSearchResult interactorSearchResult = new ChildInteractorSearchResult(searchInteractors, numGroups);

        JSONObject result = new JSONObject();
        result.put("draw", parameters.getDraw());
        result.put("recordsTotal", interactorSearchResult.getTotalElements());
        result.put("recordsFiltered", interactorSearchResult.getTotalElements());

        JSONArray data = new JSONArray();

        for (SearchChildInteractor interactor : interactorSearchResult.getContent()) {
            StringWriter writer = new StringWriter();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(writer, interactor);
            data.add(writer);
        }

        result.put("data", data);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", APPLICATION_JSON_VALUE);
        headers.add("X-Clacks-Overhead", "headers");

        return new ResponseEntity<>(result.toString(), headers, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/countInteractionResult",
            params = {
                    "query",
                    "interactorAc"
            }, produces = {APPLICATION_JSON_VALUE})
    public long countInteractionResult(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "interactorAc") String interactorAc,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "advancedSearch", required = false) boolean advancedSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypesFilter", required = false) Set<String> interactorTypesFilter,
            @RequestParam(value = "interactionDetectionMethodsFilter", required = false) Set<String> interactionDetectionMethodsFilter,
            @RequestParam(value = "participantDetectionMethodsFilter", required = false) Set<String> participantDetectionMethodsFilter,
            @RequestParam(value = "interactionTypesFilter", required = false) Set<String> interactionTypesFilter,
            @RequestParam(value = "interactionHostOrganismsFilter", required = false) Set<String> interactionHostOrganismsFilter,
            @RequestParam(value = "negativeFilter", required = false, defaultValue = "POSITIVE_ONLY") NegativeFilterStatus negativeFilter,
            @RequestParam(value = "mutationFilter", required = false) boolean mutationFilter,
            @RequestParam(value = "expansionFilter", required = false) boolean expansionFilter,
            @RequestParam(value = "minMIScore", defaultValue = "0", required = false) double minMIScore,
            @RequestParam(value = "maxMIScore", defaultValue = "1", required = false) double maxMIScore,
            @RequestParam(value = "intraSpeciesFilter", required = false) boolean intraSpeciesFilter) {
        InteractionSearchParameters parameters = InteractionSearchParameters.builder()
                .query(query)
                .batchSearch(batchSearch)
                .advancedSearch(advancedSearch)
                .interactorSpeciesFilter(interactorSpeciesFilter)
                .interactorTypesFilter(interactorTypesFilter)
                .interactionDetectionMethodsFilter(interactionDetectionMethodsFilter)
                .participantDetectionMethodsFilter(participantDetectionMethodsFilter)
                .interactionTypesFilter(interactionTypesFilter)
                .interactionHostOrganismsFilter(interactionHostOrganismsFilter)
                .negativeFilter(negativeFilter)
                .mutationFilter(mutationFilter)
                .expansionFilter(expansionFilter)
                .minMIScore(minMIScore)
                .maxMIScore(maxMIScore)
                .intraSpeciesFilter(intraSpeciesFilter)
                .build();
        return countInteractionResultsFromBody(interactorAc, parameters);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/countInteractionResult/{interactorAc}",
            produces = {APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_VALUE})
    public long countInteractionResultsFromBody(
            @PathVariable(value = "interactorAc") String interactorAc,
            @RequestBody InteractionSearchParameters parameters) {
        return interactionSearchService.countInteractionResult(interactorAc, parameters);
    }


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/countTotal", produces = {APPLICATION_JSON_VALUE})
    public long countTotal() {
        return interactionSearchService.countTotal();
    }


}