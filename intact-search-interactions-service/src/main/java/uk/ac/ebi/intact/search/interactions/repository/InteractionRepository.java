package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.utils.Constants;

import java.util.Optional;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.DEFAULT;
import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.DOCUMENT_TYPE;

/**
 * @author Elisabet Barrera
 */
@Repository
public interface InteractionRepository extends SolrCrudRepository<SearchInteraction, String>, CustomizedInteractionRepository {

    @Query(value = DOCUMENT_TYPE + ":" + Constants.INTERACTION_DOCUMENT_TYPE_VALUE + " AND " + DEFAULT + ":?0")
    Page<SearchInteraction> findInteractions(String query, Pageable pageable);

    Optional<SearchInteraction> findByAc(String ac);

    long countByDocumentType(String interactions);
}
