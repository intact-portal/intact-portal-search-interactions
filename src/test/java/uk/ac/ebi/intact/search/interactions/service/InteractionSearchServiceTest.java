package uk.ac.ebi.intact.search.interactions.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.controller.SearchInteractionResult;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.util.TestUtil;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        assertEquals(interactionSearchService.countTotal(), searchInteractions.size());
    }

    @After
    public void tearDown() {
        interactionIndexService.deleteAll();
    }

    /**
     * Behaviour If the User types "Author name" in search box
     */
    @Test
    public void findByAuthor() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("Shorter J.");
        assertEquals(interactionOp.getNumberOfElements(), 4);
    }

    /**
     * Behaviour If the User types "Species Name" in search box
     */
    @Test
    public void findBySpecies() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("Rattus norvegicus (Rat)");
        assertEquals(interactionOp.getNumberOfElements(), 4);
    }

    /**
     * Behaviour If the User types "SearchInteraction Type" in search box
     */
    @Test
    public void findByInteractionType() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("physical association");
        assertEquals(interactionOp.getNumberOfElements(), 10);
    }

    /**
     * Behaviour If the User types "SearchInteraction Detection Method" in search box
     */
    @Test
    public void findByIntDetMethod() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("molecular sieving");
        assertEquals(interactionOp.getNumberOfElements(), 1);
    }

    /**
     * Behaviour If the User types "Host Organism" in search box
     */
    @Test
    public void findByHostOrganism() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("\"In vitro\"");
        assertEquals(interactionOp.getNumberOfElements(), 6);
    }

    /**
     * Expected Facet Results If the User types "physical association" and Hit Search Button
     */
    @Test
    public void facetTest() {
        FacetPage<SearchInteraction>  interaction = interactionSearchService.findInteractionWithFacet(
                "physical association",
                null,
                null,
                null,
                false,
                0,
                1,
                null,
                false,
                0,
                10);

        //TODO This conversion and checking of the result maybe better in a test for the controller instead of the service
        //TODO This is because SearchInteractionResult is now in the controller
        SearchInteractionResult interactionOp = new SearchInteractionResult(interaction);
        Set<String> facetFields = interactionOp.getFacetResultPage().keySet();
        assertTrue(facetFields.contains(INTERACTION_NEGATIVE));
        assertTrue(facetFields.contains(INTACT_MISCORE));
        assertTrue(facetFields.contains(INTERACTION_DETECTION_METHOD_STR));
        assertTrue(facetFields.contains(INTERACTION_TYPE_STR));
        assertTrue(facetFields.contains(HOST_ORGANISM_STR));
        assertTrue(facetFields.contains(SPECIES_A_B_STR));

        for (String facetField : interactionOp.getFacetResultPage().keySet()) {
            if (facetField.equals(INTERACTION_NEGATIVE)) {
                List<SearchInteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (SearchInteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("false")) {
                        assertEquals(10, facetCount.getValueCount().longValue());
                    }
                }
            }

            if (facetField.equals(INTACT_MISCORE)) {
                List<SearchInteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (SearchInteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("0.39")) {
                        assertEquals(3, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("0.52")) {
                        assertEquals(1, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("0.55")) {
                        assertEquals(3, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("0.58")) {
                        assertEquals(3, facetCount.getValueCount().longValue());
                    }
                }
            }

            if (facetField.equals(INTERACTION_DETECTION_METHOD_STR)) {
                List<SearchInteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (SearchInteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("anti bait coip")) {
                        assertEquals(4, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("density sedimentatio")) {
                        assertEquals(3, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("affinity chrom")) {
                        assertEquals(1, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("elisa")) {
                        assertEquals(1, facetCount.getValueCount().longValue());
                    }
                }
            }

            if (facetField.equals(INTERACTION_TYPE_STR)) {
                List<SearchInteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (SearchInteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("physical association")) {
                        assertEquals(10, facetCount.getValueCount().longValue());
                    }
                }
            }

            if (facetField.equals(HOST_ORGANISM_STR)) {
                List<SearchInteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (SearchInteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("In vitro")) {
                        assertEquals(6, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("rattus norvegicus liver")) {
                        assertEquals(4, facetCount.getValueCount().longValue());
                    }
                }
            }

            if (facetField.equals(SPECIES_A_B_STR)) {
                List<SearchInteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (SearchInteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("Homo sapiens")) {
                        assertEquals(6, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("Rattus norvegicus (Rat)")) {
                        assertEquals(4, facetCount.getValueCount().longValue());
                    }
                }
            }
        }

        assertEquals(10, interactionOp.getNumberOfElements());
    }

    /**
     * Expected behaviour when filter elements are passed
     */
    @Test
    public void filterInterSpeciesFalseOneSpecies() {

        //filter1
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

        int page = 0;
        int size = 10;

        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "physical association",
                detectionMethod,
                interactionType,
                hostOrganism,
                false,
                minMiscore,
                maxMiscore,
                species,
                false,
                page,
                size);
        assertEquals(1, interactionOp.getNumberOfElements());

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

        int page = 0;

        int size = 10;

        FacetPage<SearchInteraction>  interactionOp = interactionSearchService.findInteractionWithFacet(
                "physical association",
                detectionMethod,
                interactionType,
                hostOrganism,
                false,
                minMiscore,
                maxMiscore,
                species,
                true,
                page,
                size);
        assertEquals(0, interactionOp.getNumberOfElements());
    }

}