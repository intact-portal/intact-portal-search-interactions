package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.InteractionResult;

import java.util.Set;

/**
 * @author Elisabet Barrera
 */

@Repository
public interface CustomizedInteractionRepository {

    /**
     * @param query
     * @param detectionMethodFilter (optional)
     * @param interactionTypeFilter (optional)
     * @param hostOrganismFilter    (optional)
     * @param isNegativeFilter      (optional)
     * @param sort
     * @param pageable
     * @return
     */
    InteractionResult findInteractionWithFacet(String query, Set<String> detectionMethodFilter, Set<String> interactionTypeFilter, Set<String> hostOrganismFilter, boolean isNegativeFilter, double minMiScore, double maxMiScore,Set<String> species, boolean interSpecies, Sort sort, Pageable pageable);

}
