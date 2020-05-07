package uk.ac.ebi.intact.search.interactions.service;

/**
 * Created by anjali on 19/02/20.
 */

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.util.TestUtil;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChildInteractorSearchServiceTest {

    @Resource
    private InteractionIndexService interactionIndexService;

    @Resource
    private ChildInteractorSearchService childInteractorSearchService;

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
        assertEquals(20, childInteractorSearchService.countTotal());// includes duplicated records
    }

    @After
    public void tearDown() {
        interactionIndexService.deleteAll();
    }

    /**
     * Expected number of interactors when interactions are queried by species. Returns total documents found in the groups
     * instead the number of unique groups
     */
    @Test
    public void getUniqueChildInteractorsFromInteractionQuery() {
        GroupPage<SearchChildInteractor> page = childInteractorSearchService.findInteractorsWithGroup(
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
        assertEquals(10, page.getTotalElements()); //Total documents found before grouping
        assertEquals(7, page.getNumberOfElements()); //Elements in the page
    }

    /**
     * Expected number of interactors when interactions are queried by species. Returns the number of groups
     */
    @Test
    public void countUniqueChildInteractorsFromInteractionQuery() {
        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(
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
                false);

        assertEquals(7, numInteractors);
    }

    /**
     * Expected interactors when interactions are queried by all filters
     */

    @Test
    public void getUniqueChildInteractorsFromInteractionFilterQuery() {

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

        GroupPage<SearchChildInteractor> childInteractorsOp = childInteractorSearchService.findInteractorsWithGroup(
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
        assertEquals(2, childInteractorsOp.getTotalElements()); //Total documents found before grouping
        assertEquals(2, childInteractorsOp.getNumberOfElements()); //Elements in the page

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(
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
                false);
        assertEquals(2, numInteractors);

        List<String> interactorAcs = new ArrayList<>();
        interactorAcs.add("EBI-724102");
        interactorAcs.add("EBI-715849");


        for (SearchChildInteractor searchChildInteractor : childInteractorsOp.getContent()) {
            if (!interactorAcs.contains(searchChildInteractor.getInteractorAc())) {
                Assert.fail("The interactor is not in the list of interactors expected");
            }
        }
    }

    /**
     * Expected sync between interactions and child interactors
     */
    @Test
    public void checkInteractionAndChildInteractorsSync() {

        //Interactors
        GroupPage<SearchChildInteractor> childInteractorsOp = childInteractorSearchService.findInteractorsWithGroup("Rattus norvegicus (Rat)",
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
        assertEquals(10, childInteractorsOp.getTotalElements());  //Total documents found before grouping
        assertEquals(7, childInteractorsOp.getNumberOfElements()); //Elements in the page


        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(
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
                false);
        assertEquals(7, numInteractors);

        List<String> interactorAcs = new ArrayList<>();
        interactorAcs.add("EBI-7837133");
        interactorAcs.add("EBI-915507");
        interactorAcs.add("EBI-2028244");
        interactorAcs.add("EBI-4423297");
        interactorAcs.add("EBI-9997695");
        interactorAcs.add("EBI-10000824");
        interactorAcs.add("EBI-73886");

        for (SearchChildInteractor searchChildInteractor : childInteractorsOp.getContent()) {
            if (!interactorAcs.contains(searchChildInteractor.getInteractorAc())) {
                Assert.fail("The interactor is not in the list of interactors expected");
            }
        }

        //Interactions
        FacetPage<SearchInteraction> searchInteractionsOp = interactionSearchService.findInteractionWithFacet("Rattus norvegicus (Rat)",
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
        assertEquals(5, searchInteractionsOp.getTotalElements());
        assertEquals(5, searchInteractionsOp.getNumberOfElements()); //Elements in the page


        //5 binaries 2 interactions
        List<String> interactionAcs = new ArrayList<>();
        interactionAcs.add("EBI-10000796");
        interactionAcs.add("EBI-10000862");

        for (SearchInteraction searchInteraction : searchInteractionsOp.getContent()) {
            if (!interactionAcs.contains(searchInteraction.getAc())) {
                Assert.fail("The interaction is not in the list of interactions expected");
            }
        }
    }

    /*
     * Expected interactors when queried by empty string
     **/

    @Test
    public void findInteractorsByEmptyString() {
        GroupPage<SearchChildInteractor> childInteractorsOp = childInteractorSearchService.findInteractorsWithGroup(
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
        assertEquals(20, childInteractorsOp.getTotalElements()); //Total documents found before grouping
        assertEquals(10, childInteractorsOp.getNumberOfElements()); //Elements in the first page

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(
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
                false);
        assertEquals(11, numInteractors);
    }

     /*
      * Expected interactors when queried by "*" character
      **/

    @Test
    public void findInteractionsByStarString() {
        GroupPage<SearchChildInteractor> childInteractorsOp = childInteractorSearchService.findInteractorsWithGroup(
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
        assertEquals(20, childInteractorsOp.getTotalElements()); //Total documents found before grouping
        assertEquals(10, childInteractorsOp.getNumberOfElements()); //Elements in the first page

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(
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
                false);
        assertEquals(11, numInteractors);
    }
}
