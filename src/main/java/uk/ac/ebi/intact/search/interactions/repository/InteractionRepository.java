package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.Interaction;

/**
 * @author Elisabet Barrera
 */
@Repository
public interface InteractionRepository extends SolrCrudRepository<Interaction, String> {

}
