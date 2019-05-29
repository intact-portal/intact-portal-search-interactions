package uk.ac.ebi.intact.search.interactions.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.repository.InteractionRepository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Elisabet Barrera
 */
@Service
public class InteractionSearchService {

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
                                                                 Set<String> detectionMethodFilter,
                                                                 Set<String> interactionTypeFilter,
                                                                 Set<String> hostOrganismFilter,
                                                                 boolean isNegativeFilter,
                                                                 double minMiScore,
                                                                 double maxMiScore,
                                                                 Set<String> species,
                                                                 boolean interSpecies,
                                                                 int page,
                                                                 int pageSize) {
        return interactionRepository.findInteractionWithFacet(query, detectionMethodFilter, interactionTypeFilter,
                hostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, species, interSpecies, null,
                PageRequest.of(page, pageSize));
    }

    public Page<SearchInteraction> findInteractionForGraphJson(String query,
                                                            Set<String> detectionMethodFilter,
                                                            Set<String> interactionTypeFilter,
                                                            Set<String> hostOrganismFilter,
                                                            boolean isNegativeFilter,
                                                            double minMiScore,
                                                            double maxMiScore,
                                                            Set<String> species,
                                                            boolean interSpecies,
                                                            int page,
                                                            int pageSize) {
        return interactionRepository.findInteractionForGraphJson(query, detectionMethodFilter, interactionTypeFilter,
                hostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, species, interSpecies, null,
                PageRequest.of(page, pageSize));
    }

    public long countInteractionResult(String query,
                                       String interactorAc,
                                       Set<String> detectionMethodFilter,
                                       Set<String> interactionTypeFilter,
                                       Set<String> hostOrganismFilter,
                                       boolean isNegativeFilter,
                                       double minMiScore,
                                       double maxMiScore,
                                       Set<String> species,
                                       boolean interSpecies) {
        return interactionRepository.countInteractionResult(query, interactorAc, detectionMethodFilter, interactionTypeFilter,
                hostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, species, interSpecies);
    }


    public long countTotal() {
        return this.interactionRepository.count();
    }
}
