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
import uk.ac.ebi.intact.search.interactions.utils.as.converters.DateFieldConverter;
import uk.ac.ebi.intact.search.interactions.utils.as.converters.TextFieldConverter;
import uk.ac.ebi.intact.search.interactions.utils.as.converters.XrefFieldConverter;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
        searchInteraction1.setAsInteractionIds(merge(XrefFieldConverter.indexFieldValues("intact", "EBI-123456"),
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
        searchInteraction1.setAsBioRoleA(TextFieldConverter.indexFieldValues("psi-mi", "MI:412345", "BioroleA1 shortlabel"));
        searchInteraction1.setAsBioRoleB(TextFieldConverter.indexFieldValues("psi-mi", "MI:4123345", "BioroleB1 shortlabel"));
        searchInteraction1.setAsTypeA(TextFieldConverter.indexFieldValues("psi-mi", "MI:512345", "typeA1 shortlabel"));
        searchInteraction1.setAsTypeB(TextFieldConverter.indexFieldValues("psi-mi", "MI:5123345", "typeB1 shortlabel"));
        searchInteraction1.setAsFeatureTypesA(TextFieldConverter.indexFieldValues("psi-mi", "MI:61234", "feature type A1 shortlabel"));
        searchInteraction1.setAsFeatureTypesB(TextFieldConverter.indexFieldValues("psi-mi", "MI:612334", "feature type B1 shortlabel"));
        searchInteraction1.setAsIdentificationMethodsA(TextFieldConverter.indexFieldValues("psi-mi", "MI:71234", "participant identification method A1 shortlabel"));
        searchInteraction1.setAsIdentificationMethodsB(TextFieldConverter.indexFieldValues("psi-mi", "MI:712334", "participant identification method B1 shortlabel"));
        searchInteraction1.setAsXrefsA(merge(XrefFieldConverter.indexFieldValues("intact", "EBI-9123456"),
                XrefFieldConverter.indexFieldValues("go", "GO:213456")));
        searchInteraction1.setAsXrefsB(merge(XrefFieldConverter.indexFieldValues("intact", "EBI-9223456"),
                XrefFieldConverter.indexFieldValues("go", "GO:223456")));
        searchInteraction1.setAsInteractionXrefs(XrefFieldConverter.indexFieldValues("go", "GO:412345"));
        searchInteraction1.setAsSource(TextFieldConverter.indexFieldValues("psi-mi", "MI:0469", "European Bioinformatics Institute"));
        searchInteraction1.setAsExpansionMethod(TextFieldConverter.indexFieldValues("psi-mi", "MI:1060", "spoke expansion"));
        try {
            searchInteraction1.setAsUpdationDate(DateFieldConverter.indexFieldValues(new SimpleDateFormat("yyyy/MM/dd").parse("2012/01/01")));
            searchInteraction1.setAsReleaseDate(DateFieldConverter.indexFieldValues(new SimpleDateFormat("yyyy/MM/dd").parse("2013/01/01")));
        } catch (Exception e) {
        }
        searchInteraction1.setAsIntactMiscore(0.5);

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
        searchInteraction2.setAsInteractionIds(merge(XrefFieldConverter.indexFieldValues("intact", "EBI-1234567"),
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
        searchInteraction2.setAsBioRoleA(TextFieldConverter.indexFieldValues("psi-mi", "MI:422345", "BioroleA2 shortlabel"));
        searchInteraction2.setAsBioRoleB(TextFieldConverter.indexFieldValues("psi-mi", "MI:4223345", "BioroleB2 shortlabel"));
        searchInteraction2.setAsTypeA(TextFieldConverter.indexFieldValues("psi-mi", "MI:522345", "typeA2 shortlabel"));
        searchInteraction2.setAsTypeB(TextFieldConverter.indexFieldValues("psi-mi", "MI:5223345", "typeB2 shortlabel"));
        searchInteraction2.setAsFeatureTypesA(TextFieldConverter.indexFieldValues("psi-mi", "MI:62234", "feature type A2 shortlabel"));
        searchInteraction2.setAsFeatureTypesB(TextFieldConverter.indexFieldValues("psi-mi", "MI:622334", "feature type B2 shortlabel"));
        searchInteraction2.setAsIdentificationMethodsA(TextFieldConverter.indexFieldValues("psi-mi", "MI:72234", "participant identification method A2 shortlabel"));
        searchInteraction2.setAsIdentificationMethodsB(TextFieldConverter.indexFieldValues("psi-mi", "MI:722334", "participant identification method B2 shortlabel"));
        searchInteraction2.setAsXrefsA(merge(XrefFieldConverter.indexFieldValues("intact", "EBI-8123456"),
                XrefFieldConverter.indexFieldValues("go", "GO:313456")));
        searchInteraction2.setAsXrefsB(merge(XrefFieldConverter.indexFieldValues("intact", "EBI-8223456"),
                XrefFieldConverter.indexFieldValues("go", "GO:323456")));
        searchInteraction2.setAsInteractionXrefs(XrefFieldConverter.indexFieldValues("go", "GO:512345"));
        searchInteraction2.setAsSource(TextFieldConverter.indexFieldValues("psi-mi", "MI:0471", "Mint"));
        searchInteraction2.setAsExpansionMethod(TextFieldConverter.indexFieldValues("psi-mi", "MI:1061", "matrix expansion"));
        try {
            searchInteraction2.setAsUpdationDate(DateFieldConverter.indexFieldValues(new SimpleDateFormat("yyyy/MM/dd").parse("2015/01/01")));
            searchInteraction2.setAsReleaseDate(DateFieldConverter.indexFieldValues(new SimpleDateFormat("yyyy/MM/dd").parse("2016/01/01")));
        } catch (Exception e) {
        }
        searchInteraction2.setAsIntactMiscore(1.0);

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
                INTERACTION_IDS + ":intact:EBI-123456",
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
                INTERACTION_IDS + ":imex",
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
                INTERACTION_IDS + ":IM-12345-1",
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
     * Behaviour If the User executes "pubauthors miql query with first name"
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
     * Behaviour If the User executes "pubauthors miql query with first name and initials within quotes"
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

    /**
     * Behaviour If the User executes "pubauth miql query with first name"
     */
    @Test
    public void findByAsPubFirstAuthorWithFirstName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                PUB_FIRST_AUTHOR + ":FirstAuthor1",
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
     * Behaviour If the User executes "pubauth miql query with first name and initials within quotes"
     */
    @Test
    public void findByAsPubFirstAuthorWithFirstNameAndInitialsWithinQuotes() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                PUB_FIRST_AUTHOR + ":\"FirstAuthor2 I.nitials2\"",
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
     * Behaviour If the User executes "pubyear miql query with a particular year"
     */
    @Test
    public void findByAsPubYear() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                PUB_YEAR + ":2012",
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
     * Behaviour If the User executes "pubyear miql query within year range"
     */
    @Test
    public void findByAsPubYearRange() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                PUB_YEAR + ":[2012 TO 2015]",
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
     * Behaviour If the User executes "type miql query with db:id"
     */
    @Test
    public void findByAsTypeWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE + ":psi-mi:MI:1234",
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
     * Behaviour If the User executes "type miql query with db"
     */
    @Test
    public void findByAsTypeWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE + ":psi-mi",
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
     * Behaviour If the User executes "type miql query with id"
     */
    @Test
    public void findByAsTypeWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE + ":MI:1234",
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
     * Behaviour If the User executes "type miql query with text(short name)"
     */
    @Test
    public void findByAsTypeWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE + ":\"Type1 shortlabel\"",
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
     * Behaviour If the User executes "taxidHost miql query with db:id"
     */
    @Test
    public void findByAsHostOrganismWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                HOST_ORGANISM + ":taxid:2345678",
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
     * Behaviour If the User executes "taxidHost miql query with db"
     */
    @Test
    public void findByAsHostOrganismWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                HOST_ORGANISM + ":taxid",
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
     * Behaviour If the User executes "taxidHost miql query with id"
     */
    @Test
    public void findByAsHostOrganismWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                HOST_ORGANISM + ":2345678",
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
     * Behaviour If the User executes "taxidHost miql query with text(short name)"
     */
    @Test
    public void findByAsHostOrganismWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                HOST_ORGANISM + ":\"organism4 short name\"",
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
     * Behaviour If the User executes "taxidHost miql query with text(full name)"
     */
    @Test
    public void findByAsHostOrganismWithOnlyFullName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                HOST_ORGANISM + ":\"organism4 full name\"",
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
     * Behaviour If the User executes "detmethod miql query with db:id"
     */
    @Test
    public void findByAsDetectionMethodWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTION_DETECTION_METHOD + ":psi-mi:MI:21234",
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
     * Behaviour If the User executes "detmethod miql query with db"
     */
    @Test
    public void findByAsDetectionMethodWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTION_DETECTION_METHOD + ":psi-mi",
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
     * Behaviour If the User executes "detmethod miql query with id"
     */
    @Test
    public void findByAsDetectionMethodWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTION_DETECTION_METHOD + ":MI:21234",
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
     * Behaviour If the User executes "detmethod miql query with text(short name)"
     */
    @Test
    public void findByAsDetectionMethodWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTION_DETECTION_METHOD + ":\"DetectionMethod1 shortlabel\"",
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
     * Behaviour If the User executes "pbioroleA miql query with db:id"
     */
    @Test
    public void findByAsBioRoleAWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                BIOROLE_A + ":psi-mi:MI:412345",
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
     * Behaviour If the User executes "pbioroleA miql query with db"
     */
    @Test
    public void findByAsBioRoleAWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                BIOROLE_A + ":psi-mi",
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
     * Behaviour If the User executes "pbioroleA miql query with id"
     */
    @Test
    public void findByAsBioRoleAWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                BIOROLE_A + ":MI:412345",
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
     * Behaviour If the User executes "pbioroleA miql query with text(short name)"
     */
    @Test
    public void findByAsBioRoleAWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                BIOROLE_A + ":\"BioroleA1 shortlabel\"",
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
     * Behaviour If the User executes "pbioroleB miql query with db:id"
     */
    @Test
    public void findByAsBioRoleBWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                BIOROLE_B + ":psi-mi:MI:4123345",
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
     * Behaviour If the User executes "pbioroleB miql query with db"
     */
    @Test
    public void findByAsBioRoleBWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                BIOROLE_B + ":psi-mi",
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
     * Behaviour If the User executes "pbioroleB miql query with id"
     */
    @Test
    public void findByAsBioRoleBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                BIOROLE_B + ":MI:4123345",
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
     * Behaviour If the User executes "pbioroleB miql query with text(short name)"
     */
    @Test
    public void findByAsBioRoleBWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                BIOROLE_B + ":\"BioroleB1 shortlabel\"",
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
     * Behaviour If the User executes "pbioroleA and pbioroleB miql query with id"
     */
    @Test
    public void findByAsBioRoleAAndBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                BIOROLE + ":(MI:4123345 AND MI:412345)",
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
     * Behaviour If the User executes "typeA miql query with db:id"
     */
    @Test
    public void findByAsTypeAWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE_A + ":psi-mi:MI:512345",
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
     * Behaviour If the User executes "typeA miql query with db"
     */
    @Test
    public void findByAsTypeAWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE_A + ":psi-mi",
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
     * Behaviour If the User executes "typeA miql query with id"
     */
    @Test
    public void findByAsTypeAWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE_A + ":MI:512345",
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
     * Behaviour If the User executes "typeA miql query with text(short name)"
     */
    @Test
    public void findByAsTypeAWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE_A + ":\"typeA1 shortlabel\"",
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
     * Behaviour If the User executes "typeB miql query with db:id"
     */
    @Test
    public void findByAsTypeBWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE_B + ":psi-mi:MI:5123345",
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
     * Behaviour If the User executes "typeB miql query with db"
     */
    @Test
    public void findByAsTypeBWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE_B + ":psi-mi",
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
     * Behaviour If the User executes "typeB miql query with id"
     */
    @Test
    public void findByAsTypeBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE_B + ":MI:5123345",
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
     * Behaviour If the User executes "typeB miql query with text(short name)"
     */
    @Test
    public void findByAsTypeBWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                TYPE_B + ":\"typeB1 shortlabel\"",
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
     * Behaviour If the User executes "typeA and typeB miql query with id"
     */
    @Test
    public void findByAsTypeAAndBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTOR_TYPE + ":(MI:512345 AND MI:5123345)",
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
     * Behaviour If the User executes "ftypeA miql query with db:id"
     */
    @Test
    public void findByAsFeatureTypeAWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                FEATURE_TYPE_A + ":psi-mi:MI:61234",
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
     * Behaviour If the User executes "ftypeA miql query with db"
     */
    @Test
    public void findByAsFeatureTypeAWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                FEATURE_TYPE_A + ":psi-mi",
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
     * Behaviour If the User executes "ftypeA miql query with id"
     */
    @Test
    public void findByAsFeatureTypeAWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                FEATURE_TYPE_A + ":MI:61234",
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
     * Behaviour If the User executes "ftypeA miql query with text(short name)"
     */
    @Test
    public void findByAsFeatureTypeAWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                FEATURE_TYPE_A + ":\"feature type A1 shortlabel\"",
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
     * Behaviour If the User executes "ftypeB miql query with db:id"
     */
    @Test
    public void findByAsFeatureTypeBWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                FEATURE_TYPE_B + ":psi-mi:MI:612334",
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
     * Behaviour If the User executes "ftypeB miql query with db"
     */
    @Test
    public void findByAsFeatureTypeBWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                FEATURE_TYPE_B + ":psi-mi",
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
     * Behaviour If the User executes "ftypeB miql query with id"
     */
    @Test
    public void findByAsFeatureTypeBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                FEATURE_TYPE_B + ":MI:612334",
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
     * Behaviour If the User executes "ftypeB miql query with text(short name)"
     */
    @Test
    public void findByAsFeatureTypeBWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                FEATURE_TYPE_B + ":\"feature type B1 shortlabel\"",
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
     * Behaviour If the User executes "ftypeA and ftypeB miql query with id"
     */
    @Test
    public void findByAsFeatureTypeAAndBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                FEATURE_TYPE + ":(MI:61234 AND MI:612334)",
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
     * Behaviour If the User executes "pmethodA miql query with db:id"
     */
    @Test
    public void findByAsParticipantIdentificationAWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFICATION_METHOD_A + ":psi-mi:MI:71234",
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
     * Behaviour If the User executes "pmethodA miql query with db"
     */
    @Test
    public void findByAsParticipantIdentificationAWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFICATION_METHOD_A + ":psi-mi",
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
     * Behaviour If the User executes "pmethodA miql query with id"
     */
    @Test
    public void findByAsParticipantIdentificationAWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFICATION_METHOD_A + ":MI:71234",
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
     * Behaviour If the User executes "pmethodA miql query with text(short name)"
     */
    @Test
    public void findByAsParticipantIdentificationAWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFICATION_METHOD_A + ":\"participant identification method A1 shortlabel\"",
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
     * Behaviour If the User executes "pmethodB miql query with db:id"
     */
    @Test
    public void findByAsParticipantIdentificationBWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFICATION_METHOD_B + ":psi-mi:MI:712334",
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
     * Behaviour If the User executes "pmethodB miql query with db"
     */
    @Test
    public void findByAsParticipantIdentificationBWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFICATION_METHOD_B + ":psi-mi",
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
     * Behaviour If the User executes "pmethodB miql query with id"
     */
    @Test
    public void findByAsParticipantIdentificationBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFICATION_METHOD_B + ":MI:712334",
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
     * Behaviour If the User executes "pmethodB miql query with text(short name)"
     */
    @Test
    public void findByAsParticipantIdentificationBWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFICATION_METHOD_B + ":\"participant identification method B1 shortlabel\"",
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
     * Behaviour If the User executes "pmethodA and pmethodB miql query with id"
     */
    @Test
    public void findByAsParticipantIdentificationAAndBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                IDENTIFICATION_METHOD + ":(MI:71234 AND MI:712334)",
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
     * Behaviour If the User executes "pxrefA miql query with db:id"
     */
    @Test
    public void findByAsXrefAWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                XREFS_A + ":intact:EBI-9123456",
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
     * Behaviour If the User executes "pxrefA miql query with db"
     */
    @Test
    public void findByAsXrefAWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                XREFS_A + ":intact",
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
     * Behaviour If the User executes "pxrefA miql query with id"
     */
    @Test
    public void findByAsXrefAWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                XREFS_A + ":EBI-9123456",
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
     * Behaviour If the User executes "pxrefB miql query with db:id"
     */
    @Test
    public void findByAsXrefBWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                XREFS_B + ":go:GO:223456",
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
     * Behaviour If the User executes "pxrefB miql query with db"
     */
    @Test
    public void findByAsXrefBWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                XREFS_B + ":go",
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
     * Behaviour If the User executes "pxrefB miql query with id"
     */
    @Test
    public void findByAsXrefBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                XREFS_B + ":GO:223456",
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
     * Behaviour If the User executes "pxrefA and pxrefB miql query with id"
     */
    @Test
    public void findByAsXrefAAndBWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTOR_XREFS + ":(GO:223456 AND EBI-9123456)",
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
     * Behaviour If the User executes "xref miql query with db:id"
     */
    @Test
    public void findByAsInteractionXrefWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTION_XREFS + ":go:GO:412345",
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
     * Behaviour If the User executes "xref miql query with db"
     */
    @Test
    public void findByAsInteractionXrefWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTION_XREFS + ":go",
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
     * Behaviour If the User executes "xref miql query with id"
     */
    @Test
    public void findByAsInteractionXrefWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTERACTION_XREFS + ":GO:412345",
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
     * Behaviour If the User executes "source miql query with db:id"
     */
    @Test
    public void findByAsSourceWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                SOURCE + ":psi-mi:MI:0469",
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
     * Behaviour If the User executes "source miql query with db"
     */
    @Test
    public void findByAsSourceWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                SOURCE + ":psi-mi",
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
     * Behaviour If the User executes "source miql query with id"
     */
    @Test
    public void findByAsSourceWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                SOURCE + ":MI:0469",
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
     * Behaviour If the User executes "source miql query with text(short name)"
     */
    @Test
    public void findByAsSourceWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                SOURCE + ":\"European Bioinformatics Institute\"",
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
     * Behaviour If the User executes "complex miql query with db:id"
     */
    @Test
    public void findByAsExpansionMethodWithDBAndId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                EXPANSION_METHOD + ":psi-mi:MI:1060",
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
     * Behaviour If the User executes "complex miql query with db"
     */
    @Test
    public void findByAsExpansionMethodWithOnlyDB() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                EXPANSION_METHOD + ":psi-mi",
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
     * Behaviour If the User executes "complex miql query with id"
     */
    @Test
    public void findByAsExpansionMethodWithOnlyId() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                EXPANSION_METHOD + ":MI:1060",
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
     * Behaviour If the User executes "complex miql query with text(short name)"
     */
    @Test
    public void findByAsExpansionMethodWithOnlyShortName() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                EXPANSION_METHOD + ":\"spoke expansion\"",
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
     * Behaviour If the User executes "udate miql query with a particular date"
     */
    @Test
    public void findByAsLastUpdateDate() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                LAST_UPDATED + ":20120101",
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
     * Behaviour If the User executes "udate miql query within year range"
     */
    @Test
    public void findByAsLastUpdateDateRange() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                LAST_UPDATED + ":[20120101 TO 20150101]",
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
     * Behaviour If the User executes "rdate miql query with a particular date"
     */
    @Test
    public void findByAsReleaseDate() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                RELEASE_DATE + ":20130101",
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
     * Behaviour If the User executes "rdate miql query within year range"
     */
    @Test
    public void findByAsReleaseDateRange() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                RELEASE_DATE + ":[20130101 TO 20160101]",
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
     * Behaviour If the User executes "intact-miscore miql query with a particular score"
     */
    @Test
    public void findByAsIntactMiScore() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTACT_MI_SCORE + ":0.5",
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
     * Behaviour If the User executes "intact-miscore miql query within year range"
     */
    @Test
    public void findByAsIntactMiScoreRange() {
        FacetPage<SearchInteraction> interactionFacetPage5 = interactionSearchService.findInteractionWithFacet(
                INTACT_MI_SCORE + ":[0.5 TO 1.0]",
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