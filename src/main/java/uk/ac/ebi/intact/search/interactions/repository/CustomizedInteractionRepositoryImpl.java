package uk.ac.ebi.intact.search.interactions.repository;

import org.apache.solr.common.params.FacetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.*;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.Interaction;
import uk.ac.ebi.intact.search.interactions.model.InteractionFields;
import uk.ac.ebi.intact.search.interactions.model.InteractionResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Elisabet Barrera
 */

@Repository
public class CustomizedInteractionRepositoryImpl implements CustomizedInteractionRepository {

    private SolrOperations solrOperations;

    // default minimum counts for faceting
    private static final int FACET_MIN_COUNT = 10000;

    // default sorting for the query results
    //TODO Solve problems with multivalue fields that are not allow to be sorted. Schema-less create all the fields as multivalues
//    private static final Sort DEFAULT_QUERY_SORT_WITH_QUERY = new Sort(Sort.Direction.DESC, SearchInteractorFields.INTERACTION_COUNT);

    @Autowired
    public CustomizedInteractionRepositoryImpl(SolrOperations solrOperations) {
        this.solrOperations = solrOperations;
    }

    @Override
    public InteractionResult findInteractionWithFacet(String query, Set<String> detectionMethodFilter, Set<String> interactionTypeFilter,Set<String> hostOrganismFilter,boolean isNegativeFilter,double minMiScore,double maxMiScore, Sort sort, Pageable pageable) {

        // search query
        SimpleFacetQuery search = new SimpleFacetQuery();

        // search criterias
        Criteria conditions = createSearchConditions(query);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = createFilterQuery(detectionMethodFilter, interactionTypeFilter,hostOrganismFilter,isNegativeFilter,minMiScore,maxMiScore);
        if (filterQueries != null && !filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }

        }

        // facet
        FacetOptions facetOptions = new FacetOptions(InteractionFields.INTERACTION_DETECTION_METHOD_STR, InteractionFields.INTERACTION_TYPE_STR,InteractionFields.HOST_ORGANISM_STR,InteractionFields.INTERACTION_NEGATIVE,InteractionFields.INTACT_MISCORE);
        facetOptions.setFacetLimit(FACET_MIN_COUNT);
        facetOptions.addFacetByRange(
                new FacetOptions.FieldWithNumericRangeParameters(InteractionFields.INTACT_MISCORE, 0.0, 1.0, .01)
                        .setHardEnd(true)
                        .setInclude(FacetParams.FacetRangeInclude.ALL)

        );
        search.setFacetOptions(facetOptions);

        // pagination
        search.setPageRequest(pageable);

        // sorting
        if (sort != null) {
            search.addSort(sort);
        } else {
//            search.addSort(DEFAULT_QUERY_SORT_WITH_QUERY);
        }

        return  new InteractionResult(solrOperations.queryForFacetPage(Interaction.INTERACTIONS, search, Interaction.class));
    }

    private Criteria createSearchConditions(String searchTerms) {
        Criteria conditions = null;

        //Query
        //TODO Review query formation
        if (searchTerms != null && !searchTerms.isEmpty()) {
            String[] words = searchTerms.split(" ");

            for (String word : words) {
                if (conditions == null) {
                    conditions = new Criteria(InteractionFields.DEFAULT).contains(word)
                    .or(InteractionFields.INTERACTION_AC_STR).is(word);
                } else {
                    conditions = conditions.or(InteractionFields.DEFAULT).contains(word)
                    .or(InteractionFields.INTERACTION_AC_STR).is(word);
                }
            }
        } else {
            //Default Criteria
            conditions = new Criteria(Criteria.WILDCARD).expression(Criteria.WILDCARD);
        }

        return conditions;
    }

    private List<FilterQuery> createFilterQuery(Set<String> detectionMethodFilter, Set<String> interactionTypeFilter,Set<String> hostOrganismFilter,boolean isNegativeFilter,double minMiScore,double maxMiScore) {
        List<FilterQuery> filterQueries = new ArrayList<FilterQuery>();

        //Interaction Detection Method filter
        createFilterCriteria(detectionMethodFilter, InteractionFields.INTERACTION_DETECTION_METHOD_STR, filterQueries);

        //Interaction Type filter
        createFilterCriteria(interactionTypeFilter, InteractionFields.INTERACTION_TYPE_STR, filterQueries);

        //Host Organism filter
        createFilterCriteria(hostOrganismFilter, InteractionFields.HOST_ORGANISM_STR, filterQueries);

        //isNegative filter
        createFilterCriteria(isNegativeFilter, InteractionFields.INTERACTION_NEGATIVE, filterQueries);

        //miscore filter
        createFilterCriteria(minMiScore,maxMiScore, InteractionFields.INTACT_MISCORE, filterQueries);

        return filterQueries;
    }

    private void createFilterCriteria(Set<String> values, String field, List<FilterQuery> filterQueries) {

        if (values != null) {
            Criteria conditions = null;

            for (String value : values) {
                if (conditions == null) {
                    conditions = new Criteria(field).is(value);
                } else {
                    conditions = conditions.and(new Criteria(field).is(value));
                }
            }

            if (conditions != null) {
                filterQueries.add(new SimpleFilterQuery(conditions));
            }
        }
    }

    private void createFilterCriteria(boolean value, String field, List<FilterQuery> filterQueries) {

           Criteria conditions = null;
           conditions = new Criteria(field).is(value);

        if (conditions != null) {
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    private void createFilterCriteria(double minScore,double maxScore, String field, List<FilterQuery> filterQueries) {

        Criteria conditions = null;
        conditions = new Criteria(field).between(minScore,maxScore);

        if (conditions != null) {
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }
}
