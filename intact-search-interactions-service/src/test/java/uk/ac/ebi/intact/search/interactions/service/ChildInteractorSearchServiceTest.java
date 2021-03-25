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
import static org.junit.Assert.assertTrue;

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
                "rat",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(5, page.getTotalElements());
    }

    /**
     * Expected interactors when interactions are queried by all filters
     */

    @Test
    public void getUniqueChildInteractorsFromInteractionFilterQuery() {

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

        GroupPage<SearchChildInteractor> childInteractorsOp = childInteractorSearchService.findInteractorsWithGroup(
                "physical association",
                false,
                species,
                interactorTypesFilter,
                detectionMethod,
                interactionType,
                hostOrganism,
                false,
                false,
                minMiscore,
                maxMiscore,
                false,
                null,
                null,
                page,
                size);
        assertEquals(2, childInteractorsOp.getTotalElements()); //Total documents found before grouping
        assertEquals(2, childInteractorsOp.getNumberOfElements()); //Elements in the page

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(
                "physical association",
                false,
                species,
                interactorTypesFilter,
                detectionMethod,
                interactionType,
                hostOrganism,
                false,
                false,
                minMiscore,
                maxMiscore,
                false,
                null
                , null);
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
        GroupPage<SearchChildInteractor> childInteractorsOp = childInteractorSearchService.findInteractorsWithGroup("rat",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(5, childInteractorsOp.getTotalElements());  //Total documents found before grouping
        assertEquals(5, childInteractorsOp.getNumberOfElements()); //Elements in the page


        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(
                "rat",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                0,
                1,
                false,
                null
                , null);
        assertEquals(5, numInteractors);

        List<String> interactorAcs = new ArrayList<>();
        interactorAcs.add("EBI-7837133");
        interactorAcs.add("EBI-915507");
        interactorAcs.add("EBI-2028244");
        interactorAcs.add("EBI-4423297");
        interactorAcs.add("EBI-9997695");

        for (SearchChildInteractor searchChildInteractor : childInteractorsOp.getContent()) {
            if (!interactorAcs.contains(searchChildInteractor.getInteractorAc())) {
                Assert.fail("The interactor is not in the list of interactors expected");
            }
        }

        //Interactions
        FacetPage<SearchInteraction> searchInteractionsOp = interactionSearchService.findInteractionWithFacet("rat",
                false,
                null,
                null,
                null,
                null,
                null,
                false,
                false, 0,
                1,
                false,
                null,
                null,
                0,
                10
        );
        assertEquals(4, searchInteractionsOp.getTotalElements());


        //5 binaries 2 interactions
        List<String> interactionAcs = new ArrayList<>();
        interactionAcs.add("EBI-10000796");

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
                false,
                0,
                1,
                false,
                null,
                null,
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
                false,
                0,
                1,
                false,
                null,
                null);
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
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        //TODO... Check later why total elements is 20 here, the earlier
        // checkInteractionAndChildInteractorsSync test seems to give correct value, here it should be 11

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
                false,
                0,
                1,
                false,
                null,
                null);
        assertEquals(11, numInteractors);
    }

    /*
     * Expected interactors when queried by a text and filtered by species and interactorAc
     **/
    @Test
    public void filterByInteractorAcs() {
        Set<String> species = new HashSet<>();
        species.add("Homo sapiens");

        Set<String> interactorAcs = new HashSet<>();
        interactorAcs.add("EBI-715849");
        interactorAcs.add("EBI-10000824");
        GroupPage<SearchChildInteractor> interactorOp = childInteractorSearchService.findInteractorsWithGroup(
                "physical association",
                false,
                species,
                null,
                null,
                null,
                null,
                false,
                false,
                0,
                1,
                false,
                null,
                interactorAcs,
                0,
                10);
        assertEquals(10, interactorOp.getTotalElements());//TODO...this needs to be checked it should give 6

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(
                "physical association",
                false,
                species,
                null,
                null,
                null,
                null,
                false,
                false,
                0,
                1,
                false,
                null,
                interactorAcs);

        assertEquals(6, numInteractors);

        Set<String> interactorsExpected = new HashSet<>();
        interactorsExpected.add("EBI-715849");
        interactorsExpected.add("EBI-999900");
        interactorsExpected.add("EBI-724102");
        interactorsExpected.add("EBI-999909");
        interactorsExpected.add("EBI-10000824");
        interactorsExpected.add("EBI-73886");

        assertTrue((interactorOp.getContent().get(0).getInteractorAc().equals("EBI-715849")));

        for (SearchChildInteractor interactor : interactorOp.getContent()) {
            assertTrue(interactorsExpected.contains(interactor.getInteractorAc()));
        }

    }

    /*
     * Expected interactors when queried by a text and filtered by species and binaryIds
     **/
    @Test
    public void filterByBinaryIds() {
        Set<String> species = new HashSet<>();
        species.add("Homo sapiens");

        Set<Integer> binaryIds = new HashSet<>();
        binaryIds.add(10);
        binaryIds.add(1);
        GroupPage<SearchChildInteractor> interactorOp = childInteractorSearchService.findInteractorsWithGroup(
                "physical association",
                false,
                species,
                null,
                null,
                null,
                null,
                false,
                false,
                0,
                1,
                false,
                binaryIds,
                null,
                0,
                10);
        assertEquals(4, interactorOp.getTotalElements());

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(
                "physical association",
                false,
                species,
                null,
                null,
                null,
                null,
                false,
                false,
                0,
                1,
                false,
                binaryIds,
                null
        );

        assertEquals(4, numInteractors);

        Set<String> interactorsExpected = new HashSet<>();
        interactorsExpected.add("EBI-715849");
        interactorsExpected.add("EBI-724102");
        interactorsExpected.add("EBI-10000824");
        interactorsExpected.add("EBI-73886");

        for (SearchChildInteractor interactor : interactorOp.getContent()) {
            assertTrue(interactorsExpected.contains(interactor.getInteractorAc()));
        }

    }

    /*
     * Expected interactors when queried and filtered by multiple interaction detection method
     **/
    @Test
    public void filterByMultipleDetectionMethods() {


        Set<String> detectionMethods = new HashSet<>();
        detectionMethods.add("density sedimentation");
        detectionMethods.add("molecular sieving");
        GroupPage<SearchChildInteractor> interactorOp = childInteractorSearchService.findInteractorsWithGroup(
                "physical association",
                false,
                null,
                null,
                detectionMethods,
                null,
                null,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);
        assertEquals(4, interactorOp.getTotalElements());

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(
                "physical association",
                false,
                null,
                null,
                detectionMethods,
                null,
                null,
                false,
                false,
                0,
                1,
                false,
                null,
                null);

        assertEquals(4, numInteractors);

        Set<String> interactorsExpected = new HashSet<>();
        interactorsExpected.add("EBI-715849");
        interactorsExpected.add("EBI-724102");
        interactorsExpected.add("EBI-999909");
        interactorsExpected.add("EBI-999900");

        for (SearchChildInteractor interactor : interactorOp.getContent()) {
            assertTrue(interactorsExpected.contains(interactor.getInteractorAc()));
        }

    }
}
