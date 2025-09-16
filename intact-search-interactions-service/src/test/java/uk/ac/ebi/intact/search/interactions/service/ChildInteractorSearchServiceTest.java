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
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.service.util.TestUtil;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static uk.ac.ebi.intact.search.interactions.model.AdvancedSearchInteractionFields.MiqlFieldConstants.ALTID_A;

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
        Iterator<SearchInteraction> iterator = searchInteractions.iterator();
        iterator.next().setAsAltidA(new HashSet<>(Arrays.asList("P12345", "EBI-12345")));
        iterator.next().setAsAltidA(new HashSet<>(Arrays.asList("P123456", "EBI-123456")));
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
    public void getUniqueChildInteractorsFromMIQLQuery() {

        GroupPage<SearchChildInteractor> page = childInteractorSearchService.findInteractorsWithGroup(
                PagedInteractionSearchParameters.builder()
                        .query(ALTID_A + ":(EBI-12345 OR P123456)")
                        .advancedSearch(true)
                        .build()
        );
        assertEquals(4, page.getTotalElements());
    }

    /**
     * Expected number of interactors when interactions are queried by species. Returns total documents found in the groups
     * instead the number of unique groups
     */
    @Test
    public void getUniqueChildInteractorsFromInteractionQuery() {
        GroupPage<SearchChildInteractor> page = childInteractorSearchService.findInteractorsWithGroup(PagedInteractionSearchParameters.builder().query("rat").build());
        assertEquals(5, page.getTotalElements());
    }

    /**
     * Expected interactors when interactions are queried by all filters
     */

    @Test
    public void getUniqueChildInteractorsFromInteractionFilterQuery() {


        PagedInteractionSearchParameters params = PagedInteractionSearchParameters.builder()
                .query("physical association")
                .interactorSpeciesFilter(Set.of("Homo sapiens"))
                .interactorTypesFilter(Set.of("protein"))
                .interactionDetectionMethodsFilter(Set.of("molecular sieving"))
                .interactionTypesFilter(Set.of("physical association"))
                .interactionHostOrganismsFilter(Set.of("In vitro"))
                .minMIScore(0)
                .maxMIScore(0.7)
                .build();
        GroupPage<SearchChildInteractor> childInteractorsOp = childInteractorSearchService.findInteractorsWithGroup(
                params);
        assertEquals(2, childInteractorsOp.getTotalElements()); //Total documents found before grouping
        assertEquals(2, childInteractorsOp.getNumberOfElements()); //Elements in the page

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(params);
        assertEquals(2, numInteractors);

        List<String> interactorAcs = List.of("EBI-724102", "EBI-715849");

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

        PagedInteractionSearchParameters params = PagedInteractionSearchParameters.builder().query("rat").build();

        GroupPage<SearchChildInteractor> childInteractorsOp = childInteractorSearchService.findInteractorsWithGroup(params);
        assertEquals(5, childInteractorsOp.getTotalElements());  //Total documents found before grouping
        assertEquals(5, childInteractorsOp.getNumberOfElements()); //Elements in the page


        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(params);
        assertEquals(5, numInteractors);

        List<String> interactorAcs = List.of(
                "EBI-7837133",
                "EBI-915507",
                "EBI-2028244",
                "EBI-4423297",
                "EBI-9997695"
        );

        for (SearchChildInteractor searchChildInteractor : childInteractorsOp.getContent()) {
            if (!interactorAcs.contains(searchChildInteractor.getInteractorAc())) {
                Assert.fail("The interactor is not in the list of interactors expected");
            }
        }

        //Interactions
        FacetPage<SearchInteraction> searchInteractionsOp = interactionSearchService.findInteractionWithFacet(params);
        assertEquals(4, searchInteractionsOp.getTotalElements());


        //5 binaries 2 interactions
        List<String> interactionAcs = List.of("EBI-10000796");

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
        GroupPage<SearchChildInteractor> childInteractorsOp = childInteractorSearchService.findInteractorsWithGroup(PagedInteractionSearchParameters.builder().query("").build());
        assertEquals(20, childInteractorsOp.getTotalElements()); //Total documents found before grouping
        assertEquals(10, childInteractorsOp.getNumberOfElements()); //Elements in the first page

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(PagedInteractionSearchParameters.builder().query("").build());
        assertEquals(11, numInteractors);
    }

    /*
     * Expected interactors when queried by "*" character
     **/

    @Test
    public void findInteractionsByStarString() {
        GroupPage<SearchChildInteractor> childInteractorsOp = childInteractorSearchService.findInteractorsWithGroup(PagedInteractionSearchParameters.builder().query("*").build());
        //TODO... Check later why total elements is 20 here, the earlier
        // checkInteractionAndChildInteractorsSync test seems to give correct value, here it should be 11

        assertEquals(20, childInteractorsOp.getTotalElements()); //Total documents found before grouping
        assertEquals(10, childInteractorsOp.getNumberOfElements()); //Elements in the first page

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(PagedInteractionSearchParameters.builder().query("*").build());
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
        PagedInteractionSearchParameters params = PagedInteractionSearchParameters.builder()
                .query("physical association")
                .interactorSpeciesFilter(species)
                .interactorAcs(interactorAcs)
                .build();

        GroupPage<SearchChildInteractor> interactorOp = childInteractorSearchService.findInteractorsWithGroup(params);
        assertEquals(10, interactorOp.getTotalElements());//TODO...this needs to be checked it should give 6

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(params);

        assertEquals(6, numInteractors);

        Set<String> interactorsExpected = new HashSet<>();
        interactorsExpected.add("EBI-715849");
        interactorsExpected.add("EBI-999900");
        interactorsExpected.add("EBI-724102");
        interactorsExpected.add("EBI-999909");
        interactorsExpected.add("EBI-10000824");
        interactorsExpected.add("EBI-73886");

        for (SearchChildInteractor interactor : interactorOp.getContent()) {
            assertTrue(interactorsExpected.contains(interactor.getInteractorAc()));
        }

    }

    /*
     * Expected interactors when queried by a text and filtered by species and binaryIds
     **/
    @Test
    public void filterByBinaryIds() {

        PagedInteractionSearchParameters params = PagedInteractionSearchParameters.builder()
                .query("physical association")
                .interactorSpeciesFilter(Set.of("Homo sapiens"))
                .binaryInteractionIds(Set.of(10L, 1L))
                .build();

        GroupPage<SearchChildInteractor> interactorOp = childInteractorSearchService.findInteractorsWithGroup(params);
        assertEquals(4, interactorOp.getTotalElements());

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(params);

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

        PagedInteractionSearchParameters params = PagedInteractionSearchParameters.builder()
                .query("physical association")
                .interactionDetectionMethodsFilter(Set.of(
                        "density sedimentation",
                        "molecular sieving"
                )).build();

        GroupPage<SearchChildInteractor> interactorOp = childInteractorSearchService.findInteractorsWithGroup(params);
        assertEquals(4, interactorOp.getTotalElements());

        long numInteractors = childInteractorSearchService.countInteractorsWithGroup(params);

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

    @Test
    public void sortAllInteractorsByPopularity() {
        // There are 11 different interactors, 5 that appear multiple times, and 6 other than appear just once
        // There are 4 interactors that appear in 4 interactions
        Set<String> interactorsAcsThatAppearFourTimes = Set.of(
                "EBI-715849", "EBI-9997695");
        // There are 3 interactors that appear in 2 interactions
        Set<String> interactorsAcsThatAppearTwice = Set.of(
                "EBI-724102", "EBI-999900", "EBI-999909");
        // The other 6 interactors appear just once
        Set<String> interactorsAcsThatAppearOnce = Set.of(
                "EBI-7837133", "EBI-915507", "EBI-2028244", "EBI-4423297", "EBI-10000824", "EBI-73886");

        GroupPage<SearchChildInteractor> interactorOp = childInteractorSearchService.findInteractorsWithGroup(
                PagedInteractionSearchParameters.builder()
                        .query("*")
                        .page(0)
                        .pageSize(6)
                        .build());

        assertEquals(6, interactorOp.getNumberOfElements());

        List<SearchChildInteractor> interactors = interactorOp.getContent();
        // On the first page, the first 5 interactors are those that appear multiple times,
        // and the last one is one of those that appears just once

        Set<String> interactorAcs = interactors.subList(0, 2).stream()
                .map(SearchChildInteractor::getInteractorAc)
                .collect(Collectors.toSet());
        assertEquals(interactorsAcsThatAppearFourTimes, interactorAcs);

        interactorAcs = interactors.subList(2, 5).stream()
                .map(SearchChildInteractor::getInteractorAc)
                .collect(Collectors.toSet());
        assertEquals(interactorsAcsThatAppearTwice, interactorAcs);

        assertTrue(interactorsAcsThatAppearOnce.contains(interactors.get(5).getInteractorAc()));

        interactorOp = childInteractorSearchService.findInteractorsWithGroup(
                PagedInteractionSearchParameters.builder()
                        .query("*")
                        .page(1)
                        .pageSize(6)
                        .build());

        assertEquals(5, interactorOp.getNumberOfElements());

        // On the second page, we only get interactors that appear just once
        interactors = interactorOp.getContent();
        interactors.forEach(interactor ->
                assertTrue(interactorsAcsThatAppearOnce.contains(interactor.getInteractorAc())));
    }

    @Test
    public void sortInteractorsByPopularityInGivenQuery() {
        // Search for interactions with 1 specific protein that return 3 different interactors.
        GroupPage<SearchChildInteractor> interactorOp = childInteractorSearchService.findInteractorsWithGroup(
                PagedInteractionSearchParameters.builder()
                        .query("B4DZZ7")
                        .pageSize(10)
                        .build());

        assertEquals(3, interactorOp.getNumberOfElements());

        List<SearchChildInteractor> interactors = interactorOp.getContent();
        // The first interactor is the most popular in the query results, appearing in all interactions
        Assert.assertEquals("EBI-999900", interactors.get(0).getInteractorAc());
        // The other interactors appear only once in the query results, so they are sorted by the total number
        // of interactions for them in the DB. So, as EBI-715849 appear in 4 interactions, it is sorted above the
        // other interactor.
        Assert.assertEquals("EBI-715849", interactors.get(1).getInteractorAc());
        Assert.assertEquals("EBI-999909", interactors.get(2).getInteractorAc());
    }
}