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
     * @param query                            input used to retrieve the interaction
     * @param interactorSpeciesFilter          (Optional) interactor species of the interaction
     * @param interactorTypeFilter             (Optional) filter interactions by interactor type
     * @param interactionDetectionMethodFilter (Optional) filter interactions by interaction detection method
     * @param interactionTypeFilter            (Optional) filter interactions by interaction type
     * @param interactionHostOrganismFilter    (Optional) filter interactions by host organism
     * @param isNegativeFilter                 (Optional) filter interactions that are negative if true
     * @param minMiScore                       minimun value of mi-score for the interaction
     * @param maxMiScore                       minimun value of mi-score for the interaction
     * @param interSpecies                     boolean to restrict the result ot the same or different interactor species
     * @param sort                             field to define the sort of the results
     * @param pageable                         page number and size of the request
     * @return the interaction data matching all the criteria
     */
    GroupPage<SearchChildInteractor> findChildInteractors(String query,
                                                          boolean batchSearch,
                                                          Set<String> interactorSpeciesFilter,
                                                          Set<String> interactorTypeFilter,
                                                          Set<String> interactionDetectionMethodFilter,
                                                          Set<String> interactionTypeFilter,
                                                          Set<String> interactionHostOrganismFilter,
                                                          boolean isNegativeFilter,
                                                          double minMiScore,
                                                          double maxMiScore,
                                                          boolean interSpecies, Sort sort, Pageable pageable);

}
