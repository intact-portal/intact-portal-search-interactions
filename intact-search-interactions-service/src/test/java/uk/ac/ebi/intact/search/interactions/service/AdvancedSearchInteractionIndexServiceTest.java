package uk.ac.ebi.intact.search.interactions.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.utils.DocumentType;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Elisabet Barrera
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdvancedSearchInteractionIndexServiceTest {

    private SearchInteraction searchInteraction1;
    private SearchInteraction searchInteraction2;

    @Resource
    private InteractionIndexService interactionIndexService;

    @Resource
    private InteractionSearchService interactionSearchService;

    @Before
    public void setUp() {

        //Create new interaction documents
        searchInteraction1 = new SearchInteraction();

        searchInteraction1.setAc("interaction_c1");
        searchInteraction1.setDocumentType(DocumentType.INTERACTION);
        searchInteraction1.setAuthors(new LinkedHashSet<>(Collections.singletonList("Pa et al.")));
        searchInteraction1.setCount(50);
        searchInteraction1.setIdentifiers(
                new HashSet<>(Arrays.asList("interaction_id1", "interaction_id2", "interaction_id3")));
        searchInteraction1.setPublicationIdentifiers(
                new HashSet<>(Collections.singletonList("publication_1")));
        searchInteraction1.setPublicationPubmedIdentifier("unassigned1");
        searchInteraction1.setAsIdA(new HashSet<>(Arrays.asList("P12345", "EBI-12345")));
        searchInteraction1.setAsIdB(new HashSet<>(Arrays.asList("O12345", "EBI-22345")));

        searchInteraction2 = new SearchInteraction();
        List<SearchChildInteractor> searchChildInteractors2 = new ArrayList<>();

        searchInteraction2.setAc("interaction_c2 ");
        searchInteraction2.setDocumentType(DocumentType.INTERACTION);
        searchInteraction2.setAuthors(new LinkedHashSet<>(Collections.singletonList("Ma et al.")));
        searchInteraction2.setCount(50);
        searchInteraction2.setIdentifiers(
                new HashSet<>(Arrays.asList("interaction_id4", "interaction_id5", "interaction_id6")));
        searchInteraction2.setPublicationIdentifiers(
                new HashSet<>(Collections.singletonList("publication_2")));
        searchInteraction2.setPublicationPubmedIdentifier("unassigned2");
        searchInteraction2.setAsIdA(new HashSet<>(Arrays.asList("P6789", "EBI-6789")));// all interactor A identifiers
        searchInteraction2.setAsIdB(new HashSet<>(Arrays.asList("O6789", "EBI-7789")));// all interactor B identifiers

    }

    @After
    public void tearDown() {
        interactionIndexService.deleteAll();
    }

    @Test
    public void triggerSchemaUpdateOnFirstSave() {
        interactionIndexService.save(searchInteraction1);

        Optional<SearchInteraction> interactionOp = interactionSearchService.findByInteractionAc("interaction_c1");
        SearchInteraction interaction = interactionOp.get();
        assertEquals(interaction.getAuthors(), searchInteraction1.getAuthors());
        assertEquals(1, interactionSearchService.countTotal());
    }

    @Test
    public void triggerSchemaUpdateOnSaveCollection() {
        // empty collection
        interactionIndexService.deleteAll();

        interactionIndexService.save(Arrays.asList(searchInteraction1, searchInteraction2), Duration.ofMillis(100));
        assertEquals(2, interactionSearchService.countTotal());
    }

    @Test
    public void deleteCollection() {
        // empty collection
        interactionIndexService.deleteAll();
        assertEquals(0, interactionSearchService.countTotal());
    }

}
