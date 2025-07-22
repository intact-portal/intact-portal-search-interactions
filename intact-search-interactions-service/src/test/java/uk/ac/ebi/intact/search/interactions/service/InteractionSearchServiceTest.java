package uk.ac.ebi.intact.search.interactions.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.FacetQueryEntry;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.parameters.SimpleInteractionQueryParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.service.util.TestUtil;
import uk.ac.ebi.intact.search.interactions.utils.NegativeFilterStatus;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.Assert.*;
import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

/**
 * @author Elisabet Barrera
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InteractionSearchServiceTest {

    @Resource
    private InteractionIndexService interactionIndexService;

    @Resource
    private InteractionSearchService interactionSearchService;

    /**
     * Before any tests run, this cleans the solr index and creates a new index with stored searchInteractions in an xml
     */
    @Before
    public void setUp() {
        //Delete all documents from solr core
        interactionIndexService.deleteAll();
        /*Interactions are instantiated from saved searchInteractions in an xml as instantiating it one by one in the code is cumbersome
         * For ref. The Interactions.xml can be created with a method saveInteractioninDisc in CommonUtility in intact-portal-indexer*/
        Collection<SearchInteraction> searchInteractions = TestUtil.getInteractionObjFromXml("./src/test/resources/Interactions.xml");
        interactionIndexService.save(searchInteractions);
        assertEquals(10, interactionSearchService.countTotal());
    }

    @After
    public void tearDown() {
        interactionIndexService.deleteAll();
    }

    /**
     * Behaviour If the User types "Author name" in search box
     * includes min gram size check
     */
    @Test
    public void findByAuthor() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("\"Shorter J\"");
        assertEquals(4, interactionPage.getTotalElements());
        assertEquals(4, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "Species Name" in search box
     * TODO...Depending on whether we index species in future, we include/exclude this
     */
    @Ignore
    public void findBySpecies() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("Rattus norvegicus (Rat)");
        assertEquals(4, interactionPage.getTotalElements());
        assertEquals(4, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Type" in search box
     */
    @Test
    public void findByInteractionType() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("\"physical association\"");
        assertEquals(10, interactionPage.getTotalElements());
        assertEquals(10, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Type MI identifier" in search box
     */
    @Test
    public void findByInteractionTypeMIIdentifier() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("MI:0915");
        assertEquals(10, interactionPage.getTotalElements());
        assertEquals(10, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interaction identifier" in search box
     */
    @Test
    public void findByInteractionIdentifier() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("EBI-10000862");
        assertEquals(1, interactionPage.getTotalElements());
        assertEquals(1, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Detection Method" in search box
     */
    @Test
    public void findByIntDetMethod() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("\"molecular sieving\"");
        assertEquals(1, interactionPage.getTotalElements());
        assertEquals(1, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Participant Detection Method" in search box
     */
    @Test
    public void findByParticipantDetMethod() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("predetermined");
        assertEquals(5, interactionPage.getTotalElements());
        assertEquals(5, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interactor Xref" in search box
     * includes identifier test
     */
    @Test
    public void findByInteractorXref() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("IPR023601");
        assertEquals(1, interactionPage.getTotalElements());
        assertEquals(1, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interactor Intact name" in search box
     */
    @Test
    public void findByInteractorIntactName() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("bet1l_rat");
        assertEquals(4, interactionPage.getTotalElements());
        assertEquals(4, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interactor Full name" in search box
     */
    @Test
    public void findByInteractorFullName() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("\"Transcription factor p65\"");
        assertEquals(1, interactionPage.getTotalElements());
        assertEquals(1, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interaction Xref" in search box
     */
    @Test
    public void findByInteractionXref() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("IM-23550-1");
        assertEquals(4, interactionPage.getTotalElements());
        assertEquals(4, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interactor Aliases" in search box
     */
    @Test
    public void findByInteractorAliases() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("NFKB3");
        assertEquals(1, interactionPage.getTotalElements());
        assertEquals(1, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "Host Organism" in search box
     * TODO...Depending on whether we index host organism in future, we include/exclude this
     */
    @Ignore
    public void findByHostOrganism() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("\"In vitro\"");
        assertEquals(6, interactionPage.getTotalElements());
        assertEquals(6, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Feature ShortLabel" in search box
     * includes preserve original index Test
     */
    @Test
    public void findByfeatureShortLabel() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("c-terminal");
        assertEquals(5, interactionPage.getTotalElements());
        assertEquals(5, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Feature Type" in search box
     */
    @Test
    public void findByfeatureType() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("\"gst tag\"");
        assertEquals(3, interactionPage.getTotalElements());
        assertEquals(3, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Publication Identifier" in search box
     */
    @Test
    public void findByPublicationIdentifier() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("15961401");
        assertEquals(5, interactionPage.getTotalElements());
        assertEquals(5, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "middle chunk of a word with delimiter" in search box
     */
    @Test
    public void edgeNGramsTest1() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("F-kap");
        assertEquals(0, interactionPage.getTotalElements());
        assertEquals(0, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "middle chunk of a word "in search box
     */
    @Test
    public void edgeNGramsTest2() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("FKB3");
        assertEquals(0, interactionPage.getTotalElements());
        assertEquals(0, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "word separated by delimiter" in search box
     */
    @Test
    public void wordPartIndexTimeGeneration() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("kappaB");
        assertEquals(1, interactionPage.getTotalElements());
        assertEquals(1, interactionPage.getNumberOfElements());
    }

    /**
     * Behaviour If the User types "concatenation after removing delimiter" in search box
     */
    @Test
    public void concatenationOfDelimitedTerm() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractions("NFkappaB");
        assertEquals(1, interactionPage.getTotalElements());
        assertEquals(1, interactionPage.getNumberOfElements());
    }

    /**
     * Expected Facet Results If the User types "physical association" and Hit Search Button
     */
    @Test
    public void facetTest() {
        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionFacets(
                PagedInteractionSearchParameters.builder()
                        .query("physical association")
                        .build()
        );

        // page checks, for this method the interaction content should be ignored
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(1, interactionFacetPage.getContent().size());
        assertEquals(1, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(1, interactionFacetPage.getPageable().getPageSize());
        // The total number reflects a different number than the real one.
        // Seems to be a bug in the spring page when the request size is lower than 10
        assertEquals(10, interactionFacetPage.getTotalElements());

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 6L);
        specieFacetsExpected.put("10116__Rattus norvegicus (Rat)__#5688c0", 4L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0319__dna__VEE", 1L);
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("anti bait coip", 4L);
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0006__anti bait coip", 4L);
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 5L);
        identificationMethodsFacetsExpected.put("MI:0113__western blot", 4L);
        identificationMethodsFacetsExpected.put("MI:0981__tag perox activity", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(3, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 6L);
        hostOrganismFacetsExpected.put("10116__rattus norvegicus liver__#5688c0", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 10L);
        negativeFacetsExpected.put("true", 0L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 5L);
        miScoreFacetsExpected.put("0.53", 1L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.69", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 3L);
        expansionMethodExpected.put("expansion_false", 7L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /**
     * Retrieves the interactiono identifiers  (acs and binary ids) used in the export
     */
    @Test
    public void findInteractionIdentifiersTest() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractionIdentifiers(
                PagedInteractionSearchParameters.builder().query("In vitro").build()
        );

        // page checks
        assertFalse(interactionPage.getContent().isEmpty());
        assertEquals(6, interactionPage.getContent().size());
        assertEquals(6, interactionPage.getNumberOfElements());
        assertEquals(0, interactionPage.getPageable().getPageNumber());
        assertEquals(10, interactionPage.getPageable().getPageSize());
        assertEquals(6, interactionPage.getTotalElements());

        Set<String> acs = new TreeSet<>();
        Set<String> expectedAcs = new TreeSet<>(Arrays.asList("EBI-1000008", "EBI-1000026", "EBI-1000048", "EBI-10000862"));

        for (SearchInteraction searchInteraction : interactionPage) {
            acs.add(searchInteraction.getAc());
        }
        assertEquals(expectedAcs, acs);

        Set<Long> binaryIds = new TreeSet<>();
        Set<Long> expectedBinaryIds = new TreeSet<>(Arrays.asList(1L, 2L, 3L, 4L, 5L, 10L));

        for (SearchInteraction searchInteraction : interactionPage) {
            binaryIds.add(searchInteraction.getBinaryInteractionId());
        }

        assertEquals(binaryIds, expectedBinaryIds);

    }

    /**
     * Expected behaviour when filter elements are passed
     */
    @Test
    public void filterInterSpeciesFalseOneSpecies() {

        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("physical association")
                        .interactorSpeciesFilter(Set.of("Homo sapiens__9606"))
                        .interactionTypesFilter(Set.of("physical association"))
                        .interactionDetectionMethodsFilter(Set.of("molecular sieving"))
                        .interactorTypesFilter(Set.of("protein"))
                        .interactionHostOrganismsFilter(Set.of("In vitro__-1"))
                        .minMIScore(0)
                        .maxMIScore(0.7)
                        .build()
        );

        // page checks, for this method the interaction content should be ignored
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(1, interactionFacetPage.getContent().size());
        assertEquals(1, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage.getTotalElements());

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 1L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(4, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(4, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.64", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 1L);
        expansionMethodExpected.put("expansion_false", 0L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /**
     * Expected behaviour when filter elements are passed
     */
    @Test
    public void filterInterSpeciesTrueTwoSpecies() {

        //filter2

        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("physical association")
                        .interactorSpeciesFilter(Set.of("Homo sapiens__9606", "Rattus norvegicus (Rat)__10116"))
                        .interactorTypesFilter(Set.of("protein"))
                        .interactionDetectionMethodsFilter(Set.of("molecular sieving"))
                        .interactionTypesFilter(Set.of("physical association"))
                        .interactionHostOrganismsFilter(Set.of("In vitro__-1"))
                        .minMIScore(0)
                        .maxMIScore(0.6)
                        .intraSpeciesFilter(true)
                        .build()
        );

        // page checks
        assertTrue(interactionFacetPage.getContent().isEmpty());
        assertEquals(0, interactionFacetPage.getContent().size());
        assertEquals(0, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(0, interactionFacetPage.getTotalElements());

        //species facet checking
        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(0, facetFieldEntryPage.getTotalElements());

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(0, facetFieldEntryPage.getTotalElements());

        //interactor type facet checking
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(0, facetFieldEntryPage.getTotalElements());


        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("density sedimentation", 1L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(3, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(3, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(0, facetFieldEntryPage.getTotalElements());

        //interaction type facet checking
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(0, facetFieldEntryPage.getTotalElements());

        //host organism facet checking
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(0, facetFieldEntryPage.getTotalElements());

        // negative facet checking
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(0, facetFieldEntryPage.getTotalElements());

        // affected by mutation
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(0, facetFieldEntryPage.getTotalElements());


        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.64", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 0L);
        expansionMethodExpected.put("expansion_false", 0L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /**
     * Behaviour If the User types "Species Name" in search box and expects faceting in results page
     */
    @Test
    public void findInteractionsBySpeciesWithFacet() {

        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder().query("rat").build()
        );

        // page checks, for this method the interaction content should be ignored
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(4, interactionFacetPage.getContent().size());
        assertEquals(4, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(4, interactionFacetPage.getTotalElements());

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("10116__Rattus norvegicus (Rat)__#5688c0", 4L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("anti bait coip", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0006__anti bait coip", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0113__western blot", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("10116__rattus norvegicus liver__#5688c0", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 0L);
        expansionMethodExpected.put("expansion_false", 4L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }

    }

    /*
     * Expected interactions when queried by a interactor field
     **/

    @Test
    public void findInteractionsByInteractorIndexedField() {

        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("kappaB")
                        .build()
        );

        // page checks, for this method the interaction content should be ignored
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(1, interactionFacetPage.getContent().size());
        assertEquals(1, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage.getTotalElements());

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 1L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 1L);
        interactorTypeFacetsExpected.put("MI:0319__dna__VEE", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("elisa", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0981__tag perox activity", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 1L);
        expansionMethodExpected.put("expansion_false", 0L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions when queried by empty string
     **/

    @Test
    public void findInteractionsByEmptyString() {

        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("")
                        .build()
        );

        // page checks
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(10, interactionFacetPage.getContent().size());
        assertEquals(10, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(10, interactionFacetPage.getTotalElements());

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 6L);
        specieFacetsExpected.put("10116__Rattus norvegicus (Rat)__#5688c0", 4L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0319__dna__VEE", 1L);
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("anti bait coip", 4L);
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0006__anti bait coip", 4L);
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 5L);
        identificationMethodsFacetsExpected.put("MI:0113__western blot", 4L);
        identificationMethodsFacetsExpected.put("MI:0981__tag perox activity", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(3, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 6L);
        hostOrganismFacetsExpected.put("10116__rattus norvegicus liver__#5688c0", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 5L);
        miScoreFacetsExpected.put("0.53", 1L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.69", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 3L);
        expansionMethodExpected.put("expansion_false", 7L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions when queried by "*" character
     **/

    @Test
    public void findInteractionsByStarString() {

        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("*")
                        .build()
        );

        // page checks
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(10, interactionFacetPage.getContent().size());
        assertEquals(10, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(10, interactionFacetPage.getTotalElements());

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 6L);
        specieFacetsExpected.put("10116__Rattus norvegicus (Rat)__#5688c0", 4L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0319__dna__VEE", 1L);
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("anti bait coip", 4L);
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0006__anti bait coip", 4L);
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 5L);
        identificationMethodsFacetsExpected.put("MI:0113__western blot", 4L);
        identificationMethodsFacetsExpected.put("MI:0981__tag perox activity", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(3, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 6L);
        hostOrganismFacetsExpected.put("10116__rattus norvegicus liver__#5688c0", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 5L);
        miScoreFacetsExpected.put("0.53", 1L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.69", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 3L);
        expansionMethodExpected.put("expansion_false", 7L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions when queried by "list of interactor acs" for batch search
     **/
    @Test
    public void findInteractionsByCommaSeparatedInteractorAcsString() {

        // check https://issues.apache.org/jira/browse/SOLR-12858 for embedded POST request issue
        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("EBI-715849,EBI-724102")
                        .batchSearch(true)
                        .build()
        );

        // page checks, for this method the interaction content should be ignored
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(4, interactionFacetPage.getContent().size());
        assertEquals(4, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(4, interactionFacetPage.getTotalElements());

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 4L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.69", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(3, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 1L);
        expansionMethodExpected.put("expansion_false", 3L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions/facet when queried and filtered by species and interactorAc
     **/

    @Test
    public void filterByInteractorAcs() {

        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("physical association")
                        .interactorSpeciesFilter(Set.of("Homo sapiens"))
                        .interactorAcs(Set.of("EBI-715849", "EBI-10000824"))
                        .build()
        );

        // page checks
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(5, interactionFacetPage.getContent().size());
        assertEquals(5, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(5, interactionFacetPage.getTotalElements());

        Set<Long> binariesExpected = new HashSet<>();
        binariesExpected.add(5L);
        binariesExpected.add(3L);
        binariesExpected.add(4L);
        binariesExpected.add(1L);
        binariesExpected.add(10L);

        for (SearchInteraction interaction : interactionFacetPage.getContent()) {
            assertTrue(binariesExpected.contains(interaction.getBinaryInteractionId()));
        }

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 6L);
        specieFacetsExpected.put("10116__Rattus norvegicus (Rat)__#5688c0", 4L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0319__dna__VEE", 1L);
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(4, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(4, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 5L);
        identificationMethodsFacetsExpected.put("MI:0113__western blot", 4L);
        identificationMethodsFacetsExpected.put("MI:0981__tag perox activity", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 1L);
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.53", 1L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.69", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 3L);
        expansionMethodExpected.put("expansion_false", 3L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions/facets when queried and filtered by species and binaryIds
     **/
    @Test
    public void filterByBinaryIds() {

        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("physical association")
                        .interactorSpeciesFilter(Set.of("Homo sapiens"))
                        .binaryInteractionIds(Set.of(10L, 1L))
                        .build()
        );

        // page checks
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(2, interactionFacetPage.getContent().size());
        assertEquals(2, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage.getTotalElements());

        Set<Long> binariesExpected = new HashSet<>();
        binariesExpected.add(1L);
        binariesExpected.add(10L);

        for (SearchInteraction interaction : interactionFacetPage.getContent()) {
            assertTrue(binariesExpected.contains(interaction.getBinaryInteractionId()));
        }

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 6L);
        specieFacetsExpected.put("10116__Rattus norvegicus (Rat)__#5688c0", 4L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0319__dna__VEE", 1L);
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(4, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(4, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 5L);
        identificationMethodsFacetsExpected.put("MI:0113__western blot", 4L);
        identificationMethodsFacetsExpected.put("MI:0981__tag perox activity", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 1L);
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.53", 1L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.69", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 3L);
        expansionMethodExpected.put("expansion_false", 3L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions/facets when queried and filtered by multiple interaction detection method
     **/
    @Test
    public void filterByMultipleDetectionMethods() {
        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("physical association")
                        .interactionDetectionMethodsFilter(Set.of("MI:0029", "MI:0071"))
                        .build()
        );

        // page checks
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(4, interactionFacetPage.getContent().size());
        assertEquals(4, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(4, interactionFacetPage.getTotalElements());

        Set<String> interactionsExpected = new HashSet<>();
        interactionsExpected.add("EBI-1000048");
        interactionsExpected.add("EBI-1000008");

        for (SearchInteraction interaction : interactionFacetPage.getContent()) {
            assertTrue(interactionsExpected.contains(interaction.getAc()));
        }

        //facet checking
        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 4L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("anti bait coip", 4L);
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0006__anti bait coip", 4L);
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.69", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(3, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 1L);
        expansionMethodExpected.put("expansion_false", 3L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    @Test
    public void filterByMultipleIdentificationMethods() {
        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("physical association")
//                        .participantDetectionMethodsFilter(Set.of("predetermined", "tag perox activity"))
                        .participantDetectionMethodsFilter(Set.of("MI:0396", "MI:0981"))
                        .build()
        );

        // page checks
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(6, interactionFacetPage.getContent().size());
        assertEquals(6, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(6, interactionFacetPage.getTotalElements());

        Set<String> interactionsExpected = new HashSet<>();
        interactionsExpected.add("EBI-1000008");
        interactionsExpected.add("EBI-1000026");
        interactionsExpected.add("EBI-1000048");
        interactionsExpected.add("EBI-10000862");

        for (SearchInteraction interaction : interactionFacetPage.getContent()) {
            assertTrue(interactionsExpected.contains(interaction.getAc()));
        }

        //facet checking
        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 6L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 6L);
        interactorTypeFacetsExpected.put("MI:0319__dna__VEE", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(4, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(4, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 5L);
        identificationMethodsFacetsExpected.put("MI:0113__western blot", 4L);
        identificationMethodsFacetsExpected.put("MI:0981__tag perox activity", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(3, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 6L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.4", 1L);
        miScoreFacetsExpected.put("0.53", 1L);
        miScoreFacetsExpected.put("0.69", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 3L);
        expansionMethodExpected.put("expansion_false", 3L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions/facets when queried and filtered by multiple host organism
     **/
    @Test
    public void filterByMultipleHostOrganism() {

        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("physical association")
                        .interactionHostOrganismsFilter(Set.of(
                                "In vitro__-1",
                                "rattus norvegicus liver__10116")
                        ).build()
        );

        // page checks
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(10, interactionFacetPage.getContent().size());
        assertEquals(10, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(10, interactionFacetPage.getTotalElements());

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 6L);
        specieFacetsExpected.put("10116__Rattus norvegicus (Rat)__#5688c0", 4L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0319__dna__VEE", 1L);
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("anti bait coip", 4L);
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0006__anti bait coip", 4L);
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 5L);
        identificationMethodsFacetsExpected.put("MI:0113__western blot", 4L);
        identificationMethodsFacetsExpected.put("MI:0981__tag perox activity", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(3, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 6L);
        hostOrganismFacetsExpected.put("10116__rattus norvegicus liver__#5688c0", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 5L);
        miScoreFacetsExpected.put("0.53", 1L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.69", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 3L);
        expansionMethodExpected.put("expansion_false", 7L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions/facets when queried and filtered by interaction type
     **/
    @Test
    public void filterByInteractionType() {

        Set<String> interactionTypesFilter = Set.of("MI:0915");

        FacetPage<SearchInteraction> interactionFacetPage = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("*")
                        .interactionTypesFilter(interactionTypesFilter)
                        .build()
        );

        // page checks
        assertFalse(interactionFacetPage.getContent().isEmpty());
        assertEquals(10, interactionFacetPage.getContent().size());
        assertEquals(10, interactionFacetPage.getNumberOfElements());
        assertEquals(0, interactionFacetPage.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage.getPageable().getPageSize());
        assertEquals(10, interactionFacetPage.getTotalElements());

        //species facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("9606__Homo sapiens__#335e94", 6L);
        specieFacetsExpected.put("10116__Rattus norvegicus (Rat)__#5688c0", 4L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TAX_ID_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // intra species, in these case the number are the same than TAX_ID_A_B_STYLED
        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTRA_TAX_ID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("MI:0319__dna__VEE", 1L);
        interactorTypeFacetsExpected.put("MI:0326__protein__ELLIPSE", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_A_B_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("anti bait coip", 4L);
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        HashMap<String, Long> detectionMethodsStyledFacetsExpected = new HashMap<>();
        detectionMethodsStyledFacetsExpected.put("MI:0006__anti bait coip", 4L);
        detectionMethodsStyledFacetsExpected.put("MI:0029__density sedimentation", 3L);
        detectionMethodsStyledFacetsExpected.put("MI:0411__elisa", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0004__affinity chrom", 1L);
        detectionMethodsStyledFacetsExpected.put("MI:0071__molecular sieving", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(DETECTION_METHOD_MI_STYLED);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsStyledFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //identification method facet checking
        HashMap<String, Long> identificationMethodsFacetsExpected = new HashMap<>();
        identificationMethodsFacetsExpected.put("MI:0396__predetermined", 5L);
        identificationMethodsFacetsExpected.put("MI:0113__western blot", 4L);
        identificationMethodsFacetsExpected.put("MI:0981__tag perox activity", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(IDENTIFICATION_METHOD_MI_A_B_STYLED);
        assertEquals(3, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(identificationMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("MI:0915__physical association__#7bccc4", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(TYPE_MI_IDENTIFIER_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("-1__In vitro__#8d6666", 6L);
        hostOrganismFacetsExpected.put("10116__rattus norvegicus liver__#5688c0", 4L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(HOST_ORGANISM_TAXID_STYLED);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(NEGATIVE);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // affected by mutation
        HashMap<String, Long> mutationFacetsExpected = new HashMap<>();
        mutationFacetsExpected.put("none__false__#7e8389", 10L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(AFFECTED_BY_MUTATION_STYLED);
        assertEquals(1, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(mutationFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 5L);
        miScoreFacetsExpected.put("0.53", 1L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.69", 1L);

        facetFieldEntryPage = interactionFacetPage.getFacetResultPage(INTACT_MISCORE);
        assertEquals(5, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        // expansion method facet checking
        HashMap<String, Long> expansionMethodExpected = new HashMap<>();
        expansionMethodExpected.put("expansion_true", 3L);
        expansionMethodExpected.put("expansion_false", 7L);

        Page<FacetQueryEntry> facetPageFacetQueryResult = interactionFacetPage.getFacetQueryResult();
        assertEquals(2, facetPageFacetQueryResult.getTotalElements());
        for (FacetQueryEntry facetQueryEntry : facetPageFacetQueryResult) {
            assertEquals(expansionMethodExpected.get(facetQueryEntry.getValue()), new Long(facetQueryEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions/facets when queried and filtered by positive interactions
     **/
    @Test
    public void filterByPositiveInteractions() {
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(PagedInteractionSearchParameters.builder().query("*").build());
        assertEquals(10, interactionOp.getTotalElements());

        //facet checking
        //interaction type facet checking
        HashMap<String, Long> negativeInteractionFacetsExpected = new HashMap<>();
        negativeInteractionFacetsExpected.put("false", 10L);
        negativeInteractionFacetsExpected.put("true", 0L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(NEGATIVE)) {
            assertEquals(negativeInteractionFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions/facets when queried and filtered by negative interactions
     **/
    @Test
    public void filterByNegativeInteractions() {
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("*")
                        .negativeFilter(NegativeFilterStatus.NEGATIVE_ONLY)
                        .build()
        );
        assertEquals(0, interactionOp.getTotalElements());

        //facet checking
        //interaction type facet checking
        HashMap<String, Long> negativeInteractionFacetsExpected = new HashMap<>();
        negativeInteractionFacetsExpected.put("false", 10L);
        negativeInteractionFacetsExpected.put("true", 0L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(NEGATIVE)) {
            assertEquals(negativeInteractionFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }
    }

    /*
     * Expected interactions/facets when queried and filtered by negative and positive interactions
     **/
    @Test
    public void filterByNegativeAndPositiveInteractions() {
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                PagedInteractionSearchParameters.builder()
                        .query("*")
                        .negativeFilter(NegativeFilterStatus.POSITIVE_AND_NEGATIVE)
                        .build()
        );
        assertEquals(10, interactionOp.getTotalElements());

        //facet checking
        //interaction type facet checking
        HashMap<String, Long> negativeInteractionFacetsExpected = new HashMap<>();
        negativeInteractionFacetsExpected.put("false", 10L);
        negativeInteractionFacetsExpected.put("true", 0L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(NEGATIVE)) {
            assertEquals(negativeInteractionFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }
    }

    @Test
    public void findBinaryInteractionsIdsSearch() {
        Page<Long> results = interactionSearchService.findBinaryInteractionIds(
                SimpleInteractionQueryParameters.builder()
                        .query("ndc80")
                        .advancedSearch(false)
                        .pageSize(10)
                        .page(0)
                        .build());
        assertEquals(4, results.getTotalElements());
    }
}