package uk.ac.ebi.intact.search.interactions.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.repository.InteractionRepository;
import uk.ac.ebi.intact.search.interactions.utils.DocumentType;
import uk.ac.ebi.intact.search.interactions.utils.SearchInteractionUtility;

import java.util.Optional;
import java.util.Set;

/**
 * @author Elisabet Barrera
 */
@Service
public class InteractionSearchService {

    private static final Log log = LogFactory.getLog(InteractionSearchService.class);

    private final InteractionRepository interactionRepository;

    public InteractionSearchService(InteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    public Iterable<SearchInteraction> findAll() {
        return this.interactionRepository.findAll();
    }

    public Optional<SearchInteraction> findByInteractionAc(String ac) {
        return this.interactionRepository.findByAc(ac);
    }

    public Page<SearchInteraction> findInteractions(String query) {
        query = SearchInteractionUtility.escapeQueryChars(query);
        return interactionRepository.findInteractions(query, PageRequest.of(0, 10));
    }

    public FacetPage<SearchInteraction> findInteractionWithFacet(String query,
                                                                 boolean batchSearch,
                                                                 Set<String> interactorSpeciesFilter,
                                                                 Set<String> interactorTypeFilter,
                                                                 Set<String> interactionDetectionMethodFilter,
                                                                 Set<String> interactionTypeFilter,
                                                                 Set<String> interactionHostOrganismFilter,
                                                                 boolean isNegativeFilter,
                                                                 double minMiScore,
                                                                 double maxMiScore,
                                                                 boolean interSpecies,
                                                                 Set<Long> binaryInteractionIdFilter,
                                                                 Set<String> interactorAcFilter,
                                                                 int page,
                                                                 int pageSize) {
        return interactionRepository.findInteractionWithFacet(query, batchSearch, interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies, binaryInteractionIdFilter, interactorAcFilter, null,
                PageRequest.of(page, pageSize));
    }

    public Page<SearchInteraction> findInteractionForGraphJson(String query,
                                                               boolean batchSearch,
                                                               Set<String> interactorSpeciesFilter,
                                                               Set<String> interactorTypeFilter,
                                                               Set<String> interactionDetectionMethodFilter,
                                                               Set<String> interactionTypeFilter,
                                                               Set<String> interactionHostOrganismFilter,
                                                               boolean isNegativeFilter,
                                                               double minMiScore,
                                                               double maxMiScore,
                                                               boolean interSpecies,
                                                               int page,
                                                               int pageSize) {
        return interactionRepository.findInteractionForGraphJson(query, batchSearch, interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies, null,
                PageRequest.of(page, pageSize));
    }

    public long countInteractionsForGraphJson(String query,
                                              boolean batchSearch,
                                              Set<String> interactorSpeciesFilter,
                                              Set<String> interactorTypeFilter,
                                              Set<String> interactionDetectionMethodFilter,
                                              Set<String> interactionTypeFilter,
                                              Set<String> interactionHostOrganismFilter,
                                              boolean isNegativeFilter,
                                              double minMiScore,
                                              double maxMiScore,
                                              boolean interSpecies) {
        return interactionRepository.countInteractionsForGraphJson(query, batchSearch, interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies);
    }

    public long countInteractionResult(String query,
                                       boolean batchSearch,
                                       String interactorAc,
                                       Set<String> interactorSpeciesFilter,
                                       Set<String> interactorTypeFilter,
                                       Set<String> interactionDetectionMethodFilter,
                                       Set<String> interactionTypeFilter,
                                       Set<String> interactionHostOrganismFilter,
                                       boolean isNegativeFilter,
                                       double minMiScore,
                                       double maxMiScore,
                                       boolean interSpecies,
                                       Set<Long> binaryInteractionIdFilter,
                                       Set<String> interactorAcFilter
    ) {
        return interactionRepository.countInteractionResult(query, batchSearch, interactorAc, interactorSpeciesFilter,
                interactorTypeFilter, interactionDetectionMethodFilter, interactionTypeFilter,
                interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies,
                binaryInteractionIdFilter, interactorAcFilter);
    }

    public long countTotal() {
        return this.interactionRepository.countByDocumentType(DocumentType.INTERACTION);
    }
}
