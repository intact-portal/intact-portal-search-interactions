package uk.ac.ebi.intact.search.interactions.ws.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.ChildIInteractorSearchService;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;
import uk.ac.ebi.intact.search.interactions.utils.Constants;
import uk.ac.ebi.intact.search.interactions.ws.controller.model.ChildInteractorSearchResult;
import uk.ac.ebi.intact.search.interactions.ws.controller.model.InteractionSearchResult;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Elisabet Barrera
 */

@RestController
public class InteractionSearchController {

    @Value("${server.upload.batch.file.path}")
    private String uploadBatchFilePath;
    private InteractionSearchService interactionSearchService;
    private ChildIInteractorSearchService childIInteractorSearchService;

    @Autowired
    public InteractionSearchController(InteractionSearchService interactionSearchService, ChildIInteractorSearchService childIInteractorSearchService) {
        this.interactionSearchService = interactionSearchService;
        this.childIInteractorSearchService = childIInteractorSearchService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/findInteractions/{query}", produces = {APPLICATION_JSON_VALUE})
    public Page<SearchInteraction> findInteractions(@PathVariable String query) {
        return interactionSearchService.findInteractions(query);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/findInteractionWithFacet",
            params = {
                    "query",
                    "page",
                    "pageSize"
            },
            produces = {APPLICATION_JSON_VALUE})
    public InteractionSearchResult findInteractionWithFacet(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypeFilter", required = false) Set<String> interactorTypeFilter,
            @RequestParam(value = "interactionDetectionMethodFilter", required = false) Set<String> interactionDetectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "interactionHostOrganismFilter", required = false) Set<String> interactionHostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiscore", defaultValue = "0", required = false) double minMiscore,
            @RequestParam(value = "maxMiscore", defaultValue = "1", required = false) double maxMiscore,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        return new InteractionSearchResult(interactionSearchService.findInteractionWithFacet(
                query, batchSearch, interactorSpeciesFilter,
                interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter,
                isNegativeFilter,
                minMiscore,
                maxMiscore,
                interSpecies,
                page,
                pageSize));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/findChildInteractors",
            params = {
                    "query",
                    "page",
                    "pageSize"
            },
            produces = {APPLICATION_JSON_VALUE})
    public ChildInteractorSearchResult findChildInteractors(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypeFilter", required = false) Set<String> interactorTypeFilter,
            @RequestParam(value = "interactionDetectionMethodFilter", required = false) Set<String> interactionDetectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "interactionHostOrganismFilter", required = false) Set<String> interactionHostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiscore", defaultValue = "0", required = false) double minMiscore,
            @RequestParam(value = "maxMiscore", defaultValue = "1", required = false) double maxMiscore,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        GroupPage<SearchChildInteractor> childInteractorResult = childIInteractorSearchService.findInteractorsWithGroup(
                query, batchSearch, interactorSpeciesFilter,
                interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter,
                isNegativeFilter,
                minMiscore,
                maxMiscore,
                interSpecies,
                page,
                pageSize);

        for (SearchChildInteractor searchChildInteractor : childInteractorResult.getContent()) {

            /* TODO: Pass the interSpecies from the top, it can change the result of the query */
            Long interactionCount = interactionSearchService.countInteractionResult(query, batchSearch, searchChildInteractor.getInteractorAc(), interactorSpeciesFilter,
                    interactorTypeFilter, interactionDetectionMethodFilter, interactionTypeFilter, interactionHostOrganismFilter,
                    isNegativeFilter, minMiscore, maxMiscore, false);

            searchChildInteractor.setInteractionSearchCount(interactionCount);
        }

        return new ChildInteractorSearchResult(childInteractorResult);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/countInteractionResult",
            params = {
                    "query",
                    "interactorAc"
            },
            produces = {APPLICATION_JSON_VALUE})
    public long countInteractionResult(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "interactorAc") String interactorAc,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypeFilter", required = false) Set<String> interactorTypeFilter,
            @RequestParam(value = "interactionDetectionMethodFilter", required = false) Set<String> interactionDetectionMethodFilter,
            @RequestParam(value = "interactionTypeFilter", required = false) Set<String> interactionTypeFilter,
            @RequestParam(value = "interactionHostOrganismFilter", required = false) Set<String> interactionHostOrganismFilter,
            @RequestParam(value = "isNegativeFilter", required = false) boolean isNegativeFilter,
            @RequestParam(value = "minMiscore", defaultValue = "0", required = false) double minMiscore,
            @RequestParam(value = "maxMiscore", defaultValue = "1", required = false) double maxMiscore,
            @RequestParam(value = "interSpecies", required = false) boolean interSpecies) {

        return interactionSearchService.countInteractionResult(
                query,
                batchSearch,
                interactorAc,
                interactorSpeciesFilter,
                interactorTypeFilter,
                interactionDetectionMethodFilter,
                interactionTypeFilter,
                interactionHostOrganismFilter,
                isNegativeFilter,
                minMiscore,
                maxMiscore,
                interSpecies);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/datatables/{query}",
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getInteractionsDatatablesHandler(@PathVariable String query,
                                                                   @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
                                                                   HttpServletRequest request) throws IOException {
        Set<String> interactorTypeFilter = new HashSet<>();
        Set<String> interactorSpeciesFilter = new HashSet<>();
        Set<String> interactionTypeFilter = new HashSet<>();
        Set<String> interactionDetectionMethodFilter = new HashSet<>();
        Set<String> interactionHostOrganismFilter = new HashSet<>();

        int page = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));

        if (request.getParameterValues("interactorType[]") != null) {
            interactorTypeFilter = new HashSet<>(Arrays.asList(request.getParameterValues("interactorType[]")));
        }
        if (request.getParameterValues("interactorSpecies[]") != null) {
            interactorSpeciesFilter = new HashSet<>(Arrays.asList(request.getParameterValues("interactorSpecies[]")));
        }
        if (request.getParameterValues("interactionType[]") != null) {
            interactionTypeFilter = new HashSet<>(Arrays.asList(request.getParameterValues("interactionType[]")));
        }
        if (request.getParameterValues("interactionDetectionMethod[]") != null) {
            interactionDetectionMethodFilter = new HashSet<>(Arrays.asList(request.getParameterValues("interactionDetectionMethod[]")));
        }
        if (request.getParameterValues("interactionsHostOrganism[]") != null) {
            interactionHostOrganismFilter = new HashSet<>(Arrays.asList(request.getParameterValues("interactionsHostOrganism[]")));
        }
        boolean negativeFilter = Boolean.parseBoolean(request.getParameter("negativeInteraction"));
        double minMiScoreFilter = Double.parseDouble(request.getParameter("miScoreMin"));
        double maxMiScoreFilter = Double.parseDouble(request.getParameter("miScoreMax"));

        FacetPage<SearchInteraction> searchInteraction = interactionSearchService.findInteractionWithFacet(query, batchSearch, interactorSpeciesFilter,
                interactorTypeFilter, interactionDetectionMethodFilter, interactionTypeFilter, interactionHostOrganismFilter, negativeFilter, minMiScoreFilter, maxMiScoreFilter,
                false, page, pageSize);

        InteractionSearchResult interactionSearchResult = new InteractionSearchResult(searchInteraction);

        JSONObject result = new JSONObject();
        result.put("draw", request.getParameter("draw"));
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

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/countTotal",
            produces = {APPLICATION_JSON_VALUE})
    public long countTotal() {
        return interactionSearchService.countTotal();
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/uploadBatchFile",
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<String> uploadBatchFile(@RequestParam(value = "file", required = true) MultipartFile file) {
        String rootPath = uploadBatchFilePath;
        String uploadBatchFileName = null;
        HttpStatus httpStatus = HttpStatus.OK;
        if (file != null && !file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                // Creating the directory to store file
                File dir = new File(rootPath);
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                uploadBatchFileName = Constants.UPLOADED_BATCH_FILE_PREFIX + this.hashCode();
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + uploadBatchFileName);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
            } catch (IOException ioe) {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                ioe.printStackTrace();
            }
        } else {
            httpStatus = HttpStatus.EXPECTATION_FAILED;
        }

        JSONObject result = new JSONObject();
        result.put("data", uploadBatchFileName);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", APPLICATION_JSON_VALUE);
        headers.add("X-Clacks-Overhead", "headers");

        return new ResponseEntity<String>(result.toString(), headers, httpStatus);
    }

}
