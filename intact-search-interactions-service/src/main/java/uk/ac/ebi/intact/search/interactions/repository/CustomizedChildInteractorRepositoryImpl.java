package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.RequestMethod;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupPage;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractorFields;
import uk.ac.ebi.intact.search.interactions.utils.NestedCriteria;
import uk.ac.ebi.intact.search.interactions.utils.SearchInteractionUtility;

import java.util.List;
import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteraction.INTERACTIONS;

/**
 * Created by anjali on 13/02/20.
 */
public class CustomizedChildInteractorRepositoryImpl implements CustomizedChildInteractorRepository {
    // default minimum counts for faceting
    private static final int FACET_MIN_COUNT = 10000;
    private SolrOperations solrOperations;
    private SearchInteractionUtility searchInteractionUtility = new SearchInteractionUtility();

    // default sorting for the query results
    //TODO Solve problems with multivalue fields that are not allow to be sorted. Schema-less create all the fields as multivalues
//    private static final Sort DEFAULT_QUERY_SORT_WITH_QUERY = new Sort(Sort.Direction.DESC, SearchInteractorFields.INTERACTION_COUNT);

    @Autowired
    public CustomizedChildInteractorRepositoryImpl(SolrOperations solrOperations) {
        this.solrOperations = solrOperations;
    }

    @Override
    public GroupPage<SearchChildInteractor> findChildInteractors(String query,
                                                                 boolean batchSearch,
                                                                 boolean advancedSearch,
                                                                 Set<String> interactorSpeciesFilter,
                                                                 Set<String> interactorTypesFilter,
                                                                 Set<String> interactionDetectionMethodsFilter,
                                                                 Set<String> interactionTypesFilter,
                                                                 Set<String> interactionHostOrganismsFilter,
                                                                 Boolean negativeFilter,
                                                                 boolean mutationFilter,
                                                                 boolean expansionFilter,
                                                                 double minMIScore,
                                                                 double maxMIScore,
                                                                 boolean intraSpeciesFilter,
                                                                 Set<Long> binaryInteractionIds,
                                                                 Set<String> interactorAcs,
                                                                 Sort sort, Pageable pageable) {

        // filters
        List<FilterQuery> interactionFilterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter, binaryInteractionIds, interactorAcs);

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria interactionSearchCriteria = searchInteractionUtility.createSearchConditions(query, batchSearch, advancedSearch);
        Criteria interactorCriteria = new NestedCriteria(interactionSearchCriteria, interactionFilterQueries);

        search.addCriteria(interactorCriteria);

        //group
        GroupOptions groupOptions = new GroupOptions()
                .addGroupByField(SearchChildInteractorFields.DOCUMENT_ID);
        groupOptions.setLimit(1);
        groupOptions.setGroupMain(true);
        search.setGroupOptions(groupOptions);

        // pagination
        search.setPageRequest(pageable);

        // sorting
        if (sort != null) {
            search.addSort(sort);
        }
//        else {
//            search.addSort(DEFAULT_QUERY_SORT_WITH_QUERY);
//        }

        return solrOperations.queryForGroupPage(INTERACTIONS, search, SearchChildInteractor.class,
                (batchSearch ? RequestMethod.POST : RequestMethod.GET));
    }

    // By default the numCount return by solr when group.main=true is the total documents instead of the number of groups.
    // To make the pagination of interactors working we need to ask solr the number of groups for that group.main=false and
    // group.ngroups=true (setTotalCount(true) in spring-data-solr)
    @Override
    public long countChildInteractors(String query,
                                      boolean batchSearch,
                                      boolean advancedSearch,
                                      Set<String> interactorSpeciesFilter,
                                      Set<String> interactorTypeFilter,
                                      Set<String> interactionDetectionMethodFilter,
                                      Set<String> interactionTypeFilter,
                                      Set<String> interactionHostOrganismFilter,
                                      Boolean negativeFilter,
                                      boolean mutationFilter,
                                      boolean expansionFilter,
                                      double minMIScore,
                                      double maxMIScore,
                                      boolean intraSpeciesFilter,
                                      Set<Long> binaryInteractionIds,
                                      Set<String> interactorAcs) {
        // filters
        List<FilterQuery> interactionFilterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter, binaryInteractionIds, interactorAcs);

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria interactionSearchCriteria = searchInteractionUtility.createSearchConditions(query, batchSearch, advancedSearch);
        Criteria interactorCriteria = new NestedCriteria(interactionSearchCriteria, interactionFilterQueries);

        search.addCriteria(interactorCriteria);

        //group
        GroupOptions groupOptions = new GroupOptions()
                .addGroupByField(SearchChildInteractorFields.DOCUMENT_ID);
        groupOptions.setLimit(1);
        groupOptions.setGroupMain(false);
        groupOptions.setTotalCount(true);
        search.setGroupOptions(groupOptions);

        // pagination
        search.setPageRequest(PageRequest.of(0, 1));

        GroupPage<SearchChildInteractor> groupPage = solrOperations.queryForGroupPage(INTERACTIONS, search, SearchChildInteractor.class,
                (batchSearch ? RequestMethod.POST : RequestMethod.GET));
        return groupPage.getGroupResult(SearchChildInteractorFields.DOCUMENT_ID).getGroupsCount();
    }
}