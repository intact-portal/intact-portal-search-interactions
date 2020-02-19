package uk.ac.ebi.intact.search.interactions.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.utils.Constants;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Elisabet Barrera
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InteractionIndexServiceTest {

    private SearchInteraction searchInteraction1;
    private SearchInteraction searchInteraction2;

    @Resource
    private InteractionIndexService interactionIndexService;

    @Resource
    private InteractionSearchService interactionSearchService;

    @Resource
    private ChildIInteractorSearchService childIInteractorSearchService;

    @Before
    public void setUp() {

        //Create new interactors documents
        searchInteraction1 = new SearchInteraction();
        List<SearchChildInteractor> searchChildInteractors1 = new ArrayList<>();

        searchInteraction1.setAc("interaction_c1");
        searchInteraction1.setDocumentType(Constants.INTERACTION_DOCUMENT_TYPE_VALUE);
        searchInteraction1.setAuthors(new LinkedHashSet<>(Collections.singletonList("Pa et al.")));
        searchInteraction1.setCount(50);
        searchInteraction1.setIdentifiers(
                new HashSet<>(Arrays.asList("interaction_id1", "interaction_id2", "interaction_id3")));
        searchInteraction1.setPublicationIdentifiers(
                new HashSet<>(Collections.singletonList("publication_1")));

        //Create new child interactors documents
        SearchChildInteractor searchChildInteractor1 = new SearchChildInteractor("P06730",
                "Eukaryotic translation initiation factor 4E",
                "EIF4E",
                new HashSet<>(Arrays.asList("interactor1_alias1", "interactor1_alias2", "interactor1_alias3")),
                new HashSet<>(Arrays.asList("interactor1_alt1", "interactor1_alt2", "interactor1_alt3")),
                "protein",
                "Homo sapiens (Human)",
                9606,
                new HashSet<>(Arrays.asList("interactor1_xref1", "interactor1_xref2")),
                2,
                2L,
                new HashSet<>(Arrays.asList("interaction1", "interaction2")),
                new HashSet<>(Arrays.asList("detectionMethod1", "detectionMethod2")),
                new HashSet<>(Arrays.asList("interactionType1", "interactionType2")),
                new HashSet<>(Arrays.asList("expansionMethod1", "expansionMethod2")),
                new HashSet<>(Arrays.asList(true, true, false)),
                new HashSet<>(Arrays.asList(0.33, 0.52)),
                new HashSet<>(Arrays.asList("in vitro", "Saccharomyces cerevisiae")),
                new HashSet<>(Arrays.asList("featureShortLabel1", "featureShortLabel2")), Constants.INTERACTOR_DOCUMENT_TYPE_VALUE);


        SearchChildInteractor searchChildInteractor2 = new SearchChildInteractor("Q13541",
                "Eukaryotic translation initiation factor 4E-binding protein 1",
                "4EBP1",
                new HashSet<>(Arrays.asList("interactor2_alias1", "interactor2_alias1", "interactor2_alias3")),
                new HashSet<>(Arrays.asList("interactor2_alt1", "interactor2_alt1")),
                "protein",
                "Mus musculus (mouse)",
                10090,
                new HashSet<>(Arrays.asList("interactor2_xref1", "interactor2_xref2")),
                2,
                1L,
                new HashSet<>(Arrays.asList("interaction1", "interaction2")),
                new HashSet<>(Arrays.asList("detectionMethod1", "detectionMethod2")),
                new HashSet<>(Arrays.asList("interactionType1", "interactionType2")),
                new HashSet<>(Arrays.asList("expansionMethod1", "expansionMethod2")),
                new HashSet<>(Arrays.asList(true, true, false)),
                new HashSet<>(Arrays.asList(0.33, 0.52)),
                new HashSet<>(Arrays.asList("in vitro", "Saccharomyces cerevisiae")),
                new HashSet<>(Arrays.asList("featureshortlabel1", "featureshortlabel2")), Constants.INTERACTOR_DOCUMENT_TYPE_VALUE);
        searchChildInteractors1.add(searchChildInteractor1);
        searchChildInteractors1.add(searchChildInteractor2);

        searchInteraction1.setSearchChildInteractors(searchChildInteractors1);

        searchInteraction2 = new SearchInteraction();
        List<SearchChildInteractor> searchChildInteractors2 = new ArrayList<>();

        searchInteraction2.setAc("interaction_c2 ");
        searchInteraction2.setDocumentType(Constants.INTERACTION_DOCUMENT_TYPE_VALUE);
        searchInteraction2.setAuthors(new LinkedHashSet<>(Collections.singletonList("Ma et al.")));
        searchInteraction2.setCount(50);
        searchInteraction2.setIdentifiers(
                new HashSet<>(Arrays.asList("interaction_id1", "interaction_id2", "interaction_id3")));
        searchInteraction2.setPublicationIdentifiers(
                new HashSet<>(Collections.singletonList("publication_1")));

        SearchChildInteractor searchChildInteractor3 = new SearchChildInteractor("P63165",
                "Small ubiquitin-related modifier 1",
                "SUMO1",
                new HashSet<>(Arrays.asList("interactor3_alias1", "interactor3_alias1", "interactor3_alias3")),
                new HashSet<>(Arrays.asList("interactor3_alt1", "interactor3_alt1")),
                "protein",
                "Homo sapiens (Human)",
                9606,
                new HashSet<>(Arrays.asList("interactor3_xref1", "interactor3_xref2", "interactor3_xref3", "interactor3_xref4")),
                5,
                3L,
                new HashSet<>(Arrays.asList("interaction1", "interaction2", "interaction3", "interaction4", "interaction5")),
                new HashSet<>(Arrays.asList("detectionMethod1", "detectionMethod2")),
                new HashSet<>(Arrays.asList("interactionType1", "interactionType2")),
                new HashSet<>(Arrays.asList("expansionMethod1", "expansionMethod2")),
                new HashSet<>(Arrays.asList(true, true, false)),
                new HashSet<>(Arrays.asList(0.33, 0.52)),
                new HashSet<>(Arrays.asList("Saccharomyces cerevisiae", "Homo sapiens")),
                new HashSet<>(Arrays.asList("featureshortlabel1", "featureshortlabel2")), Constants.INTERACTOR_DOCUMENT_TYPE_VALUE);

        SearchChildInteractor searchChildInteractor4 = new SearchChildInteractor("Q13541",
                "Eukaryotic translation initiation factor 4E-binding protein 1",
                "4EBP1",
                new HashSet<>(Arrays.asList("interactor2_alias1", "interactor2_alias1", "interactor2_alias3")),
                new HashSet<>(Arrays.asList("interactor2_alt1", "interactor2_alt1")),
                "protein",
                "Mus musculus (mouse)",
                10090,
                new HashSet<>(Arrays.asList("interactor2_xref1", "interactor2_xref2")),
                2,
                1L,
                new HashSet<>(Arrays.asList("interaction1", "interaction2")),
                new HashSet<>(Arrays.asList("detectionMethod1", "detectionMethod2")),
                new HashSet<>(Arrays.asList("interactionType1", "interactionType2")),
                new HashSet<>(Arrays.asList("expansionMethod1", "expansionMethod2")),
                new HashSet<>(Arrays.asList(true, true, false)),
                new HashSet<>(Arrays.asList(0.33, 0.52)),
                new HashSet<>(Arrays.asList("in vitro", "Saccharomyces cerevisiae")),
                new HashSet<>(Arrays.asList("featureshortlabel1", "featureshortlabel2")), Constants.INTERACTOR_DOCUMENT_TYPE_VALUE);

        searchChildInteractors2.add(searchChildInteractor3);
        searchChildInteractors2.add(searchChildInteractor4);

        searchInteraction2.setSearchChildInteractors(searchChildInteractors2);

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
        assertEquals(2, childIInteractorSearchService.countTotal());
    }

    @Test
    public void triggerSchemaUpdateOnSaveCollection() {
        // empty collection
        interactionIndexService.deleteAll();

        interactionIndexService.save(Arrays.asList(searchInteraction1, searchInteraction2), Duration.ofMillis(100));
        assertEquals(2, interactionSearchService.countTotal());
        assertEquals(4, childIInteractorSearchService.countTotal());
    }

    @Test
    public void deleteCollection() {
        // empty collection
        interactionIndexService.deleteAll();
        assertEquals(0, interactionSearchService.countTotal());
    }

}
