package uk.ac.ebi.intact.search.interactions.service;

import org.junit.After;
import org.junit.Before;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

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
     */
    @Test
    public void findByAuthor() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("Shorter J.");
        assertEquals(4, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "Species Name" in search box
     */
    @Test
    public void findBySpecies() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("Rattus norvegicus (Rat)");
        assertEquals(4, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Type" in search box
     */
    @Test
    public void findByInteractionType() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("physical association");
        assertEquals(10, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "SearchInteraction Detection Method" in search box
     */
    @Test
    public void findByIntDetMethod() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("molecular sieving");
        assertEquals(1, interactionOp.getTotalElements());
    }

    /**
     * Behaviour If the User types "Host Organism" in search box
     */
    @Test
    public void findByHostOrganism() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("\"In vitro\"");
        assertEquals(6, interactionOp.getTotalElements());
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
                0,
                1,
                false,
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

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.DETECTION_METHOD_STR);
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

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.TYPE_STR);
        assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("physical association")) {
                assertEquals(10, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.HOST_ORGANISM_STR);
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
     * Expected behaviour when filter elements are passed
     */
    @Test
    public void filterInterSpeciesFalseOneSpecies() {

        Set<String> species = new HashSet<>();
        species.add("Homo sapiens");

        Set<String> interactorType = new HashSet<>();
        interactorType.add("protein");

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
                interactorType,
                detectionMethod,
                interactionType,
                hostOrganism,
                false,
                minMiscore,
                maxMiscore,
                false,
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

        Set<String> interactorType = new HashSet<>();
        interactorType.add("protein");

        int page = 0;

        int size = 10;

        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "physical association",
                false,
                species,
                interactorType,
                detectionMethod,
                interactionType,
                hostOrganism,
                false,
                minMiscore,
                maxMiscore,
                true,
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
                "Rattus norvegicus (Rat)",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                0,
                1,
                false,
                0,
                10);
        assertEquals(5, interactionOp.getTotalElements());
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
                0,
                1,
                false,
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
                0,
                1,
                false,
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
                0,
                1,
                false,
                0,
                10);
        assertEquals(10, interactionOp.getTotalElements());
        assertEquals(10, interactionOp.getNumberOfElements());

    }

    /*
     * Expected interactions when queried by "comma separated interactor preferred Ids"
     **/

    @Test
    public void findInteractionsByCommaSeparatedInteractorPreferredIdString() {
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "Q96Q78,C9JGC4",
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                0,
                1,
                false,
                0,
                10);
        assertEquals(4, interactionOp.getTotalElements());
        assertEquals(4, interactionOp.getNumberOfElements());

    }

    /*
     * Expected interactions when queried by "comma separated interactor aliases"
     **/

    @Test
    public void findInteractionsByCommaSeparatedInteractorAliasesString() {
        FacetPage<SearchInteraction> interactionOp = interactionSearchService.findInteractionWithFacet(
                "NFKB3,NUF2R",
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                0,
                1,
                false,
                0,
                10);
        assertEquals(3, interactionOp.getTotalElements());
        assertEquals(3, interactionOp.getNumberOfElements());
    }
}