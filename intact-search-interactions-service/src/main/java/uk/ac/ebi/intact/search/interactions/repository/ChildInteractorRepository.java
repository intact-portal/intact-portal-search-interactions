package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;

/**
 * Created by anjali on 13/02/20.
 */
@Repository
public interface ChildInteractorRepository extends SolrCrudRepository<SearchChildInteractor, String>, CustomizedChildInteractorRepository {

    long countByDocumentType(String interactions);
}
