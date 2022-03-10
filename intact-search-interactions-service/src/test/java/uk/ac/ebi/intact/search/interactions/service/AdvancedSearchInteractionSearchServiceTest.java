package uk.ac.ebi.intact.search.interactions.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.utils.DocumentType;
import uk.ac.ebi.intact.search.interactions.utils.as.converters.TextFieldConverter;
import uk.ac.ebi.intact.search.interactions.utils.as.converters.XrefFieldConverter;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.Assert.*;
import static uk.ac.ebi.intact.search.interactions.model.AdvancedSearchInteractionFields.MiqlFieldConstants.*;
import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.DETECTION_METHOD_S;
import static uk.ac.ebi.intact.search.interactions.service.util.TestUtil.merge;

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
        SearchInteraction searchInteraction1 = new SearchInteraction();

        searchInteraction1.setAc("interaction_c1");
        searchInteraction1.setDocumentType(DocumentType.INTERACTION);
        searchInteraction1.setAuthors(new LinkedHashSet<>(Collections.singletonList("Pa et al.")));
        searchInteraction1.setCount(50);
        searchInteraction1.setIdentifiers(
                new HashSet<>(Arrays.asList("interaction_id1", "interaction_id2", "interaction_id3")));
        searchInteraction1.setPublicationIdentifiers(
                new HashSet<>(Collections.singletonList("publication_1")));
        searchInteraction1.setPublicationPubmedIdentifier("unassigned1");
        searchInteraction1.setDetectionMethod("detection_method1");
        searchInteraction1.setAsAltidA(merge(XrefFieldConverter.indexFieldValues("uniprotkb", "P12345"),
                XrefFieldConverter.indexFieldValues("intact", "EBI-12345")));// all interactor A identifiers
        searchInteraction1.setAsAltidB(merge(XrefFieldConverter.indexFieldValues("uniprotkb", "O12345"),
                XrefFieldConverter.indexFieldValues("intact", "EBI-22345")));// all interactor B identifiers
        searchInteraction1.setAsIdA(XrefFieldConverter.indexFieldValues("db1", "preferred-identifier1"));// preferred identifier
        searchInteraction1.setAsIdB(XrefFieldConverter.indexFieldValues("db1", "preferred-identifier2"));// preferred identifier
        searchInteraction1.setAsAliasA((merge(XrefFieldConverter.indexFieldValues(null, "some text of alias1"),
                XrefFieldConverter.indexFieldValues(null, "alias2"))));
        searchInteraction1.setAsAliasB(merge(XrefFieldConverter.indexFieldValues(null, "some text of alias3"),
                XrefFieldConverter.indexFieldValues(null, "alias4")));
        searchInteraction1.setAsPubId(XrefFieldConverter.indexFieldValues("pubmed", "12345/678.9"));
        searchInteraction1.setAsInteractionXrefs(merge(XrefFieldConverter.indexFieldValues("intact", "EBI-123456"),
                XrefFieldConverter.indexFieldValues("imex", "IM-12345-1")));
        searchInteraction1.setAsTaxIdA(merge(TextFieldConverter.indexFieldValues("taxid", "9606", "Human"),//short name
                TextFieldConverter.indexFieldValues("taxid", "9606", "Homo Sapiens")));//full name
        searchInteraction1.setAsTaxIdB(merge(TextFieldConverter.indexFieldValues("taxid", "9606", "Human test text"),
                TextFieldConverter.indexFieldValues("taxid", "9606", "Homo Sapiens test text")));
        searchInteraction1.setAsPubAuthors(merge(TextFieldConverter.indexFieldValues(null, null, "Name1 I.nitials1 et al."),
                TextFieldConverter.indexFieldValues(null, null, "Name2 I.nitials2 et al.")));

        SearchInteraction searchInteraction2 = new SearchInteraction();
        List<SearchChildInteractor> searchChildInteractors2 = new ArrayList<>();

        searchInteraction2.setAc("interaction_c2");
        searchInteraction2.setDocumentType(DocumentType.INTERACTION);
        searchInteraction2.setAuthors(new LinkedHashSet<>(Collections.singletonList("Ma et al.")));
        searchInteraction2.setCount(50);
        searchInteraction2.setIdentifiers(
                new HashSet<>(Arrays.asList("interaction_id4", "interaction_id5", "interaction_id6")));
        searchInteraction2.setPublicationIdentifiers(
                new HashSet<>(Collections.singletonList("publication_2")));
        searchInteraction2.setPublicationPubmedIdentifier("unassigned2");
        searchInteraction2.setDetectionMethod("detection_method2");
        searchInteraction2.setAsAltidA(merge(XrefFieldConverter.indexFieldValues("uniprotkb", "P123456"),
                XrefFieldConverter.indexFieldValues("intact", "EBI-123456")));// all interactor A identifiers
        searchInteraction2.setAsAltidB(merge(XrefFieldConverter.indexFieldValues("uniprotkb", "O123456"),
                XrefFieldConverter.indexFieldValues("intact", "EBI-223456")));// all interactor B identifiers
        searchInteraction2.setAsIdA(XrefFieldConverter.indexFieldValues("db2", "preferred-identifier3"));// preferred identifier
        searchInteraction2.setAsIdB(XrefFieldConverter.indexFieldValues("db2", "preferred-identifier4"));// preferred identifier
        searchInteraction2.setAsAliasA(merge(XrefFieldConverter.indexFieldValues(null, "alias1"),
                XrefFieldConverter.indexFieldValues(null, "alias6")));
        searchInteraction2.setAsAliasB(merge(XrefFieldConverter.indexFieldValues(null, "alias3"),
                XrefFieldConverter.indexFieldValues(null, "alias8")));
        searchInteraction2.setAsPubId(XrefFieldConverter.indexFieldValues("pubmed", "12345678"));
        searchInteraction2.setAsInteractionXrefs(merge(XrefFieldConverter.indexFieldValues("intact", "EBI-1234567"),
                XrefFieldConverter.indexFieldValues("imex", "IM-123456-1")));
        searchInteraction2.setAsTaxIdA(merge(TextFieldConverter.indexFieldValues("taxid", "10116", "organism1 short name"),
                TextFieldConverter.indexFieldValues("taxid", "10116", "organism1 full name")));
        searchInteraction2.setAsTaxIdB(merge(TextFieldConverter.indexFieldValues("taxid", "12345", "organism2 short name"),
                TextFieldConverter.indexFieldValues("taxid", "12345", "organism2 full name")));
        searchInteraction2.setAsPubAuthors(merge(TextFieldConverter.indexFieldValues(null, null, "Name3 I.nitials3 et al."),
                TextFieldConverter.indexFieldValues(null, null, "Name2 I.nitials2 et al.")));

        interactionIndexService.save(searchInteraction1);
        interactionIndexService.save(searchInteraction2);
        assertEquals(2, interactionSearchService.countTotal());
    }

    @After
    public void tearDown() {
        interactionIndexService.deleteAll();
    }

    /**
     * Behaviour If the User executes "altidA miql query"
     * this test is for all fields with text_intact type in solr
     */
    @Test
    public void findByAsAltIdA() {
        FacetPage<SearchInteraction> interactionFacetPage1 = interactionSearchService.findInteractionWithFacet(
                ALTID_A + ":P12345",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage1.getContent().isEmpty());
        assertEquals(1, interactionFacetPage1.getContent().size());
        assertEquals(1, interactionFacetPage1.getNumberOfElements());
        assertEquals(0, interactionFacetPage1.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage1.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage1.getTotalElements());


        FacetPage<SearchInteraction> interactionFacetPage2 = interactionSearchService.findInteractionWithFacet(
                ALTID_A + ":P12345*",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage2.getContent().isEmpty());
        assertEquals(2, interactionFacetPage2.getContent().size());
        assertEquals(2, interactionFacetPage2.getNumberOfElements());
        assertEquals(0, interactionFacetPage2.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage2.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage2.getTotalElements());

        //detection method facet checking
        HashMap<String, Long> detectionMethodsFacetsExpected = new HashMap<>();
        detectionMethodsFacetsExpected.put("detection_method1", 1L);
        detectionMethodsFacetsExpected.put("detection_method2", 1L);

        Page<FacetFieldEntry> facetFieldEntryPage = interactionFacetPage2.getFacetResultPage(DETECTION_METHOD_S);
        assertEquals(2, facetFieldEntryPage.getTotalElements());
        for (FacetFieldEntry facetFieldEntry : facetFieldEntryPage) {
            assertEquals(detectionMethodsFacetsExpected.get(facetFieldEntry.getValue()), new Long(facetFieldEntry.getValueCount()));
        }

        FacetPage<SearchInteraction> interactionFacetPage3 = interactionSearchService.findInteractionWithFacet(
                ALTID_A + ":EBI-12345",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage3.getContent().isEmpty());
        assertEquals(1, interactionFacetPage3.getContent().size());
        assertEquals(1, interactionFacetPage3.getNumberOfElements());
        assertEquals(0, interactionFacetPage3.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage3.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage3.getTotalElements());

        FacetPage<SearchInteraction> interactionFacetPage4 = interactionSearchService.findInteractionWithFacet(
                ALTID_A + ":(EBI-12345 P123456)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage4.getContent().isEmpty());
        assertEquals(2, interactionFacetPage4.getContent().size());
        assertEquals(2, interactionFacetPage4.getNumberOfElements());
        assertEquals(0, interactionFacetPage4.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage4.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage4.getTotalElements());

        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALTID_A + ":(EBI-12345 OR P123456)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        FacetPage<SearchInteraction> interactionFacetPage6 = interactionSearchService.findInteractionWithFacet(
                ALTID_A + ":(EBI-12345 AND P123456)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertTrue(interactionFacetPage6.getContent().isEmpty());
        assertEquals(0, interactionFacetPage6.getContent().size());
        assertEquals(0, interactionFacetPage6.getNumberOfElements());
        assertEquals(0, interactionFacetPage6.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage6.getPageable().getPageSize());
        assertEquals(0, interactionFacetPage6.getTotalElements());

        FacetPage<SearchInteraction> interactionFacetPage7 = interactionSearchService.findInteractionWithFacet(
                ALTID_A + ":(+EBI-12345 -P12345)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertTrue(interactionFacetPage7.getContent().isEmpty());
        assertEquals(0, interactionFacetPage7.getContent().size());
        assertEquals(0, interactionFacetPage7.getNumberOfElements());
        assertEquals(0, interactionFacetPage7.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage7.getPageable().getPageSize());
        assertEquals(0, interactionFacetPage7.getTotalElements());

        FacetPage<SearchInteraction> interactionFacetPage8 = interactionSearchService.findInteractionWithFacet(
                ALTID_A + ":EBI-12345 OR " + ALTID_A + ":P123456",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage8.getContent().isEmpty());
        assertEquals(2, interactionFacetPage8.getContent().size());
        assertEquals(2, interactionFacetPage8.getNumberOfElements());
        assertEquals(0, interactionFacetPage8.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage8.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage8.getTotalElements());

        FacetPage<SearchInteraction> interactionFacetPage9 = interactionSearchService.findInteractionWithFacet(
                ALTID_A + " :uniprotkb:P12345",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage9.getContent().isEmpty());
        assertEquals(1, interactionFacetPage9.getContent().size());
        assertEquals(1, interactionFacetPage9.getNumberOfElements());
        assertEquals(0, interactionFacetPage9.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage9.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage9.getTotalElements());
    }

    /**
     * Behaviour If the User executes "altidB miql query"
     */
    @Test
    public void findByAsAltIdB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALTID_B + ":(EBI-22345 OR O123456)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());
    }

    /**
     * Behaviour If the User executes "altidA,altidB miql query"
     */
    @Test
    public void findByAsAltIdAIdB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALTID_A + ":EBI-123456 AND " + ALTID_B + ":O123456",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c2", interactionFacetPage5.iterator().next().getAc());

    }

    /**
     * Behaviour If the User executes "id miql query"
     */
    @Test
    public void findByAsId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ID + ":EBI-123456 AND " + ID + ":O123456",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c2", interactionFacetPage5.iterator().next().getAc());

    }

    /**
     * Behaviour If the User executes "id miql query"
     */
    @Test
    public void findByAsIdA() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ID_A + ":preferred-identifier3",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c2", interactionFacetPage5.iterator().next().getAc());

    }

    /**
     * Behaviour If the User executes "id miql query"
     */
    @Test
    public void findByAsIdB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ID_B + ":preferred-identifier2",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());

    }

    /**
     * Behaviour If the User executes "aliasA miql query without quotes"
     */
    @Test
    public void findByAsAliasAWithoutQuotes() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALIAS_A + ":(some text of alias1)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
        assertEquals("interaction_c2", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "aliasA miql query without quotes"
     */
    @Test
    public void findByAsAliasAWithQuotes() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALIAS_A + ":\"some text of alias1\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());

    }

    /**
     * Behaviour If the User executes "aliasB miql query without quotes"
     */
    @Test
    public void findByAsAliasBWithoutQuotes() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALIAS_B + ":(some text of alias3)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
        assertEquals("interaction_c2", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "aliasB miql query without quotes"
     */
    @Test
    public void findByAsAliasBWithQuotes() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALIAS_B + ":\"some text of alias3\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());

    }

    /**
     * Behaviour If the User executes "alias miql query without quotes"
     */
    @Test
    public void findByAsAliasWithoutQuotes() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALIAS + ":(some text of alias3)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
        assertEquals("interaction_c2", iteractor.next().getAc());

    }

    /**
     * Behaviour If the User executes "alias miql query without quotes"
     */
    @Test
    public void findByAsAliasWithQuotes() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALIAS + ":\"some text of alias3\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());

    }

    /**
     * Behaviour If the User executes "alias miql query with a single term"
     */
    @Test
    public void findByAsAliasWithSingleTerm() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALIAS + ":alias6",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c2", interactionFacetPage5.iterator().next().getAc());

    }

    /**
     * Behaviour If the User executes "alias miql query with a single term with quotes"
     */
    @Test
    public void findByAsAliasWithSingleTermwithQuotes() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALIAS + ":\"alias3\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
        assertEquals("interaction_c2", iteractor.next().getAc());

    }


    /**
     * Behaviour If the User executes "alias miql query with aliasA And aliasB"
     */
    @Test
    public void findByAsAliasWithAliasAAndAliasB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                ALIAS + ":(alias3 AND alias1)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());
    }

    /**
     * Behaviour If the User executes "identifier miql query with aliasA"
     */
    @Test
    public void findByAsIdentifierWithAliasA() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFIER + ":alias6",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c2", interactionFacetPage5.iterator().next().getAc());

    }

    /**
     * Behaviour If the User executes "identifier miql query with aliasB"
     */
    @Test
    public void findByAsIdentifierWithAliasB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFIER + ":alias4",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());
    }

    /**
     * Behaviour If the User executes "identifier miql query with altIdA"
     */
    @Test
    public void findByAsIdentifierWithAltIdA() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFIER + ":P12345",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());
    }

    /**
     * Behaviour If the User executes "identifier miql query with altIdB"
     */
    @Test
    public void findByAsIdentifierWithAltIdB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFIER + ":O123456",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c2", interactionFacetPage5.iterator().next().getAc());
    }

    /**
     * Behaviour If the User executes "identifier miql query with altIdB and altIdA and aliasA and aliasB"
     */
    @Test
    public void findByAsIdentifierWithAltIdBAAndAltIdBAndAliasAAndAliasB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFIER + ":(P12345 AND O12345 AND alias1 AND alias3)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());
    }

    /**
     * Behaviour If the User executes "pubid miql query with db:id"
     */
    @Test
    public void findByAsPubIdWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                PUB_ID + ":pubmed:12345/678.9",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());
    }

    /**
     * Behaviour If the User executes "pubid miql query with db"
     */
    @Test
    public void findByAsPubIdWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                PUB_ID + ":pubmed",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
        assertEquals("interaction_c2", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "pubid miql query with id"
     */
    @Test
    public void findByAsPubIdWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                PUB_ID + ":12345/678.9",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "interaction_id miql query with db:id"
     */
    @Test
    public void findByAsInteractionIdWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTION_XREFS + ":intact:EBI-123456",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());
    }

    /**
     * Behaviour If the User executes "interaction_id miql query with db"
     */
    @Test
    public void findByAsInteractionIdWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTION_XREFS + ":imex",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
        assertEquals("interaction_c2", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "interaction_id miql query with id"
     */
    @Test
    public void findByAsInteractionIdWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTION_XREFS + ":IM-12345-1",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "taxidA miql query with db:id"
     */
    @Test
    public void findByAsTaxIdAWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TAX_ID_A + ":taxid:9606",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());
    }

    /**
     * Behaviour If the User executes "taxidA miql query with db"
     */
    @Test
    public void findByAsTaxIdAWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TAX_ID_A + ":taxid",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
        assertEquals("interaction_c2", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "taxidA miql query with id"
     */
    @Test
    public void findByAsTaxIdAWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TAX_ID_A + ":9606",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "taxidA miql query with text(short name)"
     */
    @Test
    public void findByAsTaxIdAWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TAX_ID_A + ":\"Human\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "taxidA miql query with text(full name)"
     */
    @Test
    public void findByAsTaxIdAWithOnlyFullName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TAX_ID_A + ":\"Homo Sapiens\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "taxidB miql query with db:id"
     */
    @Test
    public void findByAsTaxIdBWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TAX_ID_B + ":taxid:9606",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());
    }

    /**
     * Behaviour If the User executes "taxidB miql query with db"
     */
    @Test
    public void findByAsTaxIdBWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TAX_ID_B + ":taxid",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
        assertEquals("interaction_c2", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "taxidB miql query with id"
     */
    @Test
    public void findByAsTaxIdBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TAX_ID_B + ":9606",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "taxidB miql query with text(short name)"
     */
    @Test
    public void findByAsTaxIdBWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TAX_ID_B + ":\"Human test text\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "taxidB miql query with text(full name)"
     */
    @Test
    public void findByAsTaxIdBWithOnlyFullName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TAX_ID_B + ":\"Homo Sapiens test text\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "species miql query with db:id"
     */
    @Test
    public void findByAsSpeciesWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                SPECIES + ":taxid:9606",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        assertEquals("interaction_c1", interactionFacetPage5.iterator().next().getAc());
    }

    /**
     * Behaviour If the User executes "species miql query with db"
     */
    @Test
    public void findByAsSpeciesWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                SPECIES + ":taxid",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
        assertEquals("interaction_c2", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "species miql query with id"
     */
    @Test
    public void findByAsSpeciesWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                SPECIES + ":9606",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "species miql query with text(short name)"
     */
    @Test
    public void findByAsSpeciesWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                SPECIES + ":\"Human test text\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "species miql query with text(full name)"
     */
    @Test
    public void findByAsSpeciesWithOnlyFullName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                SPECIES + ":\"Homo Sapiens test text\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "species miql query with taxidA and TaxIdB"
     */
    @Test
    public void findByAsSpeciesWithTaxIdAAndTaxIdB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                SPECIES + ":(taxid:10116 AND taxid:12345)",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c2", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "pubauth miql query with first name"
     */
    @Test
    public void findByAsPubAuthorWithFirstName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                PUB_AUTHORS + ":Name3",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(1, interactionFacetPage5.getContent().size());
        assertEquals(1, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(1, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c2", iteractor.next().getAc());
    }

    /**
     * Behaviour If the User executes "pubauth miql query with first name and initials within quotes"
     */
    @Test
    public void findByAsPubAuthorWithFirstNameAndInitialsWithinQuotes() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                PUB_AUTHORS + ":\"Name2 I.nitials2\"",
                false,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                false,
                0,
                1,
                false,
                null,
                null,
                0,
                10);

        // page checks
        assertFalse(interactionFacetPage5.getContent().isEmpty());
        assertEquals(2, interactionFacetPage5.getContent().size());
        assertEquals(2, interactionFacetPage5.getNumberOfElements());
        assertEquals(0, interactionFacetPage5.getPageable().getPageNumber());
        assertEquals(10, interactionFacetPage5.getPageable().getPageSize());
        assertEquals(2, interactionFacetPage5.getTotalElements());

        Iterator<SearchInteraction> iteractor = interactionFacetPage5.iterator();
        assertEquals("interaction_c1", iteractor.next().getAc());
        assertEquals("interaction_c2", iteractor.next().getAc());
    }
}