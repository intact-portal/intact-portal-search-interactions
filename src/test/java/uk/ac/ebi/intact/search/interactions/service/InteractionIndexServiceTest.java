package uk.ac.ebi.intact.search.interactions.service;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.search.interactions.model.Interaction;
import uk.ac.ebi.intact.search.interactions.model.InteractionResult;
import uk.ac.ebi.intact.search.interactions.service.util.RequiresSolrServer;
import uk.ac.ebi.intact.search.interactions.service.util.TestUtil;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * @author Elisabet Barrera
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InteractionIndexServiceTest {

    private Interaction interaction1;
    private Interaction interaction2;

    private Collection<Interaction> interactions;

    @Resource
    private InteractionIndexService interactionIndexService;

    @Resource
    private InteractionSearchService interactionSearchService;

    public static
    @ClassRule
    RequiresSolrServer requiresRunningServer = RequiresSolrServer.onLocalhost();

    @Before
    public void setUp() throws Exception {

        //Delete all documents from solr core
        interactionIndexService.deleteAll();
        /*Interactions are instantiated from saved interactions in an xml as instantiating it one by one in the code is cumbersome
        * For ref. The Interactions.xml can be created with a method saveInteractioninDisc in CommonUtility in intact-portal-indexer*/
        interactions = TestUtil.getInteractionObjFromXml("./src/test/resources/Interactions.xml");
        interactionIndexService.save(interactions);
        assertEquals(interactionSearchService.countDocuments(), 10);
    }

    @Test
    /**
     * Behaviour If the User types "Author name" in search box
     */
    public void findByAuthor() {
        Page<Interaction> interactionOp = interactionSearchService.findInteractions("Shorter J.");
        assertEquals(interactionOp.getNumberOfElements(), 4);
    }

    @Test
    /**
     * Behaviour If the User types "Species Name" in search box
     */
    public void findBySpecies() {
        Page<Interaction> interactionOp = interactionSearchService.findInteractions("Rattus norvegicus (Rat)");
        assertEquals(interactionOp.getNumberOfElements(), 4);
    }

    @Test
    /**
     * Behaviour If the User types "Interaction Type" in search box
     */
    public void findByInteractionType() {
        Page<Interaction> interactionOp = interactionSearchService.findInteractions("physical association");
        assertEquals(interactionOp.getNumberOfElements(), 10);
    }

    @Test
    /**
     * Behaviour If the User types "Interaction Detection Method" in search box
     */
    public void findByIntDetMethod() {
        Page<Interaction> interactionOp = interactionSearchService.findInteractions("molecular sieving");
        assertEquals(interactionOp.getNumberOfElements(), 1);
    }

    @Test
    /**
     * Behaviour If the User types "Host Organism" in search box
     */
    public void findByHostOrganism() {
        Page<Interaction> interactionOp = interactionSearchService.findInteractions("In vitro");
        assertEquals(interactionOp.getNumberOfElements(), 6);
    }

    @Test
    /**
     * Expected Facet Results If the User types "physical association" and Hit Search Button
     */
    public void facetTest() {
        InteractionResult interactionOp = interactionSearchService.findInteractionWithFacet("physical association", null, null, null, false, 0, 1, null, false, 0, 10);
        Set<String> facetFields=interactionOp.getFacetResultPage().keySet();
        assertEquals(true,facetFields.contains("interaction_negative"));
        assertEquals(true,facetFields.contains("intact_miscore"));
        assertEquals(true,facetFields.contains("interaction_detection_method_str"));
        assertEquals(true,facetFields.contains("interaction_type_str"));
        assertEquals(true,facetFields.contains("host_organism_str"));
        assertEquals(true,facetFields.contains("speciesA_B_str"));

        for (String facetField : interactionOp.getFacetResultPage().keySet()) {
             if (facetField.equals("interaction_negative")) {
                List<InteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (InteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("false")) {
                        assertEquals(10, facetCount.getValueCount().longValue());
                    }
                }
            }

            if (facetField.equals("intact_miscore")) {
                List<InteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (InteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("0.39")) {
                        assertEquals(3, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("0.52")) {
                        assertEquals(1, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("0.55")) {
                        assertEquals(3, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("0.58")) {
                        assertEquals(3, facetCount.getValueCount().longValue());
                    }
                }
            }

            if (facetField.equals("interaction_detection_method_str")) {
                List<InteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (InteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("anti bait coip")) {
                        assertEquals(4, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("density sedimentatio")) {
                        assertEquals(3, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("affinity chrom")) {
                        assertEquals(1, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("elisa")) {
                        assertEquals(1, facetCount.getValueCount().longValue());
                    }
                }
            }

            if (facetField.equals("interaction_type_str")) {
                List<InteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (InteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("physical association")) {
                        assertEquals(10, facetCount.getValueCount().longValue());
                    }
                }
            }

            if (facetField.equals("host_organism_str")) {
                List<InteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (InteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("In vitro")) {
                        assertEquals(6, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("rattus norvegicus liver")) {
                        assertEquals(4, facetCount.getValueCount().longValue());
                    }
                }
            }

            if (facetField.equals("speciesA_B_str")) {
                List<InteractionResult.FacetCount> facetCounts = interactionOp.getFacetResultPage().get(facetField);
                for (InteractionResult.FacetCount facetCount : facetCounts) {
                    if (facetCount.getValue().equals("Homo sapiens")) {
                        assertEquals(6, facetCount.getValueCount().longValue());
                    }
                    if (facetCount.getValue().equals("Rattus norvegicus (Rat)")) {
                        assertEquals(4, facetCount.getValueCount().longValue());
                    }
                }
            }
        }

        assertEquals(10,interactionOp.getNumberOfElements());
    }

    @Test
    /**
     * Expected behaviour when filter elements are passed
     */
     public void filterTest1() {

        //filter1

        Set<String> detectionMethod=new HashSet<String>();
        detectionMethod.add("molecular sieving");

        Set<String> interactiontype=new HashSet<String>();
        interactiontype.add("physical association");

        Set<String> hostOrganism=new HashSet<String>();
        hostOrganism.add("In vitro");

        boolean isNegative=false;

        double minMiscore=0;

        double maxMiscore=0.6;

        Set<String> species=new HashSet<String>();
        species.add("Homo sapiens");

        boolean interSpecies=false;

        int page=0;

        int size=10;

        InteractionResult interactionOp = interactionSearchService.
                findInteractionWithFacet("physical association", detectionMethod, interactiontype, hostOrganism, isNegative, minMiscore, maxMiscore,species, false, page, size);
        assertEquals(1, interactionOp.getNumberOfElements());

    }

    @Test
    /**
     * Expected behaviour when filter elements are passed
     */
    public void filterTest2() {

        //filter2

        Set<String> detectionMethod=new HashSet<String>();
        detectionMethod.add("molecular sieving");

        Set<String> interactiontype=new HashSet<String>();
        interactiontype.add("physical association");

        Set<String> hostOrganism=new HashSet<String>();
        hostOrganism.add("In vitro");

        boolean isNegative=false;

        double minMiscore=0;

        double maxMiscore=0.6;

        Set<String> species=new HashSet<String>();
        species.add("Homo sapiens");
        species.add("Rattus norvegicus (Rat)");

        boolean interSpecies=true;

        int page=0;

        int size=10;

        InteractionResult interactionOp = interactionSearchService.
                findInteractionWithFacet("physical association", detectionMethod, interactiontype, hostOrganism, isNegative, minMiscore, maxMiscore,species, interSpecies, page, size);
        assertEquals(0, interactionOp.getNumberOfElements());

    }

    @Test
    public void deleteCollection() {
        // empty collection
        interactionIndexService.deleteAll();
        assertEquals(interactionSearchService.countDocuments(), 0);

    }

}