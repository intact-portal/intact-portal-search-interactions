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
     * @param query input used to retrieve the interaction
     * @param detectionMethodFilter (Optional) filter interactions by interaction detection method
     * @param interactionTypeFilter (Optional) filter interactions by interaction type
     * @param hostOrganismFilter (Optional) filter interactions by host organism
     * @param isNegativeFilter (Optional) filter interactions that are negative if true
     * @param minMiScore minimun value of mi-score for the interaction
     * @param maxMiScore minimun value of mi-score for the interaction
     * @param species (Optional) interactor species of the interaction
     * @param interSpecies boolean to restrict the result ot the same or different interactor species
     * @param sort field to define the sort of the results
     * @param pageable  page number and size of the request
     * @return the interaction data matching all the criteria
     */
    FacetPage<SearchInteraction> findInteractionWithFacet(String query, Set<String> detectionMethodFilter,
                                                          Set<String> interactionTypeFilter, Set<String> hostOrganismFilter,
                                                          boolean isNegativeFilter, double minMiScore, double maxMiScore,
                                                          Set<String> species, boolean interSpecies, Sort sort, Pageable pageable);

    /**
     * @param query input used to retrieve the interaction
     * @param detectionMethodFilter (Optional) filter interactions by interaction detection method
     * @param interactionTypeFilter (Optional) filter interactions by interaction type
     * @param hostOrganismFilter (Optional) filter interactions by host organism
     * @param isNegativeFilter (Optional) filter interactions that are negative if true
     * @param minMiScore minimun value of mi-score for the interaction
     * @param maxMiScore minimun value of mi-score for the interaction
     * @param species (Optional) interactor species of the interaction
     * @param interSpecies boolean to restrict the result ot the same or different interactor species
     * @param sort field to define the sort of the results
     * @param pageable  page number and size of the request
     * @return the interaction page matching all the criteria for graphical representation
     */
     Page<SearchInteraction> findInteractionForGraphJson(String query, Set<String> detectionMethodFilter,
                                                             Set<String> interactionTypeFilter, Set<String> hostOrganismFilter,
                                                             boolean isNegativeFilter, double minMiScore, double maxMiScore,
                                                             Set<String> species, boolean interSpecies, Sort sort, Pageable pageable);

    /**
     * @param query input used to retrieve the interaction
     * @param interactorAc interactor accession e.g. EBI-XXXXXX
     * @param detectionMethodFilter (Optional) filter interactions by interaction detection method
     * @param interactionTypeFilter (Optional) filter interactions by interaction type
     * @param hostOrganismFilter (Optional) filter interactions by host organism
     * @param isNegativeFilter (Optional) filter interactions that are negative if true
     * @param minMiScore minimun value of mi-score for the interaction
     * @param maxMiScore minimun value of mi-score for the interaction
     * @param species (Optional) interactor species of the interaction
     * @param interSpecies boolean to restrict the result ot the same or different interactor species
     * @return the number of interactions matching all the criteria
     */
    long countInteractionResult(String query, String interactorAc, Set<String> detectionMethodFilter,
                                Set<String> interactionTypeFilter, Set<String> hostOrganismFilter,
                                boolean isNegativeFilter, double minMiScore, double maxMiScore,
                                Set<String> species, boolean interSpecies);
}
