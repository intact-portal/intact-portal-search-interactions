package uk.ac.ebi.intact.search.interactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.Interaction;
import uk.ac.ebi.intact.search.interactions.model.InteractionResult;
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

    public Iterable<Interaction> findAll() {
        return this.interactionRepository.findAll();
    }

    public Optional<Interaction> findBy(String id) {
        return this.interactionRepository.findById(id);
    }

    public long countDocuments() {
        return this.interactionRepository.count();
    }

    public Page<Interaction> findInteractions(String query) {
        PageRequest pageRequest = new PageRequest(0, 10);
        return interactionRepository.findInteractions(query, pageRequest);
    }

    public InteractionResult findInteractionWithFacet(String query, Set<String> detectionMethodFilter, Set<String> interactionTypeFilter,Set<String> hostOrganismFilter,boolean isNegativeFilter,double minMiScore,double maxMiScore, int page, int pageSize) {
        PageRequest pageRequest = new PageRequest(page, pageSize);
        return interactionRepository.findInteractionWithFacet(query, detectionMethodFilter, interactionTypeFilter,hostOrganismFilter,isNegativeFilter,minMiScore,maxMiScore, null, pageRequest);
    }

}
