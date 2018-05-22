package uk.ac.ebi.intact.search.interactions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.search.interactions.model.Interaction;
import uk.ac.ebi.intact.search.interactions.repository.InteractionRepository;

import java.util.Collection;

/**
 * @author Elisabet Barrera
 */
@Service
public class InteractionIndexService {

    @Autowired
    @Qualifier("interactionRepository")
    private InteractionRepository interactionRepository;

    @Transactional
    public void deleteAll() {
        this.interactionRepository.deleteAll();
    }

    @Transactional
    public void save(Interaction interaction) {
        this.interactionRepository.save(interaction);
    }

    @Transactional
    public void save(Collection<Interaction> interactions) {
        this.interactionRepository.save(interactions);
    }

    @Transactional
    public void delete(Interaction id) {
        this.interactionRepository.delete(id);
    }

}
