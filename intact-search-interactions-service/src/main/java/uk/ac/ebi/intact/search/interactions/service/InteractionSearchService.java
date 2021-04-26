package uk.ac.ebi.intact.search.interactions.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public FacetPage<SearchInteraction> findInteractionFacets(String query,
                                                              boolean batchSearch,
                                                              Set<String> interactorSpeciesFilter,
                                                              Set<String> interactorTypesFilter,
                                                              Set<String> interactionDetectionMethodsFilter,
                                                              Set<String> interactionTypesFilter,
                                                              Set<String> interactionHostOrganismsFilter,
                                                              boolean negativeFilter,
                                                              boolean mutationFilter,
                                                              boolean expansionFilter,
                                                              double minMIScore,
                                                              double maxMIScore,
                                                              boolean intraSpeciesFilter,
                                                              Set<Long> binaryInteractionIds,
                                                              Set<String> interactorAcs) {
        return this.interactionRepository.findInteractionFacets(query, batchSearch, interactorSpeciesFilter,
                interactorTypesFilter, interactionDetectionMethodsFilter, interactionTypesFilter,
                interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter,
                binaryInteractionIds, interactorAcs);
    }

    public FacetPage<SearchInteraction> findInteractionWithFacet(String query,
                                                                 boolean batchSearch,
                                                                 Set<String> interactorSpeciesFilter,
                                                                 Set<String> interactorTypesFilter,
                                                                 Set<String> interactionDetectionMethodsFilter,
                                                                 Set<String> interactionTypesFilter,
                                                                 Set<String> interactionHostOrganismsFilter,
                                                                 boolean negativeFilter,
                                                                 boolean mutationFilter,
                                                                 boolean expansionFilter,
                                                                 double minMIScore,
                                                                 double maxMIScore,
                                                                 boolean intraSpeciesFilter,
                                                                 Set<Long> binaryInteractionIds,
                                                                 Set<String> interactorAcs,
                                                                 int page,
                                                                 int pageSize) {
        return interactionRepository.findInteractionWithFacet(query, batchSearch, interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter, binaryInteractionIds, interactorAcs, null,
                PageRequest.of(page, pageSize));
    }

    public Page<SearchInteraction> findInteractionForGraphJson(String query,
                                                               boolean batchSearch,
                                                               Set<String> interactorSpeciesFilter,
                                                               Set<String> interactorTypesFilter,
                                                               Set<String> interactionDetectionMethodsFilter,
                                                               Set<String> interactionTypesFilter,
                                                               Set<String> interactionHostOrganismsFilter,
                                                               boolean negativeFilter,
                                                               boolean mutationFilter,
                                                               boolean expansionFilter,
                                                               double minMIScore,
                                                               double maxMIScore,
                                                               boolean intraSpeciesFilter,
                                                               int page,
                                                               int pageSize) {
        return interactionRepository.findInteractionForGraphJson(query, batchSearch, interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter, null,
                PageRequest.of(page, pageSize));
    }

    public FacetPage<SearchInteraction> findInteractionForGraphJsonWithFacet(String query,
                                                                             boolean batchSearch,
                                                                             Set<String> interactorSpeciesFilter,
                                                                             Set<String> interactorTypesFilter,
                                                                             Set<String> interactionDetectionMethodsFilter,
                                                                             Set<String> interactionTypesFilter,
                                                                             Set<String> interactionHostOrganismsFilter,
                                                                             boolean negativeFilter,
                                                                             boolean mutationFilter,
                                                                             boolean expansionFilter,
                                                                             double minMiScore,
                                                                             double maxMiScore,
                                                                             boolean intraSpeciesFilter,
                                                                             int page,
                                                                             int pageSize) {
        return interactionRepository.findInteractionForGraphJsonWithFacet(query, batchSearch, interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMiScore, maxMiScore, intraSpeciesFilter, null,
                PageRequest.of(page, pageSize));
    }

    public Page<SearchInteraction> findInteractionIdentifiers(String query,
                                                              boolean batchSearch,
                                                              Set<String> interactorSpeciesFilters,
                                                              Set<String> interactorTypesFilter,
                                                              Set<String> interactionDetectionMethodsFilter,
                                                              Set<String> interactionTypesFilter,
                                                              Set<String> interactionHostOrganismsFilter,
                                                              boolean negativeFilter,
                                                              boolean mutationFilter,
                                                              boolean expansionFilter,
                                                              double minMiScore,
                                                              double maxMiScore,
                                                              boolean intraSpeciesFilter,
                                                              Set<Long> binaryInteractionIds,
                                                              Set<String> interactorAcs,
                                                              Pageable page) {
        return interactionRepository.findInteractionIdentifiers(query, batchSearch, interactorSpeciesFilters, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMiScore, maxMiScore, intraSpeciesFilter, binaryInteractionIds, interactorAcs, null,
                page);
    }

    public Page<SearchInteraction> findInteractionIdentifiers(String query,
                                                              boolean batchSearch,
                                                              Set<String> interactorSpeciesFilter,
                                                              Set<String> interactorTypesFilter,
                                                              Set<String> interactionDetectionMethodsFilter,
                                                              Set<String> interactionTypesFilter,
                                                              Set<String> interactionHostOrganismsFilter,
                                                              boolean negativeFilter,
                                                              boolean mutationFilter,
                                                              boolean expansionFilter,
                                                              double minMiScore,
                                                              double maxMiScore,
                                                              boolean intraSpeciesFilter,
                                                              Set<Long> binaryInteractionIds,
                                                              Set<String> interactorAcs,
                                                              int page,
                                                              int pageSize) {
        return interactionRepository.findInteractionIdentifiers(query, batchSearch, interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMiScore, maxMiScore, intraSpeciesFilter, binaryInteractionIds, interactorAcs, null,
                PageRequest.of(page, pageSize));

    }

    // TODO CHECK IF WE NEED IT
    public long countInteractionsForGraphJson(String query,
                                              boolean batchSearch,
                                              Set<String> interactorSpeciesFilter,
                                              Set<String> interactorTypesFilter,
                                              Set<String> interactionDetectionMethodsFilter,
                                              Set<String> interactionTypesFilter,
                                              Set<String> interactionHostOrganismsFilter,
                                              boolean negativeFilter,
                                              boolean mutationFilter,
                                              boolean expansionFilter,
                                              double minMIScore,
                                              double maxMIScore,
                                              boolean intraSpeciesFilter) {
        return interactionRepository.countInteractionsForGraphJson(query, batchSearch, interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter);
    }

    public long countInteractionResult(String query,
                                       boolean batchSearch,
                                       String interactorAc,
                                       Set<String> interactorSpeciesFilter,
                                       Set<String> interactorTypesFilter,
                                       Set<String> interactionDetectionMethodsFilter,
                                       Set<String> interactionTypesFilter,
                                       Set<String> interactionHostOrganismsFilter,
                                       boolean negativeFilter,
                                       boolean mutationFilter,
                                       boolean expansionFilter,
                                       double minMIScore,
                                       double maxMIScore,
                                       boolean intraSpeciesFilter,
                                       Set<Long> binaryInteractionIds,
                                       Set<String> interactorAcs
    ) {
        return interactionRepository.countInteractionResult(query, batchSearch, interactorAc, interactorSpeciesFilter,
                interactorTypesFilter, interactionDetectionMethodsFilter, interactionTypesFilter,
                interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter,
                binaryInteractionIds, interactorAcs);
    }

    public long countInteractionResult(String query,
                                       boolean batchSearch,
                                       Set<String> interactorSpeciesFilter,
                                       Set<String> interactorTypesFilter,
                                       Set<String> interactionDetectionMethodsFilter,
                                       Set<String> interactionTypesFilter,
                                       Set<String> interactionHostOrganismsFilter,
                                       boolean negativeFilter,
                                       boolean mutationFilter,
                                       boolean expansionFilter,
                                       double minMIScore,
                                       double maxMIScore,
                                       boolean intraSpeciesFilter,
                                       Set<Long> binaryInteractionIds,
                                       Set<String> interactorAcs
    ) {
        return interactionRepository.countInteractionResult(query, batchSearch, interactorSpeciesFilter,
                interactorTypesFilter, interactionDetectionMethodsFilter, interactionTypesFilter,
                interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter,
                binaryInteractionIds, interactorAcs);
    }

    public long countTotal() {
        return this.interactionRepository.countByDocumentType(DocumentType.INTERACTION);
    }
}