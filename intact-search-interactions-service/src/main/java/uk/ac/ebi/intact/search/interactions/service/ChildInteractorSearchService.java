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
                                                                     Set<String> interactorTypesFilter,
                                                                     Set<String> interactionDetectionMethodsFilter,
                                                                     Set<String> interactionTypesFilter,
                                                                     Set<String> interactionHostOrganismsFilter,
                                                                     boolean negativeFilter,
                                                                     boolean mutationFilter,
                                                                     double minMIScore,
                                                                     double maxMIScore,
                                                                     boolean intraSpeciesFilter,
                                                                     Set<Integer> binaryInteractionIdFilter,
                                                                     Set<String> interactorAcFilter,
                                                                     int page,
                                                                     int pageSize) {
        return childInteractorRepository.findChildInteractors(query, batchSearch, interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, minMIScore, maxMIScore, intraSpeciesFilter, binaryInteractionIdFilter, interactorAcFilter, null,
                PageRequest.of(page, pageSize));
    }

    public long countInteractorsWithGroup(String query,
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
                                          Set<Integer> binaryInteractionIdFilter,
                                          Set<String> interactorAcFilter) {
        return childInteractorRepository.countChildInteractors(query, batchSearch, interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, minMIScore, maxMIScore, intraSpeciesFilter, binaryInteractionIdFilter, interactorAcFilter);
    }

    public long countTotal() {
        return this.childInteractorRepository.countByDocumentType(DocumentType.INTERACTOR);
    }
}
