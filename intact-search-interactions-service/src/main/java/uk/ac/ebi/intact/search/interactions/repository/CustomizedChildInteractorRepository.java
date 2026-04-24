package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.parameters.InteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionSearchParameters;

import java.util.Set;

/**
 * Created by anjali on 13/02/20.
 */
@Repository
public interface CustomizedChildInteractorRepository {

    /**
     * @return the interactors matching all the criteria
     */
    GroupPage<SearchChildInteractor> findChildInteractors(PagedInteractionSearchParameters parameters);

    /**
     * @return the number of interactors matching all the criteria
     */
    long countChildInteractors(InteractionSearchParameters parameters);
}