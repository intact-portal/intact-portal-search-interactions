package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;

import java.util.Set;

/**
 * Created by anjali on 13/02/20.
 */
@Repository
public interface CustomizedChildInteractorRepository {

    /**
     * @param query                             input used to retrieve the interactors contained in the interaction
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
     * @return the interactors matching all the criteria
     */
    GroupPage<SearchChildInteractor> findChildInteractors(String query,
                                                          boolean batchSearch,
                                                          Set<String> interactorSpeciesFilter,
                                                          Set<String> interactorTypesFilter,
                                                          Set<String> interactionDetectionMethodsFilter,
                                                          Set<String> interactionTypesFilter,
                                                          Set<String> interactionHostOrganismsFilter,
                                                          boolean negativeFilter,
                                                          boolean mutationFilter,
                                                          double minMIScore,
                                                          double maxMIScore,
                                                          boolean intraSpeciesFilter,
                                                          Set<Integer> binaryInteractionIds,
                                                          Set<String> interactorAcs,
                                                          Sort sort, Pageable pageable);

    /**
     * @param query                             input used to retrieve the interactors contained in the interaction
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
     * @return the number of interactors matching all the criteria
     */
    long countChildInteractors(String query,
                               boolean batchSearch,
                               Set<String> interactorSpeciesFilter,
                               Set<String> interactorTypesFilter,
                               Set<String> interactionDetectionMethodsFilter,
                               Set<String> interactionTypesFilter,
                               Set<String> interactionHostOrganismsFilter,
                               boolean negativeFilter,
                               boolean mutationFilter,
                               double minMIScore,
                               double maxMIScore,
                               boolean intraSpeciesFilter,
                               Set<Integer> binaryInteractionIds,
                               Set<String> interactorAcs);
}
