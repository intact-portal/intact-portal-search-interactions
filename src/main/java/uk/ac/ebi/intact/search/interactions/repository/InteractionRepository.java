package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.Interaction;

/**
 * @author Elisabet Barrera
 */
@Repository
public interface InteractionRepository extends SolrCrudRepository<Interaction, String> {

    //TODO Add this field as default. It has text_en as FieldType in solr and copy all the values for now
    @Query(value = "text:?0")
//    @Query(value = DEFAULT + ":?0")
    Page<Interaction> findInteractions(String query, Pageable pageable);
}
