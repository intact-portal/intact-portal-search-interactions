package uk.ac.ebi.intact.search.interactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionResult;
import uk.ac.ebi.intact.search.interactions.repository.InteractionRepository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Elisabet Barrera
 */
@Service
public class InteractionSearchService {

    @Autowired
    @Qualifier("interactionRepository")
    private InteractionRepository interactionRepository;

    public Iterable<SearchInteraction> findAll() {
        return this.interactionRepository.findAll();
    }

    public Optional<SearchInteraction> findBy(String id) {
        return this.interactionRepository.findById(id);
    }

    public Page<SearchInteraction> findInteractions(String query) {
        PageRequest pageRequest = new PageRequest(0, 10);
        return interactionRepository.findInteractions(query, pageRequest);
    }

    public SearchInteractionResult findInteractionWithFacet(String query,
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
        PageRequest pageRequest = new PageRequest(page, pageSize);
        return interactionRepository.findInteractionWithFacet(query, detectionMethodFilter, interactionTypeFilter,
                hostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, species, interSpecies, null,
                pageRequest);
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
        PageRequest pageRequest = new PageRequest(page, pageSize);
        return interactionRepository.findInteractionForGraphJson(query, detectionMethodFilter, interactionTypeFilter,
                hostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, species, interSpecies, null,
                pageRequest);
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

}
