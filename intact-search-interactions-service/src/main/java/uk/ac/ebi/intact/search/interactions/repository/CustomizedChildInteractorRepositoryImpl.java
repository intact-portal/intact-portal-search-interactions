package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.RequestMethod;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupPage;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.parameters.InteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.utils.NestedCriteria;
import uk.ac.ebi.intact.search.interactions.utils.SearchInteractionUtility;

import java.util.List;

import static uk.ac.ebi.intact.search.interactions.model.SearchChildInteractorFields.DOCUMENT_ID;
import static uk.ac.ebi.intact.search.interactions.model.SearchChildInteractorFields.INTERACTION_COUNT;
import static uk.ac.ebi.intact.search.interactions.model.SearchInteraction.INTERACTIONS;

/**
 * Created by anjali on 13/02/20.
 */
public class CustomizedChildInteractorRepositoryImpl implements CustomizedChildInteractorRepository {

    private final SolrOperations solrOperations;
    private final SearchInteractionUtility searchInteractionUtility = new SearchInteractionUtility();

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
            // We sort by the interaction count for each interactor, which counts the total number of interactions
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

    private void addFiltersToQueryToFindInteractors(SimpleQuery search, InteractionSearchParameters parameters) {
        // filters
        List<FilterQuery> interactionFilterQueries = searchInteractionUtility.createFilterQuery(parameters);

        // search criteria
        Criteria interactionSearchCriteria = searchInteractionUtility.createSearchConditions(parameters);
        Criteria interactorCriteria = new NestedCriteria(interactionSearchCriteria, interactionFilterQueries);
        search.addCriteria(interactorCriteria);
    }
}