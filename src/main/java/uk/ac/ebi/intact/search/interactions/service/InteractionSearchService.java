package uk.ac.ebi.intact.search.interactions.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.controller.result.SearchGraphJson;
import uk.ac.ebi.intact.search.interactions.controller.result.SearchGraphLink;
import uk.ac.ebi.intact.search.interactions.controller.result.SearchGraphNode;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.repository.InteractionRepository;
import uk.ac.ebi.intact.search.interactions.utils.GraphUtility;

import java.util.*;

/**
 * @author Elisabet Barrera
 */
@Service
public class InteractionSearchService {

    private static final Log log = LogFactory.getLog(InteractionSearchService.class);

    private final InteractionRepository interactionRepository;

    public InteractionSearchService(@Qualifier("interactionRepository") InteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    public Iterable<SearchInteraction> findAll() {
        return this.interactionRepository.findAll();
    }

    public Optional<SearchInteraction> findByInteractionAc(String ac) {
        return this.interactionRepository.findByInteractionAc(ac);
    }

    public Page<SearchInteraction> findInteractions(String query) {
        return interactionRepository.findInteractions(query, PageRequest.of(0, 10));
    }

    public FacetPage<SearchInteraction> findInteractionWithFacet(String query,
                                                                 Set<String> speciesFilter,
                                                                 Set<String> interactorTypeFilter,
                                                                 Set<String> detectionMethodFilter,
                                                                 Set<String> interactionTypeFilter,
                                                                 Set<String> hostOrganismFilter,
                                                                 boolean isNegativeFilter,
                                                                 double minMiScore,
                                                                 double maxMiScore,
                                                                 boolean interSpecies,
                                                                 int page,
                                                                 int pageSize) {
        return interactionRepository.findInteractionWithFacet(query, speciesFilter, interactorTypeFilter, detectionMethodFilter,
                interactionTypeFilter, hostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies, null,
                PageRequest.of(page, pageSize));
    }

    public Page<SearchInteraction> findInteractionForGraphJson(String query,
                                                               Set<String> speciesFilter,
                                                               Set<String> interactorTypeFilter,
                                                               Set<String> detectionMethodFilter,
                                                               Set<String> interactionTypeFilter,
                                                               Set<String> hostOrganismFilter,
                                                               boolean isNegativeFilter,
                                                               double minMiScore,
                                                               double maxMiScore,
                                                               boolean interSpecies,
                                                               int page,
                                                               int pageSize) {
        return interactionRepository.findInteractionForGraphJson(query, speciesFilter, interactorTypeFilter, detectionMethodFilter,
                interactionTypeFilter, hostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies, null,
                PageRequest.of(page, pageSize));
    }

    public long countInteractionResult(String query,
                                       String interactorAc,
                                       Set<String> speciesFilter,
                                       Set<String> interactorTypeFilter,
                                       Set<String> detectionMethodFilter,
                                       Set<String> interactionTypeFilter,
                                       Set<String> hostOrganismFilter,
                                       boolean isNegativeFilter,
                                       double minMiScore,
                                       double maxMiScore,
                                       boolean interSpecies) {
        return interactionRepository.countInteractionResult(query, interactorAc, speciesFilter, interactorTypeFilter,
                detectionMethodFilter, interactionTypeFilter, hostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies);
    }

    public SearchGraphJson getGraphJson(String query,
                                        Set<String> speciesFilter,
                                        Set<String> interactorTypeFilter,
                                        Set<String> detectionMethodFilter,
                                        Set<String> interactionTypeFilter,
                                        Set<String> interactionHostOrganism,
                                        boolean isNegativeFilter,
                                        double minMiScore,
                                        double maxMiScore,
                                        boolean interSpecies,
                                        int page,
                                        int pageSize) {

        /*Page<SearchInteractor> interactors = this.interactorSearchService.findInteractorForGraphJson(query, speciesFilter, interactorTypeFilter,
                detectionMethodFilter, interactionTypeFilter, interactionHostOrganism,
                isNegativeFilter, minMiScore, maxMiScore, page, pageSize);*/

        Page<SearchInteraction> interactions = findInteractionForGraphJson(query, speciesFilter, interactorTypeFilter, detectionMethodFilter,
                interactionTypeFilter, interactionHostOrganism, isNegativeFilter, minMiScore, maxMiScore, interSpecies,
                page, pageSize);

        // return toD3FormatAlternative(interactors.getContent(), interactions.getContent());

        return toD3FormatAlternative(interactions.getContent());
    }

    /*
     * Delete this code when not needed
     *
     * */
    private SearchGraphJson toD3FormatAlternative(List<SearchInteraction> interactions) {
        SearchGraphJson searchGraphJson = new SearchGraphJson();
        List<SearchGraphNode> searchGraphNodes = new ArrayList<>();
        List<SearchGraphLink> searchGraphLinks = new ArrayList<>();
        HashSet<String> interactorSet = new HashSet<>();


        for (SearchInteraction searchInteraction : interactions) {
            try {
                SearchGraphLink searchGraphLink = new SearchGraphLink();
                searchGraphLink.setSource(searchInteraction.getInteractorAAc());
                searchGraphLink.setTarget(searchInteraction.getInteractorBAc());
                searchGraphLink.setInteractionAc(searchInteraction.getInteractionAc());
                searchGraphLink.setInteractionType(searchInteraction.getInteractionType());
                searchGraphLink.setInteractionDetectionMethod(searchInteraction.getInteractionDetectionMethod());
                searchGraphLink.setColor(GraphUtility.getColorForInteractionType(searchInteraction.getInteractionType()));

                if (!interactorSet.contains(searchInteraction.getInteractorAAc())) {
                    SearchGraphNode searchGraphNode = new SearchGraphNode();
                    searchGraphNode.setId(searchInteraction.getInteractorAAc());
                    searchGraphNode.setSpeciesName(searchInteraction.getSpeciesA());
                    searchGraphNode.setTaxId(searchInteraction.getTaxIdA());
                    searchGraphNode.setInteractorId(searchInteraction.getMoleculeA());
                    searchGraphNode.setInteractorType(searchInteraction.getTypeA());
                    searchGraphNode.setPreferredId(searchInteraction.getUniqueIdA());
                    searchGraphNode.setInteractorName(searchInteraction.getMoleculeA());
                    searchGraphNode.setColor(GraphUtility.getColorForTaxId(searchInteraction.getTaxIdA()));
                    searchGraphNodes.add(searchGraphNode);
                    interactorSet.add(searchInteraction.getInteractorAAc());
                }

                if (!interactorSet.contains(searchInteraction.getInteractorBAc())) {
                    SearchGraphNode searchGraphNode = new SearchGraphNode();
                    searchGraphNode.setId(searchInteraction.getInteractorBAc());
                    searchGraphNode.setSpeciesName(searchInteraction.getSpeciesB());
                    searchGraphNode.setTaxId(searchInteraction.getTaxIdB());
                    searchGraphNode.setInteractorId(searchInteraction.getMoleculeB());
                    searchGraphNode.setInteractorType(searchInteraction.getTypeB());
                    searchGraphNode.setPreferredId(searchInteraction.getUniqueIdB());
                    searchGraphNode.setInteractorName(searchInteraction.getMoleculeB());
                    searchGraphNode.setColor(GraphUtility.getColorForTaxId(searchInteraction.getTaxIdB()));
                    searchGraphNodes.add(searchGraphNode);
                    interactorSet.add(searchInteraction.getInteractorBAc());
                }

                searchGraphLinks.add(searchGraphLink);
            } catch (Exception e) {
                log.info("Interaction with id: " + searchInteraction.getInteractionAc() + "failed to process" +
                        "and therefore this interaction will not be in graph json");
                //TODO... Uncomment following
                //throw e;
            }
        }


        searchGraphJson.setInteractions(searchGraphLinks);
        searchGraphJson.setInteractors(searchGraphNodes);

        return searchGraphJson;
    }

    public long countTotal() {
        return this.interactionRepository.count();
    }
}
