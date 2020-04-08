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
import uk.ac.ebi.intact.search.interactions.utils.SearchInteractionUtility;

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
    private SearchInteractionUtility searchInteractionUtility = new SearchInteractionUtility();

    // default sorting for the query results
    //TODO Solve problems with multivalue fields that are not allow to be sorted. Schema-less create all the fields as multivalues
//    private static final Sort DEFAULT_QUERY_SORT_WITH_QUERY = new Sort(Sort.Direction.DESC, SearchInteractorFields.INTERACTION_COUNT);

    @Autowired
    public CustomizedInteractionRepositoryImpl(SolrOperations solrOperations) {
        this.solrOperations = solrOperations;
    }

    /**
     * @param query                            input used to retrieve the interaction
     * @param interactorSpeciesFilter          (Optional) filter interaction by interactor species
     * @param interactorTypeFilter             (Optional) filter interactions by interactor type
     * @param interactionDetectionMethodFilter (Optional) filter interactions by interaction detection method
     * @param interactionTypeFilter            (Optional) filter interactions by interaction type
     * @param interactionHostOrganismFilter    (Optional) filter interactions by host organism
     * @param isNegativeFilter                 (Optional) filter interactions that are negative if true
     * @param minMiScore                       minimum value of mi-score for the interaction
     * @param maxMiScore                       maximum value of mi-score for the interaction
     * @param interSpecies                     boolean to restrict the result ot the same or different interactor species
     * @param sort                             field to define the sort of the results
     * @param pageable                         page number and size of the request
     * @return the interaction data matching all the criteria
     */
    @Override
    public FacetPage<SearchInteraction> findInteractionWithFacet(String query,
                                                                 boolean batchSearch,
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
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
                interactionTypeFilter, interactionHostOrganismFilter, isNegativeFilter, minMiScore, maxMiScore, interSpecies);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        // facet
        // Adds exclude tags in solr to allow calculate properly the facets for multiselection in species and interactor type
        FacetOptions facetOptions = new FacetOptions(
                "{!ex=SPECIES}"+SPECIES_A_B_STR,
                "{!ex=TYPE}"+TYPE_A_B_STR,
                DETECTION_METHOD_STR,
                TYPE_STR, HOST_ORGANISM_STR,
                NEGATIVE, INTACT_MISCORE);
        facetOptions.setFacetLimit(FACET_MIN_COUNT);
        facetOptions.addFacetByRange(
                new FacetOptions.FieldWithNumericRangeParameters(INTACT_MISCORE, 0d, 1d, 0.01d)
                        .setHardEnd(true)
                        .setInclude(FacetParams.FacetRangeInclude.ALL)


        );

        facetOptions.getFieldsWithParameters().add(new FacetOptions.FieldWithFacetParameters(SPECIES_A_B_STR).setMethod("enum"));
        facetOptions.getFieldsWithParameters().add(new FacetOptions.FieldWithFacetParameters(TYPE_A_B_STR).setMethod("enum"));

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
     * @param query                            input used to retrieve the interaction
     * @param interactorSpeciesFilter          (Optional) filter interactions by interactor species
     * @param interactorTypeFilter             (Optional) filter interactions by interactor type
     * @param interactionDetectionMethodFilter (Optional) filter interactions by interaction detection method
     * @param interactionTypeFilter            (Optional) filter interactions by interaction type
     * @param interactionHostOrganismFilter    (Optional) filter interactions by host organism
     * @param isNegativeFilter                 (Optional) filter interactions that are negative if true
     * @param minMiScore                       minimum value of mi-score for the interaction
     * @param maxMiScore                       maximum value of mi-score for the interaction
     * @param interSpecies                     boolean to restrict the result ot the same or different interactor species
     * @param sort                             field to define the sort of the results
     * @param pageable                         page number and size of the request
     * @return the interaction page matching all the criteria for graphical representation
     */
    @Override
    public Page<SearchInteraction> findInteractionForGraphJson(String query,
                                                               boolean batchSearch,
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
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
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
     * @param query                            input used to retrieve the interaction
     * @param interactorAc                     interactor accession e.g. EBI-XXXXXX
     * @param interactorSpeciesFilter          (Optional) filter interactions by interactor species
     * @param interactorTypeFilter             (Optional) filter interactions by interactor type
     * @param interactionDetectionMethodFilter (Optional) filter interactions by interaction detection method
     * @param interactionTypeFilter            (Optional) filter interactions by interaction type
     * @param interactionHostOrganismFilter    (Optional) filter interactions by host organism
     * @param isNegativeFilter                 (Optional) filter interactions that are negative if true
     * @param minMiScore                       minimum value of mi-score for the interaction
     * @param maxMiScore                       maximum value of mi-score for the interaction
     * @param interSpecies                     boolean to restrict the result ot the same or different interactor species
     * @return the number of interactions matching all the criteria
     */
    @Override
    public long countInteractionResult(String query, boolean batchSearch,
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
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);

        // search query
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypeFilter, interactionDetectionMethodFilter,
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
}
