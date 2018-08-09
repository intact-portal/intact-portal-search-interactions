package uk.ac.ebi.intact.search.interactions.service;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.model.Interaction;
import uk.ac.ebi.intact.search.interactions.service.util.RequiresSolrServer;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * @author Elisabet Barrera
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InteractionIndexServiceTest {

    private Interaction interaction1;
    private Interaction interaction2;

    @Resource
    private InteractionIndexService interactionIndexService;

    @Resource
    private InteractionSearchService interactionSearchService;

    public static @ClassRule
    RequiresSolrServer requiresRunningServer = RequiresSolrServer.onLocalhost();

    @Before
    public void setUp() throws Exception {

        //Delete all documents from solr core
        interactionIndexService.deleteAll();

        //Create new interactors documents
        interaction1 = new Interaction(new LinkedHashSet(Arrays.asList("pa et al.")),
                50,
                new HashSet(Arrays.asList("interaction_id1", "interaction_id2", "interaction_id3")),
                "publication_1");
        interaction2 = new Interaction(new LinkedHashSet(Arrays.asList("Ma et al.")),
                50,
                new HashSet(Arrays.asList("interaction_id1", "interaction_id2", "interaction_id3")),
                        "publication_1");
    }

    @Test
    public void triggerSchemaUpdateOnFirstSave() {
        interactionIndexService.save(interaction1);

        Optional<Interaction> interactionOp = interactionSearchService.findBy("pa et al.");
        Interaction interaction=interactionOp.get();
        assertEquals(interaction.getAuthors(), interaction1.getAuthors());
        assertEquals(interactionSearchService.countDocuments(), 1);
    }

    @Test
    public void triggerSchemaUpdateOnSaveCollection() {
        // empty collection
        interactionIndexService.deleteAll();

        interactionIndexService.save(Arrays.asList(interaction1, interaction2));
        assertEquals(interactionSearchService.countDocuments(), 2);
    }

    @Test
    public void deleteCollection() {
        // empty collection
        interactionIndexService.deleteAll();
        assertEquals(interactionSearchService.countDocuments(), 0);

    }

}