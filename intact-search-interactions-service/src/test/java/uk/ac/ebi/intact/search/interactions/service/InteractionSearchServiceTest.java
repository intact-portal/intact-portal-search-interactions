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
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields;
import uk.ac.ebi.intact.search.interactions.service.util.TestUtil;

import javax.annotation.Resource;
import java.time.Duration;
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
        interactionIndexService.save(searchInteractions, Duration.ofMillis(100));
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
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("\"Shorter J\"");
        assertEquals(4, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "Species Name" in search box
     * TODO...Depending on whether we index species in future, we include/exclude this
     */
    @Ignore
    public void findBySpecies() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("Rattus norvegicus (Rat)");
        assertEquals(4, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Type" in search box
     */
    @Test
    public void findByInteractionType() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("\"physical association\"");
        assertEquals(10, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Type MI identifier" in search box
     */
    @Test
    public void findByInteractionTypeMIIdentifier() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("MI:0915");
        assertEquals(10, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interaction identifier" in search box
     */
    @Test
    public void findByInteractionIdentifier() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("EBI-10000862");
        assertEquals(1, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Detection Method" in search box
     */
    @Test
    public void findByIntDetMethod() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("\"molecular sieving\"");
        assertEquals(1, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Participant Detection Method" in search box
     */
    @Test
    public void findByParticipantDetMethod() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("predetermined");
        assertEquals(5, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interactor Xref" in search box
     * includes identifier test
     */
    @Test
    public void findByInteractorXref() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("IPR023601");
        assertEquals(1, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interactor Intact name" in search box
     */
    @Test
    public void findByInteractorIntactName() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("bet1l_rat");
        assertEquals(4, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interactor Full name" in search box
     */
    @Test
    public void findByInteractorFullName() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("\"Transcription factor p65\"");
        assertEquals(1, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interaction Xref" in search box
     */
    @Test
    public void findByInteractionXref() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("IM-23550-1");
        assertEquals(4, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Interactor Aliases" in search box
     */
    @Test
    public void findByInteractorAliases() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("NFKB3");
        assertEquals(1, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "Host Organism" in search box
     * TODO...Depending on whether we index host organism in future, we include/exclude this
     */
    @Ignore
    public void findByHostOrganism() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("\"In vitro\"");
        assertEquals(6, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Feature ShortLabel" in search box
     * includes preserve original index Test
     */
    @Test
    public void findByfeatureShortLabel() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("c-terminal");
        assertEquals(5, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Feature Type" in search box
     */
    @Test
    public void findByfeatureType() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("\"gst tag\"");
        assertEquals(3, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Publication Identifier" in search box
     */
    @Test
    public void findByPublicationIdentifier() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("15961401");
        assertEquals(5, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "middle chunk of a word with delimiter" in search box
     */
    @Test
    public void edgeNGramsTest1() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("F-kap");
        assertEquals(0, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "middle chunk of a word "in search box
     */
    @Test
    public void edgeNGramsTest2() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("FKB3");
        assertEquals(0, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "word separated by delimiter" in search box
     */
    @Test
    public void wordPartIndexTimeGeneration() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("kappaB");
        assertEquals(1, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "concatenation after removing delimiter" in search box
     */
    @Test
    public void concatenationOfDelimitedTerm() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("NFkappaB");
        assertEquals(1, interactionOp.getTotalElements());
    }

    /**
     * Expected Facet Results If the User types "physical association" and Hit Search Button
     */
    @Test
    public void facetTest() {
        FacetPage<SearchInteraction> interaction = interactionSearchService.findInteractionWithFacet(
                "physical association",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        Page<FacetFieldEntry> facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.NEGATIVE);
        assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("false")) {
                assertEquals(10, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.INTACT_MISCORE);
        assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("0.39")) {
                assertEquals(3, facetFieldEntry.getValueCount());
            }
            if (value.equals("0.52")) {
                assertEquals(1, facetFieldEntry.getValueCount());
            }
            if (value.equals("0.55")) {
                assertEquals(3, facetFieldEntry.getValueCount());
            }
            if (value.equals("0.58")) {
                assertEquals(3, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.DETECTION_METHOD_S);
        assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("anti bait coip")) {
                assertEquals(4, facetFieldEntry.getValueCount());
            }
            if (value.equals("density sedimentatio")) {
                assertEquals(3, facetFieldEntry.getValueCount());
            }
            if (value.equals("affinity chrom")) {
                assertEquals(1, facetFieldEntry.getValueCount());
            }
            if (value.equals("elisa")) {
                assertEquals(1, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.TYPE_S);
        assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("physical association")) {
                assertEquals(10, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.HOST_ORGANISM_S);
        assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("In vitro")) {
                assertEquals(6, facetFieldEntry.getValueCount());
            }
            if (value.equals("rattus norvegicus liver")) {
                assertEquals(4, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.SPECIES_A_B_STR);
        assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("Homo sapiens")) {
                assertEquals(6, facetFieldEntry.getValueCount());
            }
            if (value.equals("Rattus norvegicus (Rat)")) {
                assertEquals(4, facetFieldEntry.getValueCount());
            }
        }

        assertEquals(10, interaction.getTotalElements());
    }

    /**
     * Expected Facet Results If the User types "physical association" and Hit Search Button
     */
    @Test
    public void findInteractionIdentifiersTest() {
        Page<SearchInteraction> interactions = interactionSearchService.findInteractionIdentifiers(
                "physical association",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        assertEquals(10, interactions.getTotalElements());

        Set<String> acs = new TreeSet<>();
        Set<String> expectedAcs = new TreeSet<>(Arrays.asList("EBI-1000008", "EBI-1000026", "EBI-1000048", "EBI-10000796", "EBI-10000862"));

        for (SearchInteraction searchInteraction : interactions) {
            acs.add(searchInteraction.getAc());
        }
        assertEquals(expectedAcs, acs);

        Set<Long> binaryIds = new TreeSet<>();
        Set<Long> expectedBinaryIds = new TreeSet<>(Arrays.asList(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L));

        for (SearchInteraction searchInteraction : interactions) {
            binaryIds.add(searchInteraction.getBinaryInteractionId());
        }

        assertEquals(binaryIds, expectedBinaryIds);

    }

    /**
     * Expected behaviour when filter elements are passed
     */
    @Test
    public void filterInterSpeciesFalseOneSpecies() {

        Set<String> species = new HashSet<>();
        species.add("Homo sapiens");

        Set<String> interactorTypesFilter = new HashSet<>();
        interactorTypesFilter.add("protein");

        Set<String> detectionMethod = new HashSet<>();
        detectionMethod.add("molecular sieving");

        Set<String> interactionType = new HashSet<>();
        interactionType.add("physical association");

        Set<String> hostOrganism = new HashSet<>();
        hostOrganism.add("In vitro");

        double minMiscore = 0;
        double maxMiscore = 0.7;

        int page = 0;
        int size = 10;

        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "physical association",
                false,
                species,
                interactorTypesFilter,
                detectionMethod,
                interactionType,
                hostOrganism,
                false,
                false,
                false,
                minMiscore,
                maxMiscore,
                false,
                null,
                null,
                page,
                size);
        assertEquals(1, interactionOp.getTotalElements());

    }

    /**
     * Expected behaviour when filter elements are passed
     */
    @Test
    public void filterInterSpeciesTrueTwoSpecies() {

        //filter2
        Set<String> detectionMethod = new HashSet<>();
        detectionMethod.add("molecular sieving");

        Set<String> interactionType = new HashSet<>();
        interactionType.add("physical association");

        Set<String> hostOrganism = new HashSet<>();
        hostOrganism.add("In vitro");

        double minMiscore = 0;
        double maxMiscore = 0.6;

        Set<String> species = new HashSet<>();
        species.add("Homo sapiens");
        species.add("Rattus norvegicus (Rat)");

        Set<String> interactorTypesFilter = new HashSet<>();
        interactorTypesFilter.add("protein");

        int page = 0;

        int size = 10;

        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "physical association",
                false,
                species,
                interactorTypesFilter,
                detectionMethod,
                interactionType,
                hostOrganism,
                false,
                false,
                false,
                minMiscore,
                maxMiscore,
                true,
                null,
                null,
                page,
                size);
        assertEquals(0, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "Species Name" in search box and expects faceting in results page
     */
    @Test
    public void findInteractionsBySpeciesWithFacet() {

        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "rat",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(4, interactionOp.getTotalElements());
    }

    /*
     * Expected interactions when queried by a interactor field
     **/

    @Test
    public void findInteractionsByInteractorIndexedField() {
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "kappaB",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(1, interactionOp.getTotalElements());

    }

    /*
     * Expected interactions when queried by empty string
     **/

    @Test
    public void findInteractionsByEmptyString() {
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(10, interactionOp.getTotalElements());
        assertEquals(10, interactionOp.getNumberOfElements());

    }

    /*
     * Expected interactions when queried by "*" character
     **/

    @Test
    public void findInteractionsByStarString() {
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "*",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(10, interactionOp.getTotalElements());
        assertEquals(10, interactionOp.getNumberOfElements());

    }

    /*
     * Expected interactions when queried by "list of interactor acs" for batch search
     **/
    @Test
    public void findInteractionsByCommaSeparatedInteractorAcsString() {
        // check https://issues.apache.org/jira/browse/SOLR-12858 for embedded POST request issue
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "EBI-715849,EBI-724102",
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(4, interactionOp.getTotalElements());
        assertEquals(4, interactionOp.getNumberOfElements());

    }

    /*
     * Expected interactions/facet when queried and filtered by species and interactorAc
     **/

    @Test
    public void filterByInteractorAcs() {
        Set<String> species = new HashSet<>();
        species.add("Homo sapiens");

        Set<String> interactorAcs = new HashSet<>();
        interactorAcs.add("EBI-715849");
        interactorAcs.add("EBI-10000824");
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "physical association",
                false,
                species,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                interactorAcs,
                0,
                10);
        assertEquals(5, interactionOp.getTotalElements());

        Set<Long> binariesExpected = new HashSet<>();
        binariesExpected.add(5L);
        binariesExpected.add(3L);
        binariesExpected.add(4L);
        binariesExpected.add(1L);
        binariesExpected.add(10L);

        for (SearchInteraction interaction : interactionOp.getContent()) {
            assertTrue(binariesExpected.contains(interaction.getBinaryInteractionId()));
        }

        //specie facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("Homo sapiens", 6L);
        specieFacetsExpected.put("Rattus norvegicus (Rat)", 4L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(SPECIES_A_B_STR)) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("dna", 1L);
        interactorTypeFacetsExpected.put("protein", 6L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(TYPE_A_B_STR)) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(DETECTION_METHOD_S)) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("physical association", 6L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(TYPE_S)) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("In vitro", 6L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(HOST_ORGANISM_S)) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


        //Negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 6L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(NEGATIVE)) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


        //MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 1L);
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.53", 1L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.69", 1L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(INTACT_MISCORE)) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


    }

    /*
     * Expected interactions/facets when queried and filtered by species and binaryIds
     **/
    @Test
    public void filterByBinaryIds() {
        Set<String> species = new HashSet<>();
        species.add("Homo sapiens");

        Set<Long> binaryIds = new HashSet<>();
        binaryIds.add(10L);
        binaryIds.add(1L);
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "physical association",
                false,
                species,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                binaryIds,
                null,
                0,
                10);
        assertEquals(2, interactionOp.getTotalElements());

        Set<Long> binariesExpected = new HashSet<>();
        binariesExpected.add(1L);
        binariesExpected.add(10L);

        for (SearchInteraction interaction : interactionOp.getContent()) {
            assertTrue(binariesExpected.contains(interaction.getBinaryInteractionId()));
        }

        //specie facet checking
        HashMap<String, Long> specieFacetsExpected = new HashMap<>();
        specieFacetsExpected.put("Homo sapiens", 6L);
        specieFacetsExpected.put("Rattus norvegicus (Rat)", 4L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(SPECIES_A_B_STR)) {
            assertEquals(specieFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        //interactor type facet checking
        HashMap<String, Long> interactorTypeFacetsExpected = new HashMap<>();
        interactorTypeFacetsExpected.put("dna", 1L);
        interactorTypeFacetsExpected.put("protein", 6L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(TYPE_A_B_STR)) {
            assertEquals(interactorTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(DETECTION_METHOD_S)) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("physical association", 6L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(TYPE_S)) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


        //host organism facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("In vitro", 6L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(HOST_ORGANISM_S)) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


        //Negative facet checking
        HashMap<String, Long> negativeFacetsExpected = new HashMap<>();
        negativeFacetsExpected.put("false", 6L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(NEGATIVE)) {
            assertEquals(negativeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


        //MIscore facet checking
        HashMap<String, Long> miScoreFacetsExpected = new HashMap<>();
        miScoreFacetsExpected.put("0.4", 1L);
        miScoreFacetsExpected.put("0.64", 2L);
        miScoreFacetsExpected.put("0.53", 1L);
        miScoreFacetsExpected.put("0.56", 1L);
        miScoreFacetsExpected.put("0.69", 1L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(INTACT_MISCORE)) {
            assertEquals(miScoreFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }


    }

    /*
     * Expected interactions/facets when queried and filtered by multiple interaction detection method
     **/
    @Test
    public void filterByMultipleDetectionMethods() {
        Set<String> detectionMethods = new HashSet<>();
        detectionMethods.add("density sedimentation");
        detectionMethods.add("molecular sieving");
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "physical association",
                false,
                null,
                null,
                detectionMethods,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(4, interactionOp.getTotalElements());

        Set<String> interactionsExpected = new HashSet<>();
        interactionsExpected.add("EBI-1000048");
        interactionsExpected.add("EBI-1000008");

        for (SearchInteraction interaction : interactionOp.getContent()) {
            assertTrue(interactionsExpected.contains(interaction.getAc()));
        }

        //facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("density sedimentation", 3L);
        detectionMethodsFacetsExpected.put("molecular sieving", 1L);
        detectionMethodsFacetsExpected.put("elisa", 1L);
        detectionMethodsFacetsExpected.put("affinity chrom", 1L);
        detectionMethodsFacetsExpected.put("anti bait coip", 4L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(DETECTION_METHOD_S)) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

    }

    /*
     * Expected interactions/facets when queried and filtered by multiple host organism
     **/
    @Test
    public void filterByMultipleHostOrganism() {
        Set<String> hostOrganisms = new HashSet<>();
        hostOrganisms.add("In vitro");
        hostOrganisms.add("rattus norvegicus liver");
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "physical association",
                false,
                null,
                null,
                null,
                null,
                hostOrganisms,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(10, interactionOp.getTotalElements());

        Set<String> hostOrganismsForFacetTest = new HashSet<>();
        hostOrganismsForFacetTest.add("In vitro");
        FacetPage<SearchInteraction> interactionOpForFacetTest = interactionSearchService.findInteractionWithFacet(
                "physical association",
                false,
                null,
                null,
                null,
                null,
                hostOrganismsForFacetTest,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(6, interactionOpForFacetTest.getTotalElements());


        //facet checking
        HashMap<String, Long> hostOrganismFacetsExpected = new HashMap<>();
        hostOrganismFacetsExpected.put("In vitro", 6L);
        hostOrganismFacetsExpected.put("rattus norvegicus liver", 4L);

        for (FacetFieldEntry facetFieldEntry : interactionOpForFacetTest.getFacetResultPage(HOST_ORGANISM_S)) {
            assertEquals(hostOrganismFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

    }

    /*
     * Expected interactions/facets when queried and filtered by multiple host organism
     **/
    @Test
    public void filterByMultipleInteractionType() {
        Set<String> interactionTypesFilter = new HashSet<>();
        interactionTypesFilter.add("physical association");
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "*",
                false,
                null,
                null,
                null,
                interactionTypesFilter,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(10, interactionOp.getTotalElements());

        //facet checking
        //interaction type facet checking
        HashMap<String, Long> interactionTypeFacetsExpected = new HashMap<>();
        interactionTypeFacetsExpected.put("physical association", 10L);

        for (FacetFieldEntry facetFieldEntry : interactionOp.getFacetResultPage(TYPE_S)) {
            assertEquals(interactionTypeFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }
    }
}