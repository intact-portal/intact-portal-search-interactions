package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;

import java.util.Optional;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.DEFAULT;

/**
 * Created by anjali on 13/02/20.
 */
@Repository
public interface ChildInteractorRepository extends SolrCrudRepository<SearchChildInteractor, String>, CustomizedChildInteractorRepository {

}
