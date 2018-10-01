package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionResult;

import java.util.Set;

/**
 * @author Elisabet Barrera
 */

@Repository
public interface CustomizedInteractionRepository {

    /**
     *
     * @param query
     * @param detectionMethodFilter (Optional)
     * @param interactionTypeFilter (Optional)
     * @param hostOrganismFilter (Optional)
     * @param isNegativeFilter (Optional)
     * @param minMiScore
     * @param maxMiScore
     * @param species (Optional)
     * @param interSpecies
     * @param sort
     * @param pageable
     * @return
     */
    SearchInteractionResult findInteractionWithFacet(String query, Set<String> detectionMethodFilter,
                                                     Set<String> interactionTypeFilter, Set<String> hostOrganismFilter,
                                                     boolean isNegativeFilter, double minMiScore, double maxMiScore,
                                                     Set<String> species, boolean interSpecies, Sort sort, Pageable pageable);

}
