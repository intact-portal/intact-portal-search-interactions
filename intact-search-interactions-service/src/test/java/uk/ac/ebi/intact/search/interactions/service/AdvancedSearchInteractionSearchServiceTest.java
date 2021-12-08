package uk.ac.ebi.intact.search.interactions.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.service.util.TestUtil;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/**
 * @author Elisabet Barrera
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdvancedSearchInteractionSearchServiceTest {

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
        Iterator<SearchInteraction> searchInteractionIterator = searchInteractions.iterator();
        searchInteractionIterator.next().setAsIdA(new HashSet<>(Arrays.asList("P12345", "EBI-12345")));
        searchInteractionIterator.next().setAsIdA(new HashSet<>(Arrays.asList("P123456", "EBI-123456")));
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
    public void findByAsIdA() {
        Page<SearchInteraction> interactionPage = interactionSearchService.findInteractionsByAdvancedQuery("P12345");
        assertEquals(1, interactionPage.getTotalElements());
        assertEquals(1, interactionPage.getNumberOfElements());

        Page<SearchInteraction> interactionPage2 = interactionSearchService.findInteractionsByAdvancedQuery("P12345*");
        assertEquals(2, interactionPage2.getTotalElements());
        assertEquals(2, interactionPage2.getNumberOfElements());

        Page<SearchInteraction> interactionPage3 = interactionSearchService.findInteractionsByAdvancedQuery("EBI-12345");
        assertEquals(1, interactionPage3.getTotalElements());
        assertEquals(1, interactionPage3.getNumberOfElements());

        Page<SearchInteraction> interactionPage4 = interactionSearchService.findInteractionsByAdvancedQuery("EBI-12345*");
        assertEquals(2, interactionPage4.getTotalElements());
        assertEquals(2, interactionPage4.getNumberOfElements());
    }


}