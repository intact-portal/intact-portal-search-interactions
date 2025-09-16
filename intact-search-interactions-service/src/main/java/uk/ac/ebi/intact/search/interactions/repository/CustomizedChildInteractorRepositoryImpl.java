package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.RequestMethod;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.CountEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.GroupPage;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.parameters.InteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.utils.NestedCriteria;
import uk.ac.ebi.intact.search.interactions.utils.SearchInteractionUtility;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static uk.ac.ebi.intact.search.interactions.model.SearchChildInteractorFields.DOCUMENT_ID;
import static uk.ac.ebi.intact.search.interactions.model.SearchChildInteractorFields.INTERACTION_COUNT;
import static uk.ac.ebi.intact.search.interactions.model.SearchInteraction.INTERACTIONS;

/**
 * Created by anjali on 13/02/20.
 */
public class CustomizedChildInteractorRepositoryImpl implements CustomizedChildInteractorRepository {
    // default minimum counts for faceting
    private static final int FACET_MIN_COUNT = 10000;
    // We need to add a new sort field for each top interactor in the search, so we limit we how many
    // interactors we get to not build a query too long.
    private static final int NUMBER_OF_TOP_DOCUMENTS_TO_GET = 50;

    private final SolrOperations solrOperations;
    private final SearchInteractionUtility searchInteractionUtility = new SearchInteractionUtility();

    // default sorting for the query results
    //TODO Solve problems with multivalue fields that are not allow to be sorted. Schema-less create all the fields as multivalues
//    private static final Sort DEFAULT_QUERY_SORT_WITH_QUERY = new Sort(Sort.Direction.DESC, SearchInteractorFields.INTERACTION_COUNT);

    @Autowired
    public CustomizedChildInteractorRepositoryImpl(SolrOperations solrOperations) {
        this.solrOperations = solrOperations;
    }

    @Override
    public GroupPage<SearchChildInteractor> findChildInteractors(PagedInteractionSearchParameters parameters) {
        SimpleQuery search = queryToFindInteractors(parameters);

        // group
        GroupOptions groupOptions = new GroupOptions().addGroupByField(DOCUMENT_ID);
        groupOptions.setLimit(1);
        groupOptions.setGroupMain(true);
        search.setGroupOptions(groupOptions);

        // pagination
        search.setPageRequest(PageRequest.of(parameters.getPage(), parameters.getPageSize()));

        // sorting
        if (parameters.getSort() != null) {
            search.addSort(parameters.standardiseSort());
        } else {
            // First we sort by the document id of the top interactors, to put on top the interactors appearing
            // in multiple interactions in the query results.
            List<String> documentIdFacetsSorted = getDocumentIdsInResultsSorted(parameters);
            documentIdFacetsSorted.forEach(documentIdFacet ->
                    search.addSort(Sort.by(Sort.Direction.DESC, customDocumentIdSortField(documentIdFacet))));
            // Then we sort by the interaction count for each interactor, which counts the total number of interactions
            // in the DB, not just for the current query.
            search.addSort(Sort.by(Sort.Direction.DESC, INTERACTION_COUNT));
        }

        return solrOperations.queryForGroupPage(INTERACTIONS, search, SearchChildInteractor.class,
                (parameters.isBatchSearch() ? RequestMethod.POST : RequestMethod.GET));
    }

    // By default the numCount return by solr when group.main=true is the total documents instead of the number of groups.
    // To make the pagination of interactors working we need to ask solr the number of groups for that group.main=false and
    // group.ngroups=true (setTotalCount(true) in spring-data-solr)
    @Override
    public long countChildInteractors(InteractionSearchParameters parameters) {
        SimpleQuery search = queryToFindInteractors(parameters);

        // group
        GroupOptions groupOptions = new GroupOptions().addGroupByField(DOCUMENT_ID);
        groupOptions.setLimit(1);
        groupOptions.setGroupMain(false);
        groupOptions.setTotalCount(true);
        search.setGroupOptions(groupOptions);

        // pagination
        search.setPageRequest(PageRequest.of(0, 1));

        GroupPage<SearchChildInteractor> groupPage = solrOperations.queryForGroupPage(INTERACTIONS, search, SearchChildInteractor.class,
                (parameters.isBatchSearch() ? RequestMethod.POST : RequestMethod.GET));
        return groupPage.getGroupResult(DOCUMENT_ID).getGroupsCount();
    }

    private SimpleQuery queryToFindInteractors(InteractionSearchParameters parameters) {
        // search query
        SimpleQuery search = new SimpleQuery();
        addFiltersToQueryToFindInteractors(search, parameters);
        return search;
    }

    private SimpleFacetQuery facetQueryToFindInteractors(InteractionSearchParameters parameters) {
        // search query
        SimpleFacetQuery search = new SimpleFacetQuery();
        addFiltersToQueryToFindInteractors(search, parameters);
        return search;
    }

    private void addFiltersToQueryToFindInteractors(SimpleQuery search, InteractionSearchParameters parameters) {
        // filters
        List<FilterQuery> interactionFilterQueries = searchInteractionUtility.createFilterQuery(parameters);

        // search criteria
        Criteria interactionSearchCriteria = searchInteractionUtility.createSearchConditions(parameters);
        Criteria interactorCriteria = new NestedCriteria(interactionSearchCriteria, interactionFilterQueries);
        search.addCriteria(interactorCriteria);
    }

    private SimpleFacetQuery createQueryWithFacetOptions(PagedInteractionSearchParameters parameters, FacetOptions facetOptions) {
        // search query
        SimpleFacetQuery search = facetQueryToFindInteractors(parameters);

        facetOptions.setFacetLimit(FACET_MIN_COUNT);

        search.setFacetOptions(facetOptions);

        // pagination
        search.setPageRequest(PageRequest.of(parameters.getPage(), parameters.getPageSize()));

        // projection
        search.addProjectionOnFields(DOCUMENT_ID);

        return search;
    }

    private FacetPage<SearchChildInteractor> findInteractorFacets(InteractionSearchParameters parameters, FacetOptions facetOptions) {
        // search query
        SimpleFacetQuery search = createQueryWithFacetOptions(
                PagedInteractionSearchParameters.copyParameters(parameters).pageSize(1).build(),
                facetOptions);

        return solrOperations.queryForFacetPage(INTERACTIONS, search, SearchChildInteractor.class,
                (parameters.isBatchSearch() ? RequestMethod.POST : RequestMethod.GET));
    }

    private List<String> getDocumentIdsInResultsSorted(InteractionSearchParameters parameters) {
        FacetOptions facetOptions = new FacetOptions(DOCUMENT_ID);
        // We only want the documents ids of interactors that appear more than once, to prioritise the interactors
        // with multiple interactions.
        facetOptions.setFacetMinCount(2);
        facetOptions.setFacetLimit(NUMBER_OF_TOP_DOCUMENTS_TO_GET);
        FacetPage<SearchChildInteractor> facets = findInteractorFacets(parameters, facetOptions);
        return facets.getFacetResultPage(DOCUMENT_ID).getContent().stream()
                .sorted(Comparator.comparing(CountEntry::getValueCount).reversed())
                .limit(NUMBER_OF_TOP_DOCUMENTS_TO_GET)
                .map(CountEntry::getValue)
                .collect(Collectors.toList());
    }

    private String customDocumentIdSortField(String species) {
        return String.format(
                "termfreq(%s,'%s')",
                DOCUMENT_ID,
                species);
    }
}