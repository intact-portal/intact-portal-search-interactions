package uk.ac.ebi.intact.search.interactions.repository;

import org.apache.solr.common.params.FacetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.*;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionResult;

import java.util.ArrayList;
import java.util.Iterator;
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

    /**
     *
     * @param query
     * @param detectionMethodFilter (Optional)
     * @param interactionTypeFilter (Optional)
     * @param hostOrganismFilter (Optional)
     * @param isNegativeFilter (Optional)
     * @param minMiScore
     * @param maxMiScore
     * @param species (Optional)
     * @param interSpecies: if true it expects two species and returned interactions should be between the given two species
     *                      if false interactions should atleast have one of the given species
     * @param sort
     * @param pageable
     * @return
     */
    @Override
    public SearchInteractionResult findInteractionWithFacet(String query, Set<String> detectionMethodFilter,
                                                            Set<String> interactionTypeFilter, Set<String> hostOrganismFilter,
                                                            boolean isNegativeFilter, double minMiScore, double maxMiScore,
                                                            Set<String> species, boolean interSpecies, Sort sort, Pageable pageable) {

        // search query
        SimpleFacetQuery search = new SimpleFacetQuery();

        // search criterias
        Criteria conditions = createSearchConditions(query);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = createFilterQuery(detectionMethodFilter, interactionTypeFilter,
                hostOrganismFilter, isNegativeFilter, species, interSpecies, minMiScore, maxMiScore);
        if (filterQueries != null && !filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }

        }

        // facet
        FacetOptions facetOptions = new FacetOptions(SearchInteractionFields.INTERACTION_DETECTION_METHOD_STR,
                SearchInteractionFields.INTERACTION_TYPE_STR, SearchInteractionFields.HOST_ORGANISM_STR,
                SearchInteractionFields.INTERACTION_NEGATIVE, SearchInteractionFields.INTACT_MISCORE,
                SearchInteractionFields.SPECIES_A_B_STR);
        facetOptions.setFacetLimit(FACET_MIN_COUNT);
        facetOptions.addFacetByRange(
                new FacetOptions.FieldWithNumericRangeParameters(SearchInteractionFields.INTACT_MISCORE, 0d, 1d, 0.01d)
                        .setHardEnd(true)
                        .setInclude(FacetParams.FacetRangeInclude.ALL)


        );

        facetOptions.getFieldsWithParameters().add(new FacetOptions.FieldWithFacetParameters(SearchInteractionFields.SPECIES_A_B).setMethod("enum"));
        /*facetOptions.setFacetSort(FacetOptions.FacetSort.COUNT);*/
        search.setFacetOptions(facetOptions);

        // pagination
        search.setPageRequest(pageable);

        // sorting
        if (sort != null) {
            search.addSort(sort);
        } else {
//            search.addSort(DEFAULT_QUERY_SORT_WITH_QUERY);
        }

        return new SearchInteractionResult(solrOperations.queryForFacetPage(SearchInteraction.INTERACTIONS, search, SearchInteraction.class));
    }

    /**
     *
     * @param searchTerms
     * @return Criteria
     */
    private Criteria createSearchConditions(String searchTerms) {
        Criteria conditions = null;

        //Query
        //TODO Review query formation
        if (searchTerms != null && !searchTerms.isEmpty()) {
            String[] words = searchTerms.split(" ");

            for (String word : words) {
                if (conditions == null) {
                    conditions = new Criteria(SearchInteractionFields.DEFAULT).contains(word)
                            .or(SearchInteractionFields.INTERACTION_AC_STR).is(word);
                } else {
                    conditions = conditions.or(SearchInteractionFields.DEFAULT).contains(word)
                            .or(SearchInteractionFields.INTERACTION_AC_STR).is(word);
                }
            }
        } else {
            //Default Criteria
            conditions = new Criteria(Criteria.WILDCARD).expression(Criteria.WILDCARD);
        }

        return conditions;
    }

    /**
     * Creates filter conditions for all the filters passed in.
     * @param detectionMethodFilter
     * @param interactionTypeFilter
     * @param hostOrganismFilter
     * @param isNegativeFilter
     * @param species
     * @param interSpecies
     * @param minMiScore
     * @param maxMiScore
     * @return
     */
    private List<FilterQuery> createFilterQuery(Set<String> detectionMethodFilter, Set<String> interactionTypeFilter,
                                                Set<String> hostOrganismFilter, boolean isNegativeFilter,
                                                Set<String> species, boolean interSpecies, double minMiScore, double maxMiScore) {
        List<FilterQuery> filterQueries = new ArrayList<FilterQuery>();

        //SearchInteraction Detection Method filter
        createFilterCriteria(detectionMethodFilter, SearchInteractionFields.INTERACTION_DETECTION_METHOD_STR, filterQueries);

        //SearchInteraction Type filter
        createFilterCriteria(interactionTypeFilter, SearchInteractionFields.INTERACTION_TYPE_STR, filterQueries);

        //Host Organism filter
        createFilterCriteria(hostOrganismFilter, SearchInteractionFields.HOST_ORGANISM_STR, filterQueries);

        //isNegative filter
        createFilterCriteria(isNegativeFilter, SearchInteractionFields.INTERACTION_NEGATIVE, filterQueries);

        //miscore filter
        createFilterCriteria(minMiScore, maxMiScore, SearchInteractionFields.INTACT_MISCORE, filterQueries);

        //species filter
        createFilterCriteriaForSpecies(species, interSpecies, filterQueries);

        return filterQueries;
    }

    /**
     * Creates filter conditions in filterQueries for set of String values passed for a field
     * @param values
     * @param field
     * @param filterQueries
     */
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

    /**
     * Creates filter conditions in filterQueries for boolean value passed for a field
     * @param value
     * @param field
     * @param filterQueries
     */
    private void createFilterCriteria(boolean value, String field, List<FilterQuery> filterQueries) {

        Criteria conditions = null;
        conditions = new Criteria(field).is(value);

        if (conditions != null) {
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    /**
     * Creates filter conditions in filterQueries for a range of value passed for a field
     * @param minScore
     * @param maxScore
     * @param field
     * @param filterQueries
     */
    private void createFilterCriteria(double minScore, double maxScore, String field, List<FilterQuery> filterQueries) {

        Criteria conditions = null;
        conditions = new Criteria(field).between(minScore, maxScore);

        if (conditions != null) {
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    /**
     * Creates filter conditions in filterQueries for a set of species passed.
     * @param species
     * @param interSpecies: if true it creates 'and' condition between two species
     *                      if false it creates 'or' condition between set of species
     * @param filterQueries
     */
    private void createFilterCriteriaForSpecies(Set<String> species, boolean interSpecies, List<FilterQuery> filterQueries) {

        if (species != null) {
            Criteria conditions = null;
            if (!interSpecies) {
                for (String value : species) {

                    if (conditions == null) {
                        conditions = new Criteria(SearchInteractionFields.SPECIES_A_STR).is(value).or(new Criteria(SearchInteractionFields.SPECIES_B_STR).is(value));
                    } else {
                        conditions = conditions.or(new Criteria(SearchInteractionFields.SPECIES_A_STR).is(value)).or(new Criteria(SearchInteractionFields.SPECIES_B_STR).is(value));
                    }
                }
            } else {
                Iterator iterator = species.iterator();
                String speciesA;
                String speciesB;

                speciesA = (iterator.hasNext()) ? (String) iterator.next() : "";
                speciesB = (iterator.hasNext()) ? (String) iterator.next() : "";
                conditions = new Criteria(SearchInteractionFields.SPECIES_A_B_STR).is(speciesA).and(new Criteria(SearchInteractionFields.SPECIES_A_B_STR).is(speciesB));
            }
            if (conditions != null) {
                filterQueries.add(new SimpleFilterQuery(conditions));
            }

        }
    }
}
