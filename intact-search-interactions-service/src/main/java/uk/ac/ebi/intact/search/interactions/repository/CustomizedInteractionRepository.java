package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;

import java.util.Set;

/**
 * @author Elisabet Barrera
 */

@Repository
public interface CustomizedInteractionRepository {

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
     * @return the interaction facets matching all the criteria
     */
    public FacetPage<SearchInteraction> findInteractionFacets(String query,
                                                              boolean batchSearch,
                                                              Set<String> interactorSpeciesFilter,
                                                              Set<String> interactorTypesFilter,
                                                              Set<String> interactionDetectionMethodsFilter,
                                                              Set<String> interactionTypesFilter,
                                                              Set<String> interactionHostOrganismsFilter,
                                                              Boolean negativeFilter,
                                                              boolean mutationFilter,
                                                              boolean expansionFilter,
                                                              double minMIScore,
                                                              double maxMIScore,
                                                              boolean intraSpeciesFilter,
                                                              Set<Long> binaryInteractionIds,
                                                              Set<String> interactorAcs);

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
    Page<SearchInteraction> findInteractions(String query,
                                             boolean batchSearch,
                                             Set<String> interactorSpeciesFilter,
                                             Set<String> interactorTypesFilter,
                                             Set<String> interactionDetectionMethodsFilter,
                                             Set<String> interactionTypesFilter,
                                             Set<String> interactionHostOrganismsFilter,
                                             boolean negativeFilter,
                                             boolean mutationFilter,
                                             boolean expansionFilter,
                                             double minMIScore,
                                             double maxMIScore,
                                             boolean intraSpeciesFilter,
                                             Set<Long> binaryInteractionIds,
                                             Set<String> interactorAcs,
                                             Sort sort, Pageable pageable);

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
    FacetPage<SearchInteraction> findInteractionWithFacet(String query,
                                                          boolean batchSearch,
                                                          Set<String> interactorSpeciesFilter,
                                                          Set<String> interactorTypesFilter,
                                                          Set<String> interactionDetectionMethodsFilter,
                                                          Set<String> interactionTypesFilter,
                                                          Set<String> interactionHostOrganismsFilter,
                                                          Boolean negativeFilter,
                                                          boolean mutationFilter,
                                                          boolean expansionFilter,
                                                          double minMIScore,
                                                          double maxMIScore,
                                                          boolean intraSpeciesFilter,
                                                          Set<Long> binaryInteractionIds,
                                                          Set<String> interactorAcs,
                                                          Sort sort, Pageable pageable);

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
    Page<SearchInteraction> findInteractionForGraphJson(String query,
                                                        boolean batchSearch,
                                                        Set<String> interactorSpeciesFilter,
                                                        Set<String> interactorTypesFilter,
                                                        Set<String> interactionDetectionMethodsFilter,
                                                        Set<String> interactionTypesFilter,
                                                        Set<String> interactionHostOrganismsFilter,
                                                        boolean negativeFilter,
                                                        boolean mutationFilter,
                                                        boolean expansionFilter,
                                                        double minMIScore,
                                                        double maxMIScore,
                                                        boolean intraSpeciesFilter, Sort sort, Pageable pageable);

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
    FacetPage<SearchInteraction> findInteractionForGraphJsonWithFacet(String query,
                                                                      boolean batchSearch,
                                                                      Set<String> interactorSpeciesFilter,
                                                                      Set<String> interactorTypesFilter,
                                                                      Set<String> interactionDetectionMethodsFilter,
                                                                      Set<String> interactionTypesFilter,
                                                                      Set<String> interactionHostOrganismsFilter,
                                                                      boolean negativeFilter,
                                                                      boolean mutationFilter,
                                                                      boolean expansionFilter,
                                                                      double minMIScore,
                                                                      double maxMIScore,
                                                                      boolean intraSpeciesFilter, Sort sort, Pageable pageable);

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
    long countInteractionsForGraphJson(String query,
                                       boolean batchSearch,
                                       Set<String> interactorSpeciesFilter,
                                       Set<String> interactorTypesFilter,
                                       Set<String> interactionDetectionMethodsFilter,
                                       Set<String> interactionTypesFilter,
                                       Set<String> interactionHostOrganismsFilter,
                                       boolean negativeFilter,
                                       boolean mutationFilter,
                                       boolean expansionFilter,
                                       double minMIScore,
                                       double maxMIScore,
                                       boolean intraSpeciesFilter);

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
    Page<SearchInteraction> findInteractionIdentifiers(String query,
                                                       boolean batchSearch,
                                                       Set<String> interactorSpeciesFilter,
                                                       Set<String> interactorTypesFilter,
                                                       Set<String> interactionDetectionMethodsFilter,
                                                       Set<String> interactionTypesFilter,
                                                       Set<String> interactionHostOrganismsFilter,
                                                       boolean negativeFilter,
                                                       boolean mutationFilter,
                                                       boolean expansionFilter,
                                                       double minMIScore,
                                                       double maxMIScore,
                                                       boolean intraSpeciesFilter,
                                                       Set<Long> binaryInteractionIds,
                                                       Set<String> interactorAcs,
                                                       Sort sort, Pageable pageable);


    /**
     * @param query                             input used to retrieve the interaction
     * @param batchSearch                       (optional) true if que query needs to be treated as a batch search
     * @param interactorSpeciesFilter           (Optional) Filters interactor species of the interaction
     * @param interactorTypesFilter             (Optional) Filters interactions by interactor type            *
     * @param interactionDetectionMethodsFilter (Optional) Filters interactions by interaction detection method
     * @param interactionTypesFilter            (Optional) Filters interactions by interaction type
     * @param interactionHostOrganismsFilter    (Optional) Filters interactions by host organism
     * @param negativeFilter                    (Optional) Filters interactions that are negativeFilter if true
     * @param mutationFilter                    (Optional) Filters interaction that are not affected by mutation
     * @param minMIScore                        minimum value of mi-score for the interaction
     * @param maxMIScore                        maximum value of mi-score for the interaction
     * @param intraSpeciesFilter                boolean to restrict the result ot the same or different interactor species
     * @param binaryInteractionIds
     * @param interactorAcs                     interactor accession e.g. EBI-XXXXXX
     * @return the number of interactions matching all the criteria
     */
    long countInteractionResult(String query,
                                boolean batchSearch,
                                String interactorAc,
                                Set<String> interactorSpeciesFilter,
                                Set<String> interactorTypesFilter,
                                Set<String> interactionDetectionMethodsFilter,
                                Set<String> interactionTypesFilter,
                                Set<String> interactionHostOrganismsFilter,
                                Boolean negativeFilter,
                                boolean mutationFilter,
                                boolean expansionFilter,
                                double minMIScore,
                                double maxMIScore,
                                boolean intraSpeciesFilter,
                                Set<Long> binaryInteractionIds,
                                Set<String> interactorAcs);

    long countInteractionResult(String query,
                                boolean batchSearch,
                                Set<String> interactorSpeciesFilter,
                                Set<String> interactorTypesFilter,
                                Set<String> interactionDetectionMethodsFilter,
                                Set<String> interactionTypesFilter,
                                Set<String> interactionHostOrganismsFilter,
                                boolean negativeFilter,
                                boolean mutationFilter,
                                boolean expansionFilter,
                                double minMIScore,
                                double maxMIScore,
                                boolean intraSpeciesFilter,
                                Set<Long> binaryInteractionIds,
                                Set<String> interactorAcs);
}