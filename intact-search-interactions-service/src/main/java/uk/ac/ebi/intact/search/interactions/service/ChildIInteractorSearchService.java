package uk.ac.ebi.intact.search.interactions.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.repository.ChildInteractorRepository;
import uk.ac.ebi.intact.search.interactions.utils.Constants;

import java.util.Set;

/**
 * Created by anjali on 13/02/20.
 */
@Service
public class ChildIInteractorSearchService {

    private final ChildInteractorRepository childInteractorRepository;

    public ChildIInteractorSearchService(ChildInteractorRepository childInteractorRepository) {
        this.childInteractorRepository = childInteractorRepository;
    }

    public GroupPage<SearchChildInteractor> findInteractorsWithGroup(String query,
                                                                     Set<String> interactorSpeciesFilter,
                                                                     Set<String> interactorTypeFilter,
                                                                     Set<String> interactionDetectionMethodFilter,
                                                                     Set<String> interactionTypeFilter,
                                                                     Set<String> interactionHostOrganismFilter,
                                                                     boolean isNegativeFilter,
                                                                     double minMiScore,
                                                                     double maxMiScore,
                                                                     boolean interSpecies,
                                                                     int page,
                                                                     int pageSize) {
        return childInteractorRepository.findChildInteractors(query, interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies, null,
                PageRequest.of(page, pageSize));
    }

    public long countTotal() {
        return this.childInteractorRepository.countByDocumentType(Constants.INTERACTOR_DOCUMENT_TYPE_VALUE);
    }
}
