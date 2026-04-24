package uk.ac.ebi.intact.search.interactions.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;
import uk.ac.ebi.intact.search.interactions.model.parameters.SimpleInteractionQueryParameters;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.parameters.*;
import uk.ac.ebi.intact.search.interactions.repository.InteractionRepository;
import uk.ac.ebi.intact.search.interactions.utils.DocumentType;
import uk.ac.ebi.intact.search.interactions.utils.SearchInteractionUtility;

import java.util.Optional;

/**
 * @author Elisabet Barrera
 */
@Service
public class InteractionSearchService {

    private final InteractionRepository interactionRepository;

    public InteractionSearchService(InteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    public Iterable<SearchInteraction> findAll() {
        return this.interactionRepository.findAll();
    }

    public Optional<SearchInteraction> findByInteractionAc(String ac) {
        return this.interactionRepository.findByAc(ac);
    }

    public Page<SearchInteraction> findInteractions(String query) {
        return findInteractions(query, PageRequest.of(0, 10));
    }

    public Page<SearchInteraction> findInteractions(String query, Pageable pageable) {
        query = SearchInteractionUtility.escapeQueryChars(query);
        return interactionRepository.findInteractions(query, pageable);
    }

    public FacetPage<SearchInteraction> findInteractionFacets(InteractionSearchParameters parameters) {
        return this.interactionRepository.findInteractionFacets(parameters);
    }

    public FacetPage<SearchInteraction> findInteractionWithFacet(PagedInteractionSearchParameters parameters) {
        return interactionRepository.findInteractionWithFacet(parameters);
    }

    public Page<Long> findBinaryInteractionIds(SimpleInteractionQueryParameters simpleInteractionQueryParameters) {
        return interactionRepository.findBinaryInteractionIds(simpleInteractionQueryParameters);
    }

    public Page<SearchInteraction> findInteractionForGraphJson(PagedInteractionGraphJSONParameters parameters) {
        return interactionRepository.findInteractionForGraphJson(parameters.toPagedInteractionSearchParameters());
    }

    public FacetPage<SearchInteraction> findInteractionForGraphJsonWithFacet(PagedInteractionGraphJSONParameters parameters) {
        return interactionRepository.findInteractionForGraphJsonWithFacet(parameters.toPagedInteractionSearchParameters());
    }

    public Page<SearchInteraction> findInteractionIdentifiersWithFormat(PagedFormattedInteractionSearchParameters parameters) {
        return interactionRepository.findInteractionIdentifiersWithFormat(parameters);
    }


    public Page<SearchInteraction> findInteractionIdentifiers(PagedInteractionSearchParameters parameters) {
        return interactionRepository.findInteractionIdentifiers(parameters);

    }

    public long countInteractionsForGraphJson(InteractionGraphJSONParameters parameters) {
        return interactionRepository.countInteractionsForGraphJson(InteractionSearchParameters.copyParameters(parameters).build());
    }

    public long countInteractionResult(String interactorAc, InteractionSearchParameters parameters) {
        return interactionRepository.countInteractionResult(interactorAc, parameters);
    }

    public long countInteractionResult(InteractionSearchParameters parameters) {
        return interactionRepository.countInteractionResult(parameters);
    }

    public long countTotal() {
        return this.interactionRepository.countByDocumentType(DocumentType.INTERACTION);
    }
}