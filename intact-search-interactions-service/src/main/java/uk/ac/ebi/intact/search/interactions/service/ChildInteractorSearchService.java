package uk.ac.ebi.intact.search.interactions.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.parameters.InteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionSearchParameters;
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

    public GroupPage<SearchChildInteractor> findInteractorsWithGroup(PagedInteractionSearchParameters parameters) {
        return childInteractorRepository.findChildInteractors(parameters);
    }

    public long countInteractorsWithGroup(InteractionSearchParameters parameters) {
        return childInteractorRepository.countChildInteractors(parameters);
    }

    public long countTotal() {
        return this.childInteractorRepository.countByDocumentType(DocumentType.INTERACTOR);
    }
}