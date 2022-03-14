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
import uk.ac.ebi.intact.search.interactions.utils.as.converters.TextFieldConverter;
import uk.ac.ebi.intact.search.interactions.utils.as.converters.XrefFieldConverter;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static uk.ac.ebi.intact.search.interactions.service.util.TestUtil.merge;

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
        searchInteraction1.setAsAltidA(merge(XrefFieldConverter.indexFieldValues("uniprotkb", "P12345"),
                XrefFieldConverter.indexFieldValues("intact", "EBI-12345")));// all interactor A identifiers
        searchInteraction1.setAsAltidB(merge(XrefFieldConverter.indexFieldValues("uniprotkb", "O12345"),
                XrefFieldConverter.indexFieldValues("intact", "EBI-22345")));// all interactor B identifiers
        searchInteraction1.setAsIdA(XrefFieldConverter.indexFieldValues("db1", "preferred-identifier1"));// preferred identifier
        searchInteraction1.setAsIdB(XrefFieldConverter.indexFieldValues("db1", "preferred-identifier2"));// preferred identifier
        searchInteraction1.setAsAliasA(merge(XrefFieldConverter.indexFieldValues(null, "alias1"),
                XrefFieldConverter.indexFieldValues(null, "alias2")));
        searchInteraction1.setAsAliasB(merge(XrefFieldConverter.indexFieldValues(null, "alias3"),
                XrefFieldConverter.indexFieldValues(null, "alias4")));
        searchInteraction1.setAsPubId(XrefFieldConverter.indexFieldValues("pubmed", "12345/678.9"));
        searchInteraction1.setAsInteractionXrefs(merge(XrefFieldConverter.indexFieldValues("intact", "EBI-123456"),
                XrefFieldConverter.indexFieldValues("imex", "IM-12345-1")));
        searchInteraction1.setAsTaxIdA(merge(TextFieldConverter.indexFieldValues("taxid", "9606", "Human"),//short name
                TextFieldConverter.indexFieldValues("taxid", "9606", "Homo Sapiens")));//full name
        searchInteraction1.setAsTaxIdB(merge(TextFieldConverter.indexFieldValues("taxid", "9606", "Human test text"),
                TextFieldConverter.indexFieldValues("taxid", "9606", "Homo Sapiens test text")));
        searchInteraction1.setAsHostOrganism(merge(TextFieldConverter.indexFieldValues("taxid", "2345678", "organism4 short name"),
                TextFieldConverter.indexFieldValues("taxid", "2345678", "organism4 full name")));
        searchInteraction1.setAsIDetectionMethod(TextFieldConverter.indexFieldValues("psi-mi", "MI:21234", "DetectionMethod1 shortlabel"));
        searchInteraction1.setAsPubAuthors(merge(TextFieldConverter.indexFieldValues(null, null, "Name1 I.nitials1 et al."),
                TextFieldConverter.indexFieldValues(null, null, "Name2 I.nitials2 et al.")));
        searchInteraction1.setAsPubFirstAuthor(TextFieldConverter.indexFieldValues(null, null, "FirstAuthor1 I.nitials1 et al."));
        searchInteraction1.setAsPubYear(2012);
        searchInteraction1.setAsType(TextFieldConverter.indexFieldValues("psi-mi", "MI:1234", "Type1 shortlabel"));

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
        searchInteraction2.setAsAltidA(merge(XrefFieldConverter.indexFieldValues("uniprotkb", "P6789"),
                XrefFieldConverter.indexFieldValues("intact", "EBI-6789")));// all interactor A identifiers
        searchInteraction2.setAsAltidB(merge(XrefFieldConverter.indexFieldValues("uniprotkb", "O6789"),
                XrefFieldConverter.indexFieldValues("intact", "EEBI-7789")));// all interactor B identifiers
        searchInteraction2.setAsIdA(XrefFieldConverter.indexFieldValues("db2", "preferred-identifier3"));// preferred identifier
        searchInteraction2.setAsIdB(XrefFieldConverter.indexFieldValues("db2", "preferred-identifier4"));// preferred identifier
        searchInteraction2.setAsAliasA(merge(XrefFieldConverter.indexFieldValues(null, "alias5"),
                XrefFieldConverter.indexFieldValues(null, "alias6")));
        searchInteraction2.setAsAliasB(merge(XrefFieldConverter.indexFieldValues(null, "alias7"),
                XrefFieldConverter.indexFieldValues(null, "alias8")));
        searchInteraction2.setAsPubId(XrefFieldConverter.indexFieldValues("pubmed", "12345678"));
        searchInteraction2.setAsInteractionXrefs(merge(XrefFieldConverter.indexFieldValues("intact", "EBI-1234567"),
                XrefFieldConverter.indexFieldValues("imex", "IM-123456-1")));
        searchInteraction2.setAsTaxIdA(merge(TextFieldConverter.indexFieldValues("taxid", "10116", "organism1 short name"),
                TextFieldConverter.indexFieldValues("taxid", "10116", "organism1 full name")));
        searchInteraction2.setAsTaxIdB(merge(TextFieldConverter.indexFieldValues("taxid", "12345", "organism2 short name"),
                TextFieldConverter.indexFieldValues("taxid", "12345", "organism2 full name")));
        searchInteraction2.setAsHostOrganism(merge(TextFieldConverter.indexFieldValues("taxid", "345678", "organism3 short name"),
                TextFieldConverter.indexFieldValues("taxid", "345678", "organism3 full name")));
        searchInteraction2.setAsIDetectionMethod(TextFieldConverter.indexFieldValues("psi-mi", "MI:41234", "DetectionMethod2 shortlabel"));
        searchInteraction2.setAsPubAuthors(merge(TextFieldConverter.indexFieldValues(null, null, "Name3 I.nitials3 et al."),
                TextFieldConverter.indexFieldValues(null, null, "Name2 I.nitials2 et al.")));
        searchInteraction2.setAsPubFirstAuthor(TextFieldConverter.indexFieldValues(null, null, "FirstAuthor2 I.nitials2 et al."));
        searchInteraction2.setAsPubYear(2015);
        searchInteraction2.setAsType(TextFieldConverter.indexFieldValues("psi-mi", "MI:12345", "Type2 shortlabel"));
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
