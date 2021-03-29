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
import uk.ac.ebi.intact.search.interactions.service.ChildInteractorSearchService;
import uk.ac.ebi.intact.search.interactions.service.InteractionSearchService;
import uk.ac.ebi.intact.search.interactions.ws.controller.model.ChildInteractorSearchResult;
import uk.ac.ebi.intact.search.interactions.ws.controller.model.InteractionSearchResult;

import java.io.*;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Elisabet Barrera
 */

@CrossOrigin(origins = "*")
@RestController
public class InteractionSearchController {

    //TODO temporary?
    public static final String UPLOADED_BATCH_FILE_PREFIX = "file_";
    private final InteractionSearchService interactionSearchService;
    private final ChildInteractorSearchService childInteractorSearchService;

    @Value("${server.upload.batch.file.path}")
    private String uploadBatchFilePath;

    @Autowired
    public InteractionSearchController(InteractionSearchService interactionSearchService,
                                       ChildInteractorSearchService childInteractorSearchService) {
        this.interactionSearchService = interactionSearchService;
        this.childInteractorSearchService = childInteractorSearchService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/findInteractions/{query}", produces = {APPLICATION_JSON_VALUE})
    public Page<SearchInteraction> findInteractions(@PathVariable String query) {
        return interactionSearchService.findInteractions(query);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/findInteractionWithFacet",
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<String> findInteractionWithFacet(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypesFilter", required = false) Set<String> interactorTypesFilter,
            @RequestParam(value = "interactionDetectionMethodsFilter", required = false) Set<String> interactionDetectionMethodsFilter,
            @RequestParam(value = "interactionTypesFilter", required = false) Set<String> interactionTypesFilter,
            @RequestParam(value = "interactionHostOrganismsFilter", required = false) Set<String> interactionHostOrganismsFilter,
            @RequestParam(value = "negativeFilter", required = false) boolean negativeFilter,
            @RequestParam(value = "mutationFilter", required = false) boolean mutationFilter,
            @RequestParam(value = "minMIScore", defaultValue = "0", required = false) double minMIScore,
            @RequestParam(value = "maxMIScore", defaultValue = "1", required = false) double maxMIScore,
            @RequestParam(value = "intraSpeciesFilter", required = false) boolean intraSpeciesFilter,
            @RequestParam(value = "binaryInteractionIds", required = false) Set<Integer> binaryInteractionIds,
            @RequestParam(value = "interactorAcs", required = false) Set<String> interactorAcs,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) throws IOException {

        InteractionSearchResult interactionSearchResult = new InteractionSearchResult(interactionSearchService.findInteractionWithFacet(
                extractSearchTerms(query),
                batchSearch,
                interactorSpeciesFilter,
                interactorTypesFilter,
                interactionDetectionMethodsFilter,
                interactionTypesFilter,
                interactionHostOrganismsFilter,
                negativeFilter,
                mutationFilter,
                minMIScore,
                maxMIScore,
                intraSpeciesFilter,
                binaryInteractionIds,
                interactorAcs,
                page,
                pageSize));

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

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/list",
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getInteractionsDatatablesHandler(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypesFilter", required = false) Set<String> interactorTypesFilter,
            @RequestParam(value = "interactionDetectionMethodsFilter", required = false) Set<String> interactionDetectionMethodsFilter,
            @RequestParam(value = "interactionTypesFilter", required = false) Set<String> interactionTypesFilter,
            @RequestParam(value = "interactionHostOrganismsFilter", required = false) Set<String> interactionHostOrganismsFilter,
            @RequestParam(value = "negativeFilter", required = false) boolean negativeFilter,
            @RequestParam(value = "mutationFilter", required = false) boolean mutationFilter,
            @RequestParam(value = "minMIScore", defaultValue = "0", required = false) double minMIScore,
            @RequestParam(value = "maxMIScore", defaultValue = "1", required = false) double maxMIScore,
            @RequestParam(value = "intraSpeciesFilter", required = false) boolean intraSpeciesFilter,
            @RequestParam(value = "binaryInteractionIds", required = false) Set<Integer> binaryInteractionIds,
            @RequestParam(value = "interactorAcs", required = false) Set<String> interactorAcs,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "draw") int draw) throws IOException {
        FacetPage<SearchInteraction> searchInteraction = interactionSearchService.findInteractionWithFacet(
                extractSearchTerms(query),
                batchSearch,
                interactorSpeciesFilter,
                interactorTypesFilter,
                interactionDetectionMethodsFilter,
                interactionTypesFilter,
                interactionHostOrganismsFilter,
                negativeFilter,
                mutationFilter, minMIScore,
                maxMIScore,
                intraSpeciesFilter,
                binaryInteractionIds,
                interactorAcs,
                page,
                pageSize);

        InteractionSearchResult interactionSearchResult = new InteractionSearchResult(searchInteraction);

        JSONObject result = new JSONObject();
        result.put("draw", draw);
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
    @PostMapping(value = "/interactors/list",
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getInteractorsDatatablesHandler(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypesFilter", required = false) Set<String> interactorTypesFilter,
            @RequestParam(value = "interactionDetectionMethodsFilter", required = false) Set<String> interactionDetectionMethodsFilter,
            @RequestParam(value = "interactionTypesFilter", required = false) Set<String> interactionTypesFilter,
            @RequestParam(value = "interactionHostOrganismsFilter", required = false) Set<String> interactionHostOrganismsFilter,
            @RequestParam(value = "negativeFilter", required = false) boolean negativeFilter,
            @RequestParam(value = "mutationFilter", required = false) boolean mutationFilter,
            @RequestParam(value = "minMIScore", defaultValue = "0", required = false) double minMIScore,
            @RequestParam(value = "maxMIScore", defaultValue = "1", required = false) double maxMIScore,
            @RequestParam(value = "intraSpeciesFilter", required = false) boolean intraSpeciesFilter,
            @RequestParam(value = "binaryInteractionIds", required = false) Set<Integer> binaryInteractionId,
            @RequestParam(value = "interactorAcs", required = false) Set<String> interactorAc,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "draw") int draw) throws IOException {


        GroupPage<SearchChildInteractor> searchInteractors = childInteractorSearchService.findInteractorsWithGroup(
                extractSearchTerms(query),
                batchSearch,
                interactorSpeciesFilter,
                interactorTypesFilter,
                interactionDetectionMethodsFilter,
                interactionTypesFilter,
                interactionHostOrganismsFilter,
                negativeFilter,
                mutationFilter, minMIScore,
                maxMIScore,
                intraSpeciesFilter,
                binaryInteractionId,
                interactorAc,
                page,
                pageSize);

        long numGroups = childInteractorSearchService.countInteractorsWithGroup(
                extractSearchTerms(query),
                batchSearch,
                interactorSpeciesFilter,
                interactorTypesFilter,
                interactionDetectionMethodsFilter,
                interactionTypesFilter,
                interactionHostOrganismsFilter,
                negativeFilter,
                mutationFilter, minMIScore,
                maxMIScore,
                intraSpeciesFilter,
                binaryInteractionId,
                interactorAc);

        for (SearchChildInteractor searchInteractor : searchInteractors.getContent()) {

            Long interactionCount = interactionSearchService.countInteractionResult(
                    extractSearchTerms(query),
                    batchSearch,
                    searchInteractor.getInteractorAc(),
                    interactorSpeciesFilter,
                    interactorTypesFilter,
                    interactionDetectionMethodsFilter,
                    interactionTypesFilter,
                    interactionHostOrganismsFilter,
                    negativeFilter,
                    mutationFilter, minMIScore,
                    maxMIScore,
                    intraSpeciesFilter,
                    binaryInteractionId,
                    interactorAc);

            searchInteractor.setInteractionSearchCount(interactionCount);
        }

        ChildInteractorSearchResult interactorSearchResult = new ChildInteractorSearchResult(searchInteractors, numGroups);

        JSONObject result = new JSONObject();
        result.put("draw", draw);
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
            },
            produces = {APPLICATION_JSON_VALUE})
    public long countInteractionResult(
            @RequestParam(value = "query") String query,
            @RequestParam(value = "batchSearch", required = false) boolean batchSearch,
            @RequestParam(value = "interactorAc") String interactorAc,
            @RequestParam(value = "interactorSpeciesFilter", required = false) Set<String> interactorSpeciesFilter,
            @RequestParam(value = "interactorTypesFilter", required = false) Set<String> interactorTypesFilter,
            @RequestParam(value = "interactionDetectionMethodsFilter", required = false) Set<String> interactionDetectionMethodsFilter,
            @RequestParam(value = "interactionTypesFilter", required = false) Set<String> interactionTypesFilter,
            @RequestParam(value = "interactionHostOrganismsFilter", required = false) Set<String> interactionHostOrganismsFilter,
            @RequestParam(value = "negativeFilter", required = false) boolean negativeFilter,
            @RequestParam(value = "mutationFilter", required = false) boolean mutationFilter,
            @RequestParam(value = "minMIScore", defaultValue = "0", required = false) double minMIScore,
            @RequestParam(value = "maxMIScore", defaultValue = "1", required = false) double maxMIScore,
            @RequestParam(value = "intraSpeciesFilter", required = false) boolean intraSpeciesFilter) {

        return interactionSearchService.countInteractionResult(
                extractSearchTerms(query),
                batchSearch,
                interactorAc,
                interactorSpeciesFilter,
                interactorTypesFilter,
                interactionDetectionMethodsFilter,
                interactionTypesFilter,
                interactionHostOrganismsFilter,
                negativeFilter,
                mutationFilter, minMIScore,
                maxMIScore,
                intraSpeciesFilter,
                null,
                null);
    }


    @PostMapping(value = "/uploadFile",
            produces = {APPLICATION_JSON_VALUE})
    public ResponseEntity<String> uploadBatchFile(
            @RequestParam(value = "file", required = true) MultipartFile file) {
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
                uploadBatchFileName = UPLOADED_BATCH_FILE_PREFIX + file.hashCode();
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

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", APPLICATION_JSON_VALUE);
        headers.add("X-Clacks-Overhead", "headers");

        return new ResponseEntity<String>(uploadBatchFileName, headers, httpStatus);
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/countTotal",
            produces = {APPLICATION_JSON_VALUE})
    public long countTotal() {
        return interactionSearchService.countTotal();
    }

    private String extractSearchTerms(String query) {

        StringBuilder searchTerms = new StringBuilder();

        if (query.startsWith(UPLOADED_BATCH_FILE_PREFIX)) {
            File uploadedBatchFile = new File(uploadBatchFilePath + File.separator + query);
            if (uploadedBatchFile.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(uploadedBatchFile));
                    String line;
                    int count = 0;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (count > 0) {
                            searchTerms.append(",").append(line);
                        } else {
                            searchTerms = new StringBuilder(line);
                        }
                        count++;
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        } else {
            searchTerms = new StringBuilder(query);
        }

        return searchTerms.toString();
    }

}
