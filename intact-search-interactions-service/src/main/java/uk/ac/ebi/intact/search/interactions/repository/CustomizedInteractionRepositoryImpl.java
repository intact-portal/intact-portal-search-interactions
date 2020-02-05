package uk.ac.ebi.intact.search.interactions.repository;

import org.apache.solr.common.params.FacetParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteraction.INTERACTIONS;
import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

/**
 * @author Elisabet Barrera
 */

@Repository
public class CustomizedInteractionRepositoryImpl implements CustomizedInteractionRepository {

    // default minimum counts for faceting
    private static final int FACET_MIN_COUNT = 10000;
    private SolrOperations solrOperations;

    // default sorting for the query results
    //TODO Solve problems with multivalue fields that are not allow to be sorted. Schema-less create all the fields as multivalues
//    private static final Sort DEFAULT_QUERY_SORT_WITH_QUERY = new Sort(Sort.Direction.DESC, SearchInteractorFields.INTERACTION_COUNT);

    @Autowired
    public CustomizedInteractionRepositoryImpl(SolrOperations solrOperations) {
        this.solrOperations = solrOperations;
    }

    /**
     * @param query                   input used to retrieve the interaction
     * @param interactorSpeciesFilter (Optional) filter interaction by interactor species
     * @param interactorTypeFilter    (Optional) filter interactions by interactor type
     * @param interactionDetectionMethodFilter   (Optional) filter interactions by interaction detection method
     * @param interactionTypeFilter   (Optional) filter interactions by interaction type
     * @param interactionHostOrganismFilter      (Optional) filter interactions by host organism
     * @param isNegativeFilter        (Optional) filter interactions that are negative if true
     * @param minMiScore              minimum value of mi-score for the interaction
     * @param maxMiScore              maximum value of mi-score for the interaction
     * @param interSpecies            boolean to restrict the result ot the same or different interactor species
     * @param sort                    field to define the sort of the results
     * @param pageable                page number and size of the request
     * @return the interaction data matching all the criteria
     */
    @Override
    public FacetPage<SearchInteraction> findInteractionWithFacet(String query,
                                                                 Set<String> interactorSpeciesFilter,
                                                                 Set<String> interactorTypeFilter,
                                                                 Set<String> interactionDetectionMethodFilter,
                                                                 Set<String> interactionTypeFilter,
                                                                 Set<String> interactionHostOrganismFilter,
                                                                 boolean isNegativeFilter,
                                                                 double minMiScore,
                                                                 double maxMiScore,
                                                                 boolean interSpecies, Sort sort, Pageable pageable) {

        // search query
        SimpleFacetQuery search = new SimpleFacetQuery();

        // search criterias
        Criteria conditions = createSearchConditions(query);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = createFilterQuery(interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        // facet
        FacetOptions facetOptions = new FacetOptions(
                DETECTION_METHOD_STR,
                TYPE_STR, HOST_ORGANISM_STR,
                NEGATIVE, INTACT_MISCORE,
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

        return solrOperations.queryForFacetPage(INTERACTIONS, search, SearchInteraction.class);
    }

    /**
     * @param query                    input used to retrieve the interaction
     * @param interactorSpeciesFilter  (Optional) filter interactions by interactor species
     * @param interactorTypeFilter     (Optional) filter interactions by interactor type
     * @param interactionDetectionMethodFilter    (Optional) filter interactions by interaction detection method
     * @param interactionTypeFilter    (Optional) filter interactions by interaction type
     * @param interactionHostOrganismFilter       (Optional) filter interactions by host organism
     * @param isNegativeFilter         (Optional) filter interactions that are negative if true
     * @param minMiScore               minimum value of mi-score for the interaction
     * @param maxMiScore               maximum value of mi-score for the interaction
     * @param interSpecies             boolean to restrict the result ot the same or different interactor species
     * @param sort                     field to define the sort of the results
     * @param pageable                 page number and size of the request
     * @return the interaction page matching all the criteria for graphical representation
     */
    @Override
    public Page<SearchInteraction> findInteractionForGraphJson(String query,
                                                               Set<String> interactorSpeciesFilter,
                                                               Set<String> interactorTypeFilter,
                                                               Set<String> interactionDetectionMethodFilter,
                                                               Set<String> interactionTypeFilter,
                                                               Set<String> interactionHostOrganismFilter,
                                                               boolean isNegativeFilter,
                                                               double minMiScore,
                                                               double maxMiScore,
                                                               boolean interSpecies, Sort sort, Pageable pageable) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = createSearchConditions(query);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = createFilterQuery(interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies);

        if (!filterQueries.isEmpty()) {
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

        //interaction details
        search.addProjectionOnField(new SimpleField(AC));
        search.addProjectionOnField(new SimpleField(BINARY_INTERACTION_ID));
        search.addProjectionOnField(new SimpleField(TYPE));
        search.addProjectionOnField(new SimpleField(DETECTION_METHOD));
        search.addProjectionOnField(new SimpleField(EXPANSION_METHOD));
        search.addProjectionOnField(new SimpleField(INTACT_MISCORE));
        search.addProjectionOnField(new SimpleField(TYPE_MI_IDENTIFIER));
        search.addProjectionOnField(new SimpleField(DISRUPTED_BY_MUTATION));

        //interactor details
        search.addProjectionOnField(new SimpleField(AC_A));
        search.addProjectionOnField(new SimpleField(AC_B));
        search.addProjectionOnField(new SimpleField(SPECIES_A));
        search.addProjectionOnField(new SimpleField(SPECIES_B));
        search.addProjectionOnField(new SimpleField(TAX_IDA));
        search.addProjectionOnField(new SimpleField(TAX_IDB));
        search.addProjectionOnField(new SimpleField(ID_A));
        search.addProjectionOnField(new SimpleField(ID_B));
        search.addProjectionOnField(new SimpleField(TYPE_A));
        search.addProjectionOnField(new SimpleField(TYPE_B));
        search.addProjectionOnField(new SimpleField(TYPE_MI_A));
        search.addProjectionOnField(new SimpleField(TYPE_MI_B));
        search.addProjectionOnField(new SimpleField(MOLECULE_A));
        search.addProjectionOnField(new SimpleField(MOLECULE_B));
        search.addProjectionOnField(new SimpleField(UNIQUE_ID_A));
        search.addProjectionOnField(new SimpleField(UNIQUE_ID_B));
        search.addProjectionOnField(new SimpleField(MUTATION_A));
        search.addProjectionOnField(new SimpleField(MUTATION_B));

        return solrOperations.queryForPage(INTERACTIONS, search, SearchInteraction.class);
    }

    /**
     * @param query                   input used to retrieve the interaction
     * @param interactorAc            interactor accession e.g. EBI-XXXXXX
     * @param interactorSpeciesFilter (Optional) filter interactions by interactor species
     * @param interactorTypeFilter    (Optional) filter interactions by interactor type
     * @param interactionDetectionMethodFilter   (Optional) filter interactions by interaction detection method
     * @param interactionTypeFilter   (Optional) filter interactions by interaction type
     * @param interactionHostOrganismFilter      (Optional) filter interactions by host organism
     * @param isNegativeFilter        (Optional) filter interactions that are negative if true
     * @param minMiScore              minimum value of mi-score for the interaction
     * @param maxMiScore              maximum value of mi-score for the interaction
     * @param interSpecies            boolean to restrict the result ot the same or different interactor species
     * @return the number of interactions matching all the criteria
     */
    @Override
    public long countInteractionResult(String query,
                                       String interactorAc,
                                       Set<String> interactorSpeciesFilter,
                                       Set<String> interactorTypeFilter,
                                       Set<String> interactionDetectionMethodFilter,
                                       Set<String> interactionTypeFilter,
                                       Set<String> interactionHostOrganismFilter,
                                       boolean isNegativeFilter,
                                       double minMiScore,
                                       double maxMiScore,
                                       boolean interSpecies) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = createSearchConditions(query);

        // search query
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = createFilterQuery(interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }

            FilterQuery fq = new SimpleFilterQuery();

            Criteria cond1 = Criteria.where(AC_A_STR).is(interactorAc);
            Criteria cond2 = Criteria.where(AC_B_STR).is(interactorAc);
            Criteria c = cond1.or(cond2);

            fq.addCriteria(c);
            search.addFilterQuery(fq);
        }
        return solrOperations.count(INTERACTIONS, SimpleQuery.fromQuery(search));
    }

    private Criteria createSearchConditions(String searchTerms) {
        Criteria conditions = null;

        //Query
        //TODO Review query formation
        if (searchTerms != null && !searchTerms.isEmpty()) {
            String[] words = searchTerms.split(" ");

            for (String word : words) {
                if (conditions == null) {
                    conditions = new Criteria(DEFAULT).contains(word)
                            .or(AC_A_STR).is(word)
                            .or(AC_B_STR).is(word)
                            .or(AC_STR).is(word);
                } else {
                    conditions = conditions.or(DEFAULT).contains(word)
                            .or(AC_A_STR).is(word)
                            .or(AC_B_STR).is(word)
                            .or(AC_STR).is(word);
                }
            }
        } else {
            //Default Criteria
            conditions = new Criteria(Criteria.WILDCARD).expression(Criteria.WILDCARD);
        }

        return conditions;
    }

    private List<FilterQuery> createFilterQuery(Set<String> interactorSpeciesFilter,
                                                Set<String> interactorTypeFilter,
                                                Set<String> interactionDetectionMethodFilter,
                                                Set<String> interactionTypeFilter,
                                                Set<String> interactionHostOrganismFilter,
                                                boolean isNegativeFilter,
                                                double minMiScore,
                                                double maxMiScore,
                                                boolean interSpecies) {

        List<FilterQuery> filterQueries = new ArrayList<FilterQuery>();

        //Interactor species filter
        createInteractorSpeciesFilterCriteria(interactorSpeciesFilter, interSpecies, filterQueries);

        //Interactor type filter
        createInteractorTypeFilterCriteria(interactorTypeFilter, filterQueries);

        //Interaction detection method filter
        createFilterCriteria(interactionDetectionMethodFilter, DETECTION_METHOD_STR, filterQueries);

        //Interaction type filter
        createFilterCriteria(interactionTypeFilter, TYPE_STR, filterQueries);

        //Interaction host organism filter
        createFilterCriteria(interactionHostOrganismFilter, HOST_ORGANISM_STR, filterQueries);

        //isNegative filter
        createNegativeFilterCriteria(isNegativeFilter, filterQueries);

        //miscore filter
        createMiScoreFilterCriteria(minMiScore, maxMiScore, filterQueries);

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

    private void createNegativeFilterCriteria(boolean value, List<FilterQuery> filterQueries) {

        Criteria conditions = new Criteria(NEGATIVE).is(value);
        filterQueries.add(new SimpleFilterQuery(conditions));
    }

    private void createMiScoreFilterCriteria(double minScore, double maxScore, List<FilterQuery> filterQueries) {

        Criteria conditions = new Criteria(INTACT_MISCORE).between(minScore, maxScore);
        filterQueries.add(new SimpleFilterQuery(conditions));
    }


    // interSpecies: if true it creates 'and' condition between two species
    //               if false it creates 'or' condition between set of species
    private void createInteractorSpeciesFilterCriteria(Set<String> species, boolean interSpecies, List<FilterQuery> filterQueries) {

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
                Iterator<String> iterator = species.iterator();
                String speciesA;
                String speciesB;

                speciesA = (iterator.hasNext()) ? iterator.next() : "";
                speciesB = (iterator.hasNext()) ? iterator.next() : "";
                conditions = new Criteria(SPECIES_A_B_STR).is(speciesA).and(
                        new Criteria(SPECIES_A_B_STR).is(speciesB));
            }
            if (conditions != null) {
                filterQueries.add(new SimpleFilterQuery(conditions));
            }
        }
    }

    private void createInteractorTypeFilterCriteria(Set<String> interactorType, List<FilterQuery> filterQueries) {

        if (interactorType != null) {
            Criteria conditions = null;

            for (String value : interactorType) {

                if (conditions == null) {
                    conditions = new Criteria(TYPE_A_STR).is(value).or(
                            new Criteria(TYPE_B_STR).is(value));
                } else {
                    conditions = conditions.or(new Criteria(TYPE_A_STR).is(value)).or(
                            new Criteria(TYPE_B_STR).is(value));
                }
            }

            if (conditions != null) {
                filterQueries.add(new SimpleFilterQuery(conditions));
            }
        }
    }
}
