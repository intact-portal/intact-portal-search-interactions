package uk.ac.ebi.intact.search.interactions.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.repository.ChildInteractorRepository;
import uk.ac.ebi.intact.search.interactions.utils.DocumentType;

import java.util.Set;

/**
 * Created by anjali on 13/02/20.
 */
@Service
public class ChildInteractorSearchService {

    private final ChildInteractorRepository childInteractorRepository;

    public ChildInteractorSearchService(ChildInteractorRepository childInteractorRepository) {
        this.childInteractorRepository = childInteractorRepository;
    }

    public GroupPage<SearchChildInteractor> findInteractorsWithGroup(String query,
                                                                     boolean batchSearch,
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
        return childInteractorRepository.findChildInteractors(query, batchSearch, interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies, null,
                PageRequest.of(page, pageSize));
    }

    public long countInteractorsWithGroup(String query,
                                          Set<String> interactorSpeciesFilter,
                                          Set<String> interactorTypeFilter,
                                          Set<String> interactionDetectionMethodFilter,
                                          Set<String> interactionTypeFilter,
                                          Set<String> interactionHostOrganismFilter,
                                          boolean isNegativeFilter,
                                          double minMiScore,
                                          double maxMiScore,
                                          boolean interSpecies) {
        return childInteractorRepository.countChildInteractors(query, interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies);
    }

    public long countTotal() {
        return this.childInteractorRepository.countByDocumentType(DocumentType.INTERACTOR);
    }
}
