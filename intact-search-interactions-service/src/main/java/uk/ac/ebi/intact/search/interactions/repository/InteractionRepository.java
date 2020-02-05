package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;

import java.util.Optional;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.DEFAULT;

/**
 * @author Elisabet Barrera
 */
@Repository
public interface InteractionRepository extends SolrCrudRepository<SearchInteraction, String>, CustomizedInteractionRepository {

    @Query(value = DEFAULT + ":?0")
    Page<SearchInteraction> findInteractions(String query, Pageable pageable);

    Optional<SearchInteraction> findByAc(String ac);
}
