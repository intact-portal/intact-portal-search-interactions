package uk.ac.ebi.intact.search.interactions.service;

import org.junit.After;
import org.junit.Assert;
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

import static org.junit.Assert.assertEquals;
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
        Assert.assertEquals(interactionOp.getTotalElements(), 4);
    }

    /**
     * Behaviour If the User types "Species Name" in search box
     */
    @Test
    public void findBySpecies() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("Rattus norvegicus (Rat)");
        Assert.assertEquals(interactionOp.getTotalElements(), 4);
    }

    /**
     * Behaviour If the User types "SearchInteraction Type" in search box
     */
    @Test
    public void findByInteractionType() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("physical association");
        Assert.assertEquals(interactionOp.getTotalElements(), 10);
    }

    /**
     * Behaviour If the User types "SearchInteraction Detection Method" in search box
     */
    @Test
    public void findByIntDetMethod() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("molecular sieving");
        Assert.assertEquals(interactionOp.getTotalElements(), 1);
    }

    /**
     * Behaviour If the User types "Host Organism" in search box
     */
    @Test
    public void findByHostOrganism() {
        Page<SearchInteraction> interactionOp = interactionSearchService.findInteractions("\"In vitro\"");
        Assert.assertEquals(interactionOp.getTotalElements(), 6);
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
                null,
                null,
                false,
                0,
                1,
                false,
                0,
                10);

        Page<FacetFieldEntry> facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.NEGATIVE);
        Assert.assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if(value.equals("false")){
                Assert.assertEquals(10, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.INTACT_MISCORE);
        Assert.assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("0.39")) {
                Assert.assertEquals(3, facetFieldEntry.getValueCount());
            }
            if (value.equals("0.52")) {
                Assert.assertEquals(1, facetFieldEntry.getValueCount());
            }
            if (value.equals("0.55")) {
                Assert.assertEquals(3, facetFieldEntry.getValueCount());
            }
            if (value.equals("0.58")) {
                Assert.assertEquals(3, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.DETECTION_METHOD_STR);
        Assert.assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("anti bait coip")) {
                Assert.assertEquals(4, facetFieldEntry.getValueCount());
            }
            if (value.equals("density sedimentatio")) {
                Assert.assertEquals(3, facetFieldEntry.getValueCount());
            }
            if (value.equals("affinity chrom")) {
                Assert.assertEquals(1, facetFieldEntry.getValueCount());
            }
            if (value.equals("elisa")) {
                Assert.assertEquals(1, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.TYPE_STR);
        Assert.assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("physical association")) {
                Assert.assertEquals(10, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.HOST_ORGANISM_STR);
        Assert.assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("In vitro")) {
                Assert.assertEquals(6, facetFieldEntry.getValueCount());
            }
            if (value.equals("rattus norvegicus liver")) {
                Assert.assertEquals(4, facetFieldEntry.getValueCount());
            }
        }

        facetFieldEntryPage = interaction.getFacetResultPage(SearchInteractionFields.SPECIES_A_B_STR);
        Assert.assertFalse(facetFieldEntryPage.getContent().isEmpty());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage.getContent()) {
            final String value = facetFieldEntry.getValue();
            if (value.equals("Homo sapiens")) {
                Assert.assertEquals(6, facetFieldEntry.getValueCount());
            }
            if (value.equals("Rattus norvegicus (Rat)")) {
                Assert.assertEquals(4, facetFieldEntry.getValueCount());
            }
        }

        Assert.assertEquals(10, interaction.getTotalElements());
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
        Assert.assertEquals(1, interactionOp.getTotalElements());

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
        Assert.assertEquals(0, interactionOp.getTotalElements());
    }

}