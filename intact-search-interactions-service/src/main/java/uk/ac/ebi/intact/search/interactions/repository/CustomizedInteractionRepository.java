package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SimpleInteractionQueryParameters;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.parameters.InteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedFormattedInteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionSearchParameters;

/**
 * @author Elisabet Barrera
 */

@Repository
public interface CustomizedInteractionRepository {

    /**
     * @param parameters the different query parameters
     * @return the interaction facets matching all the criteria
     */
    public FacetPage<SearchInteraction> findInteractionFacets(InteractionSearchParameters parameters);

    Page<SearchInteraction> findInteractions(PagedInteractionSearchParameters parameters);


    FacetPage<SearchInteraction> findInteractionWithFacet(PagedInteractionSearchParameters parameters);

    Page<Long> findBinaryInteractionIds(SimpleInteractionQueryParameters simpleInteractionQueryParameters);

    Page<SearchInteraction> findInteractionForGraphJson(PagedInteractionSearchParameters parameters);


    FacetPage<SearchInteraction> findInteractionForGraphJsonWithFacet(PagedInteractionSearchParameters parameters);


    long countInteractionsForGraphJson(InteractionSearchParameters parameters);

    Page<SearchInteraction> findInteractionIdentifiers(PagedInteractionSearchParameters parameters);

    /**
     * @param parameters@return the interaction data matching all the criteria
     */
    Page<SearchInteraction> findInteractionIdentifiersWithFormat(PagedFormattedInteractionSearchParameters parameters);

    /**
     * @param interactorAc the interactor ac of which we wanna know how many interactions it has
     * @param parameters The different query parameters
     *
     * @return the number of interactions matching all the criteria
     */
    long countInteractionResult(String interactorAc, InteractionSearchParameters parameters);

    long countInteractionResult(InteractionSearchParameters parameters);
}