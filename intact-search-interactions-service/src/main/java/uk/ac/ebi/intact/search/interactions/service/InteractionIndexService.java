package uk.ac.ebi.intact.search.interactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.repository.InteractionRepository;

import java.time.Duration;

/**
 * @author Elisabet Barrera
 */
@Service
public class InteractionIndexService {

    private final InteractionRepository interactionRepository;

    @Autowired
    public InteractionIndexService(InteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    @Transactional
    public void deleteAll() {
        this.interactionRepository.deleteAll();
    }

    @Transactional
    public void save(SearchInteraction searchInteraction) {
        this.interactionRepository.save(searchInteraction);
    }

    @Transactional
    public void save(Iterable<SearchInteraction> interactions) {
        this.interactionRepository.saveAll(interactions);
    }

    @Transactional
    public void save(SearchInteraction searchInteraction, Duration commitWithin) {
        this.interactionRepository.save(searchInteraction, commitWithin);
    }

    @Transactional
    public void save(Iterable<SearchInteraction> interactions, Duration commitWithin) {
        this.interactionRepository.saveAll(interactions, commitWithin);
    }

    @Transactional
    public void delete(SearchInteraction id) {
        this.interactionRepository.delete(id);
    }

}
