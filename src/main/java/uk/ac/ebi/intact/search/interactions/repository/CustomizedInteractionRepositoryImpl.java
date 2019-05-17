package uk.ac.ebi.intact.search.interactions.repository;

import org.apache.solr.common.params.FacetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.*;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteraction.*;
import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

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
     * @param query
     * @param detectionMethodFilter (Optional)
     * @param interactionTypeFilter (Optional)
     * @param hostOrganismFilter    (Optional)
     * @param isNegativeFilter      (Optional)
     * @param minMiScore
     * @param maxMiScore
     * @param species               (Optional)
     * @param interSpecies:         if true it expects two species and returned interactions should be between the given two species
     *                              if false interactions should atleast have one of the given species
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
        FacetOptions facetOptions = new FacetOptions(
                INTERACTION_DETECTION_METHOD_STR,
                INTERACTION_TYPE_STR, HOST_ORGANISM_STR,
                INTERACTION_NEGATIVE, INTACT_MISCORE,
                SPECIES_A_B_STR);
        facetOptions.setFacetLimit(FACET_MIN_COUNT);
        facetOptions.addFacetByRange(
                new FacetOptions.FieldWithNumericRangeParameters(INTACT_MISCORE, 0d, 1d, 0.01d)
                        .setHardEnd(true)
                        .setInclude(FacetParams.FacetRangeInclude.ALL)


        );

        facetOptions.getFieldsWithParameters().add(new FacetOptions.FieldWithFacetParameters(SPECIES_A_B).setMethod("enum"));
        /*facetOptions.setFacetSort(FacetOptions.FacetSort.COUNT);*/
        search.setFacetOptions(facetOptions);

        // pagination
        search.setPageRequest(pageable);

        // sorting
        if (sort != null) {
            search.addSort(sort);
        }
//        else {
//            search.addSort(DEFAULT_QUERY_SORT_WITH_QUERY);
//        }

        return new SearchInteractionResult(solrOperations.queryForFacetPage(INTERACTIONS, search, SearchInteraction.class));
    }

    /**
     * @param query
     * @param detectionMethodFilter (Optional)
     * @param interactionTypeFilter (Optional)
     * @param hostOrganismFilter    (Optional)
     * @param isNegativeFilter      (Optional)
     * @param minMiScore
     * @param maxMiScore
     * @param species               (Optional)
     * @param interSpecies:         if true it expects two species and returned interactions should be between the given two species
     *                              if false interactions should atleast have one of the given species
     * @param sort
     * @param pageable
     * @return
     */
    @Override
    public Page<SearchInteraction> findInteractionForGraphJson(String query, Set<String> detectionMethodFilter,
                                                             Set<String> interactionTypeFilter, Set<String> hostOrganismFilter,
                                                             boolean isNegativeFilter, double minMiScore, double maxMiScore,
                                                             Set<String> species, boolean interSpecies, Sort sort, Pageable pageable) {

        // search query
        SimpleQuery search = new SimpleQuery();

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

        // pagination
        search.setPageRequest(pageable);

        // sorting
        if (sort != null) {
            search.addSort(sort);
        }

        //projection
        search.addProjectionOnField(new SimpleField(INTERACTION_AC));
        search.addProjectionOnField(new SimpleField(INTERACTOR_AC_A));
        search.addProjectionOnField(new SimpleField(INTERACTOR_AC_B));
        search.addProjectionOnField(new SimpleField(SPECIES_A));
        search.addProjectionOnField(new SimpleField(SPECIES_B));
        search.addProjectionOnField(new SimpleField(TAX_IDA));
        search.addProjectionOnField(new SimpleField(TAX_IDB));
        search.addProjectionOnField(new SimpleField(INTERACTOR_IDA));
        search.addProjectionOnField(new SimpleField(INTERACTOR_IDB));

        return solrOperations.queryForPage(INTERACTIONS, search, SearchInteraction.class);
    }

    @Override
    public long countInteractionResult(String query, String interactorAc, Set<String> detectionMethodFilter,
                                       Set<String> interactionTypeFilter, Set<String> hostOrganismFilter,
                                       boolean isNegativeFilter, double minMiScore, double maxMiScore, Set<String> species,
                                       boolean interSpecies) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = createSearchConditions(query);

        // search query
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = createFilterQuery(detectionMethodFilter, interactionTypeFilter,
                hostOrganismFilter, isNegativeFilter, species, interSpecies, minMiScore, maxMiScore);
        if (filterQueries != null && !filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }

            FilterQuery fq = new SimpleFilterQuery();

            Criteria cond1 = Criteria.where("interactor_acA_str").is(interactorAc);
            Criteria cond2 = Criteria.where("interactor_acB_str").is(interactorAc);
            Criteria c = cond1.or(cond2);

            fq.addCriteria(c);
            search.addFilterQuery(fq);
        }
        return solrOperations.count(INTERACTIONS, SimpleQuery.fromQuery(search));
    }

    /**
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
                    conditions = new Criteria(DEFAULT).contains(word)
                            .or("interactor_acA_str").is(word)
                            .or("interactor_acB_str").is(word)
                            .or(INTERACTION_AC_STR).is(word);
                } else {
                    conditions = conditions.or(DEFAULT).contains(word)
                            .or("interactor_acA_str").is(word)
                            .or("interactor_acB_str").is(word)
                            .or(INTERACTION_AC_STR).is(word);
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
     *
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
        createFilterCriteria(detectionMethodFilter, INTERACTION_DETECTION_METHOD_STR, filterQueries);

        //SearchInteraction Type filter
        createFilterCriteria(interactionTypeFilter, INTERACTION_TYPE_STR, filterQueries);

        //Host Organism filter
        createFilterCriteria(hostOrganismFilter, HOST_ORGANISM_STR, filterQueries);

        //isNegative filter
        createNegativeFilterCriteria(isNegativeFilter, filterQueries);

        //miscore filter
        createMiScoreFilterCriteria(minMiScore, maxMiScore, filterQueries);

        //species filter
        createSpeciesFilterCriteria(species, interSpecies, filterQueries);

        return filterQueries;
    }

    /**
     * Creates filter conditions in filterQueries for set of String values passed for a field
     *
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
     *
     * @param value
     * @param filterQueries
     */
    private void createNegativeFilterCriteria(boolean value, List<FilterQuery> filterQueries) {

        Criteria conditions = new Criteria(INTERACTION_NEGATIVE).is(value);

        filterQueries.add(new SimpleFilterQuery(conditions));
    }

    /**
     * Creates filter conditions in filterQueries for a range of value passed for a field
     *
     * @param minScore
     * @param maxScore
     * @param filterQueries
     */
    private void createMiScoreFilterCriteria(double minScore, double maxScore, List<FilterQuery> filterQueries) {

        Criteria conditions = new Criteria(INTACT_MISCORE).between(minScore, maxScore);

        filterQueries.add(new SimpleFilterQuery(conditions));
    }

    /**
     * Creates filter conditions in filterQueries for a set of species passed.
     *
     * @param species
     * @param interSpecies: if true it creates 'and' condition between two species
     *                      if false it creates 'or' condition between set of species
     * @param filterQueries
     */
    private void createSpeciesFilterCriteria(Set<String> species, boolean interSpecies, List<FilterQuery> filterQueries) {

        if (species != null) {
            Criteria conditions = null;
            if (!interSpecies) {
                for (String value : species) {

                    if (conditions == null) {
                        conditions = new Criteria(SPECIES_A_STR).is(value).or(
                                new Criteria(SPECIES_B_STR).is(value));
                    } else {
                        conditions = conditions.or(new Criteria(SPECIES_A_STR).is(value)).or(
                                new Criteria(SPECIES_B_STR).is(value));
                    }
                }
            } else {
                Iterator iterator = species.iterator();
                String speciesA;
                String speciesB;

                speciesA = (iterator.hasNext()) ? (String) iterator.next() : "";
                speciesB = (iterator.hasNext()) ? (String) iterator.next() : "";
                conditions = new Criteria(SPECIES_A_B_STR).is(speciesA).and(
                        new Criteria(SPECIES_A_B_STR).is(speciesB));
            }
            if (conditions != null) {
                filterQueries.add(new SimpleFilterQuery(conditions));
            }

        }
    }
}
