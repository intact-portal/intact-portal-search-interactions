package uk.ac.ebi.intact.search.interactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.Interaction;
import uk.ac.ebi.intact.search.interactions.repository.InteractionRepository;

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

    public Interaction findBy(String id) {
        return this.interactionRepository.findOne(id);
    }

    public long countDocuments() {
        return this.interactionRepository.count();
    }
}
