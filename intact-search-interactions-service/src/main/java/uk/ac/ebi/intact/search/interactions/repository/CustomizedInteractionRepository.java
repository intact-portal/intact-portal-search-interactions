package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.parameters.InteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedFormattedInteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionGraphJSONParameters;
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

    /**
     * @param query                             input used to retrieve the interaction
     * @param batchSearch                       (optional) true if que query needs to be treated as a batch search
     * @param interactorSpeciesFilter           (Optional) Filters interactor species of the interaction
     * @param interactorTypesFilter             (Optional) Filters interactions by interactor type
     * @param interactionDetectionMethodsFilter (Optional) Filters interactions by interaction detection method
     * @param interactionTypesFilter            (Optional) Filters interactions by interaction type
     * @param interactionHostOrganismsFilter    (Optional) Filters interactions by host organism
     * @param negativeFilter                    (Optional) Filters interactions that are negativeFilter if true
     * @param mutationFilter                    (Optional) Filters interaction that are not affected by mutation
     * @param minMIScore                        minimum value of mi-score for the interaction
     * @param maxMIScore                        maximum value of mi-score for the interaction
     * @param intraSpeciesFilter                boolean to restrict the result ot the same or different interactor species
     * @param sort                              field to define the sort of the results
     * @param pageable                          page number and size of the request
     * @return the interaction data matching all the criteria
     */
    Page<SearchInteraction> findInteractions(PagedInteractionSearchParameters parameters);

    /**
     * @param query                             input used to retrieve the interaction
     * @param batchSearch                       (optional) true if que query needs to be treated as a batch search
     * @param interactorSpeciesFilter           (Optional) Filters interactor species of the interaction
     * @param interactorTypesFilter             (Optional) Filters interactions by interactor type
     * @param interactionDetectionMethodsFilter (Optional) Filters interactions by interaction detection method
     * @param interactionTypesFilter            (Optional) Filters interactions by interaction type
     * @param interactionHostOrganismsFilter    (Optional) Filters interactions by host organism
     * @param negativeFilter                    (Optional) Filters interactions that are negativeFilter if true
     * @param mutationFilter                    (Optional) Filters interaction that are not affected by mutation
     * @param minMIScore                        minimum value of mi-score for the interaction
     * @param maxMIScore                        maximum value of mi-score for the interaction
     * @param intraSpeciesFilter                boolean to restrict the result ot the same or different interactor species
     * @param sort                              field to define the sort of the results
     * @param pageable                          page number and size of the request
     * @return the interaction data matching all the criteria
     */
    FacetPage<SearchInteraction> findInteractionWithFacet(PagedInteractionSearchParameters parameters);

    /**
     * @param query                             input used to retrieve the interaction
     * @param batchSearch                       (Optional) true if que query needs to be treated as a batch search
     * @param interactorSpeciesFilter           (Optional) Filters interactor species of the interaction
     * @param interactorTypesFilter             (Optional) Filters interactions by interactor type
     * @param interactionDetectionMethodsFilter (Optional) Filters interactions by interaction detection method
     * @param interactionTypesFilter            (Optional) Filters interactions by interaction type
     * @param interactionHostOrganismsFilter    (Optional) Filters interactions by host organism
     * @param negativeFilter                    (Optional) Filters interactions that are negativeFilter if true
     * @param mutationFilter                    (Optional) Filters interaction that are not affected by mutation
     * @param minMIScore                        minimum value of mi-score for the interaction
     * @param maxMIScore                        maximum value of mi-score for the interaction
     * @param intraSpeciesFilter                boolean to restrict the result ot the same or different interactor species
     * @param sort                              field to define the sort of the results
     * @param pageable                          page number and size of the request
     * @return the interaction page matching all the criteria for graphical representation
     */
    Page<SearchInteraction> findInteractionForGraphJson(PagedInteractionSearchParameters parameters);

    /**
     * @param query                             input used to retrieve the interaction
     * @param batchSearch                       (Optional) true if que query needs to be treated as a batch search
     * @param interactorSpeciesFilter           (Optional) Filters interactor species of the interaction
     * @param interactorTypesFilter             (Optional) Filters interactions by interactor type
     * @param interactionDetectionMethodsFilter (Optional) Filters interactions by interaction detection method
     * @param interactionTypesFilter            (Optional) Filters interactions by interaction type
     * @param interactionHostOrganismsFilter    (Optional) Filters interactions by host organism
     * @param negativeFilter                    (Optional) Filters interactions that are negativeFilter if true
     * @param mutationFilter                    (Optional) Filters interaction that are not affected by mutation
     * @param minMIScore                        minimum value of mi-score for the interaction
     * @param maxMIScore                        maximum value of mi-score for the interaction
     * @param intraSpeciesFilter                boolean to restrict the result ot the same or different interactor species
     * @param sort                              field to define the sort of the results
     * @param pageable                          page number and size of the request
     * @return the interaction page matching all the criteria for graphical representation
     */
    FacetPage<SearchInteraction> findInteractionForGraphJsonWithFacet(PagedInteractionSearchParameters parameters);

    /**
     * @param query                             input used to retrieve the interaction
     * @param batchSearch                       (Optional) true if que query needs to be treated as a batch search
     * @param interactorSpeciesFilter           (Optional) Filters interactions by interactor species
     * @param interactorTypesFilter             (Optional) Filters interactions by interactor type
     * @param interactionDetectionMethodsFilter (Optional) Filters interactions by interaction detection method
     * @param interactionTypesFilter            (Optional) Filters interactions by interaction type
     * @param interactionHostOrganismsFilter    (Optional) Filters interactions by host organism
     * @param negativeFilter                    (Optional) Filters interactions that are negativeFilter if true
     * @param mutationFilter                    (Optional) Filters interaction that are not affected by mutation
     * @param minMIScore                        minimum value of mi-score for the interaction
     * @param maxMIScore                        maximum value of mi-score for the interaction
     * @param intraSpeciesFilter                boolean to restrict the result ot the same or different interactor species
     * @return the number of interactions all the criteria for graphical representation
     */
    long countInteractionsForGraphJson(InteractionSearchParameters parameters);

    /**
     * @param query                             input used to retrieve the interaction
     * @param batchSearch                       (optional) true if que query needs to be treated as a batch search
     * @param interactorSpeciesFilter           (Optional) interactor species of the interaction
     * @param interactorTypesFilter             (Optional) filter interactions by interactor type
     * @param interactionDetectionMethodsFilter (Optional) filter interactions by interaction detection method
     * @param interactionTypesFilter            (Optional) filter interactions by interaction type
     * @param interactionHostOrganismsFilter    (Optional) filter interactions by host organism
     * @param negativeFilter                    (Optional) filter interactions that are negativeFilter if true
     * @param mutationFilter                    (Optional) Filters interaction that are not affected by mutation
     * @param minMIScore                        minimum value of mi-score for the interaction
     * @param maxMIScore                        maximum value of mi-score for the interaction
     * @param intraSpeciesFilter                boolean to restrict the result ot the same or different interactor species
     * @param binaryInteractionIds
     * @param sort                              field to define the sort of the results
     * @param pageable                          page number and size of the request
     * @return the interaction data matching all the criteria
     */
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