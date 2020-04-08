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
     * Expected number of interactors when interactions are queried by species
     */
    @Test
    public void getUniqueChildInteractorsFromInteractionQuery() {
        GroupPage<SearchChildInteractor> page = childInteractorSearchService.findInteractorsWithGroup("Rattus norvegicus (Rat)",
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
        assertEquals(5, page.getTotalElements());
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
        Assert.assertEquals(2, childInteractorsOp.getTotalElements());

        List<String> interactorAcs = new ArrayList<>();
        boolean correctChildInteractors = true;
        interactorAcs.add("EBI-724102");
        interactorAcs.add("EBI-715849");


        for (SearchChildInteractor searchChildInteractor : childInteractorsOp.getContent()) {
            if (!interactorAcs.contains(searchChildInteractor.getInteractorAc())) {
                correctChildInteractors = false;
            }
        }

        Assert.assertEquals(true, correctChildInteractors);

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
        assertEquals(5, childInteractorsOp.getTotalElements());

        List<String> interactorAcs = new ArrayList<>();
        boolean correctChildInteractors = true;
        interactorAcs.add("EBI-7837133");
        interactorAcs.add("EBI-915507");
        interactorAcs.add("EBI-2028244");
        interactorAcs.add("EBI-4423297");
        interactorAcs.add("EBI-9997695");

        for (SearchChildInteractor searchChildInteractor : childInteractorsOp.getContent()) {
            if (!interactorAcs.contains(searchChildInteractor.getInteractorAc())) {
                correctChildInteractors = false;
            }
        }
        Assert.assertEquals(true, correctChildInteractors);

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
        assertEquals(4, searchInteractionsOp.getTotalElements());

        boolean correctInteractions = true;
        String interactionAc = "EBI-10000796";

        for (SearchInteraction searchInteraction : searchInteractionsOp.getContent()) {
            if (!interactionAc.equals(searchInteraction.getAc())) {
                correctInteractions = false;
            }
        }
        Assert.assertEquals(true, correctInteractions);
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
        Assert.assertEquals(10, childInteractorsOp.getNumberOfElements());

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
        Assert.assertEquals(10, childInteractorsOp.getNumberOfElements());

    }

}
