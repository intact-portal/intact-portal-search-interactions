package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.RequestMethod;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.utils.SearchInteractionUtility;

import javax.annotation.Resource;
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
    @Resource
    private boolean isEmbeddedSolr;

    // default sorting for the query results
    //TODO Solve problems with multivalue fields that are not allow to be sorted. Schema-less create all the fields as multivalues
//    private static final Sort DEFAULT_QUERY_SORT_WITH_QUERY = new Sort(Sort.Direction.DESC, SearchInteractorFields.INTERACTION_COUNT);

    @Autowired
    public CustomizedInteractionRepositoryImpl(SolrOperations solrOperations) {
        this.solrOperations = solrOperations;
    }

    @Override
    public FacetPage<SearchInteraction> findInteractionFacets(String query,
                                                                 boolean batchSearch,
                                                                 Set<String> interactorSpeciesFilter,
                                                                 Set<String> interactorTypesFilter,
                                                                 Set<String> interactionDetectionMethodsFilter,
                                                                 Set<String> interactionTypesFilter,
                                                                 Set<String> interactionHostOrganismsFilter,
                                                                 boolean negativeFilter,
                                                                 boolean mutationFilter,
                                                                 boolean expansionFilter,
                                                                 double minMIScore,
                                                                 double maxMIScore,
                                                                 boolean intraSpeciesFilter,
                                                                 Set<Long> binaryInteractionIds,
                                                                 Set<String> interactorAcs) {
        return findInteractionWithFacet(query,
                batchSearch,
                interactorSpeciesFilter,
                interactorTypesFilter,
                interactionDetectionMethodsFilter,
                interactionTypesFilter,
                interactionHostOrganismsFilter,
                negativeFilter,
                mutationFilter,
                expansionFilter,
                minMIScore,
                maxMIScore,
                intraSpeciesFilter,
                binaryInteractionIds,
                interactorAcs,
                Sort.unsorted(),
                // We need at least one page with one interaction to avoid problems until we find a way to retrieve the
                // facets without results.
                PageRequest.of(0,1));
    }

    @Override
    public FacetPage<SearchInteraction> findInteractionWithFacet(String query,
                                                                 boolean batchSearch,
                                                                 Set<String> interactorSpeciesFilter,
                                                                 Set<String> interactorTypesFilter,
                                                                 Set<String> interactionDetectionMethodsFilter,
                                                                 Set<String> interactionTypesFilter,
                                                                 Set<String> interactionHostOrganismsFilter,
                                                                 boolean negativeFilter,
                                                                 boolean mutationFilter,
                                                                 boolean expansionFilter,
                                                                 double minMIScore,
                                                                 double maxMIScore,
                                                                 boolean intraSpeciesFilter,
                                                                 Set<Long> binaryInteractionIds,
                                                                 Set<String> interactorAcs,
                                                                 Sort sort, Pageable pageable) {

        // search query
        SimpleFacetQuery search = new SimpleFacetQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter, binaryInteractionIds, interactorAcs);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        // facet
        // Adds exclude tags in solr to allow calculate properly the facets for multiselection in species and interactor type
        FacetOptions facetOptions = new FacetOptions(
                "{!ex=SPECIES,INTRA_SPECIES,GRAPH_FILTER}" + TAX_ID_A_B_STYLED,
                "{!ex=SPECIES,INTRA_SPECIES,GRAPH_FILTER}" + INTRA_TAX_ID_STYLED,
                "{!ex=TYPE,GRAPH_FILTER}" + TYPE_MI_A_B_STYLED,
                "{!ex=DETECTION_METHOD,GRAPH_FILTER}" + DETECTION_METHOD_S,
                "{!ex=INTERACTION_TYPE,GRAPH_FILTER}" + TYPE_MI_IDENTIFIER_STYLED,
                "{!ex=HOST_ORGANISM,GRAPH_FILTER}" + HOST_ORGANISM_TAXID_STYLED,
                "{!ex=NEGATIVE_INTERACTION,GRAPH_FILTER}" + NEGATIVE,
                "{!ex=MUTATION,GRAPH_FILTER}" + AFFECTED_BY_MUTATION,
                "{!ex=MI_SCORE,GRAPH_FILTER}" + INTACT_MISCORE);

        facetOptions.addFacetQuery(new SimpleFacetQuery(new Criteria("{!ex=EXPANSION,GRAPH_FILTER key=expansion_true} -" + EXPANSION_METHOD_S).is("spoke expansion")));
        facetOptions.addFacetQuery(new SimpleFacetQuery(new Criteria("{!ex=EXPANSION,GRAPH_FILTER key=expansion_false}" + EXPANSION_METHOD_S).is("spoke expansion")));

        facetOptions.setFacetLimit(FACET_MIN_COUNT);

        facetOptions.getFieldsWithParameters().add(new FacetOptions.FieldWithFacetParameters(TAX_ID_A_B_STYLED).setMethod("enum"));
        facetOptions.getFieldsWithParameters().add(new FacetOptions.FieldWithFacetParameters(TYPE_MI_A_B_STYLED).setMethod("enum"));

        /*facetOptions.setFacetSort(FacetOptions.FacetSort.COUNT);*/
        search.setFacetOptions(facetOptions);

        // pagination
        search.setPageRequest(pageable);

        // sorting
        if (sort != null) {
            search.addSort(sort);
        } else {
            //order is important to give clustering effect
            search.addSort(Sort.by(Sort.Direction.DESC, INTACT_MISCORE));
            search.addSort(Sort.by(Sort.Direction.ASC, MOLECULE_A));
            search.addSort(Sort.by(Sort.Direction.ASC, MOLECULE_B));
            search.addSort(Sort.by(Sort.Direction.ASC, PUBLICATION_PUBMED_IDENTIFIER));
            search.addSort(Sort.by(Sort.Direction.ASC, DETECTION_METHOD));
        }

        // isEmbeddedSolr parameter is needed for the batch search tests to work
        // check https://issues.apache.org/jira/browse/SOLR-12858 for embedded POST request issue
        return solrOperations.queryForFacetPage(INTERACTIONS, search, SearchInteraction.class,
                ((batchSearch && !isEmbeddedSolr) ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public Page<SearchInteraction> findInteractions(String query,
                                                                 boolean batchSearch,
                                                                 Set<String> interactorSpeciesFilter,
                                                                 Set<String> interactorTypesFilter,
                                                                 Set<String> interactionDetectionMethodsFilter,
                                                                 Set<String> interactionTypesFilter,
                                                                 Set<String> interactionHostOrganismsFilter,
                                                                 boolean negativeFilter,
                                                                 boolean mutationFilter,
                                                                 boolean expansionFilter,
                                                                 double minMIScore,
                                                                 double maxMIScore,
                                                                 boolean intraSpeciesFilter,
                                                                 Set<Long> binaryInteractionIds,
                                                                 Set<String> interactorAcs,
                                                                 Sort sort, Pageable pageable) {


        SimpleQuery search = (SimpleQuery) createQuery(
                query,
                batchSearch,
                interactorSpeciesFilter,
                interactorTypesFilter,
                interactionDetectionMethodsFilter,
                interactionTypesFilter,
                interactionHostOrganismsFilter,
                negativeFilter,
                mutationFilter,
                expansionFilter,
                minMIScore,
                maxMIScore,
                intraSpeciesFilter,
                binaryInteractionIds,
                interactorAcs,
                sort,
                pageable);

        // isEmbeddedSolr parameter is needed for the batch search tests to work
        // check https://issues.apache.org/jira/browse/SOLR-12858 for embedded POST request issue
        return solrOperations.queryForPage(INTERACTIONS, search, SearchInteraction.class,
                ((batchSearch && !isEmbeddedSolr) ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public Page<SearchInteraction> findInteractionForGraphJson(String query,
                                                               boolean batchSearch,
                                                               Set<String> interactorSpeciesFilter,
                                                               Set<String> interactorTypesFilter,
                                                               Set<String> interactionDetectionMethodsFilter,
                                                               Set<String> interactionTypesFilter,
                                                               Set<String> interactionHostOrganismsFilter,
                                                               boolean negativeFilter,
                                                               boolean mutationFilter,
                                                               boolean expansionFilter,
                                                               double minMIScore,
                                                               double maxMIScore,
                                                               boolean intraSpeciesFilter,
                                                               Sort sort, Pageable pageable) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter, null, null);

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
        search.addProjectionOnField(new SimpleField(AFFECTED_BY_MUTATION));

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

        return solrOperations.queryForPage(INTERACTIONS, search, SearchInteraction.class,
                (batchSearch ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public FacetPage<SearchInteraction> findInteractionForGraphJsonWithFacet(String query,
                                                                             boolean batchSearch,
                                                                             Set<String> interactorSpeciesFilter,
                                                                             Set<String> interactorTypesFilter,
                                                                             Set<String> interactionDetectionMethodsFilter,
                                                                             Set<String> interactionTypesFilter,
                                                                             Set<String> interactionHostOrganismsFilter,
                                                                             boolean negativeFilter,
                                                                             boolean mutationFilter,
                                                                             boolean expansionFilter,
                                                                             double minMIScore,
                                                                             double maxMIScore,
                                                                             boolean intraSpeciesFilter,
                                                                             Sort sort, Pageable pageable) {

        // search query
        SimpleFacetQuery search = new SimpleFacetQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter, null, null);

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

        // facet
        // Adds exclude tags in solr to allow calculate properly the facets for multiselection in species and interactor type
        FacetOptions facetOptions = new FacetOptions(
                "{!ex=SPECIES,GRAPH_FILTER}" + SPECIES_A_B_STR, //TODO replace by taxids of A and B
                "{!ex=TYPE,GRAPH_FILTER}" + TYPE_A_B_STR, //TODO replace by MI ID
                "{!ex=INTERACTION_TYPE,GRAPH_FILTER}" + TYPE_S); //TODO replace by MI ID
        facetOptions.setFacetLimit(FACET_MIN_COUNT);

        facetOptions.getFieldsWithParameters().add(new FacetOptions.FieldWithFacetParameters(SPECIES_A_B_STR).setMethod("enum"));
        facetOptions.getFieldsWithParameters().add(new FacetOptions.FieldWithFacetParameters(TYPE_A_B_STR).setMethod("enum"));

//        facetOptions.setFacetSort(FacetOptions.FacetSort.COUNT);
        search.setFacetOptions(facetOptions);

        //projection

        //interaction details
        search.addProjectionOnField(new SimpleField(AC));
        search.addProjectionOnField(new SimpleField(BINARY_INTERACTION_ID));
        search.addProjectionOnField(new SimpleField(TYPE));
        search.addProjectionOnField(new SimpleField(DETECTION_METHOD));
        search.addProjectionOnField(new SimpleField(EXPANSION_METHOD));
        search.addProjectionOnField(new SimpleField(INTACT_MISCORE));
        search.addProjectionOnField(new SimpleField(TYPE_MI_IDENTIFIER));
        search.addProjectionOnField(new SimpleField(AFFECTED_BY_MUTATION));

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

        return solrOperations.queryForFacetPage(INTERACTIONS, search, SearchInteraction.class,
                (batchSearch ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public long countInteractionsForGraphJson(String query, boolean batchSearch,
                                              Set<String> interactorSpeciesFilter,
                                              Set<String> interactorTypesFilter,
                                              Set<String> interactionDetectionMethodsFilter,
                                              Set<String> interactionTypesFilter,
                                              Set<String> interactionHostOrganismsFilter,
                                              boolean negativeFilter,
                                              boolean mutationFilter,
                                              boolean expansionFilter,
                                              double minMiScore,
                                              double maxMiScore,
                                              boolean intraSpeciesFilter) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMiScore, maxMiScore, intraSpeciesFilter, null, null);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        return solrOperations.count(INTERACTIONS, SimpleQuery.fromQuery(search),
                (batchSearch ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public Page<SearchInteraction> findInteractionIdentifiers(String query,
                                                              boolean batchSearch,
                                                              Set<String> interactorSpeciesFilters,
                                                              Set<String> interactorTypesFilter,
                                                              Set<String> interactionDetectionMethodsFilter,
                                                              Set<String> interactionTypesFilter,
                                                              Set<String> interactionHostOrganismsFilter,
                                                              boolean negativeFilter,
                                                              boolean mutationFilter,
                                                              boolean expansionFilter,
                                                              double minMIScore,
                                                              double maxMIScore,
                                                              boolean intraSpeciesFilter,
                                                              Set<Long> binaryInteractionIds,
                                                              Set<String> interactorAcs,
                                                              Sort sort,
                                                              Pageable pageable) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilters, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter, binaryInteractionIds, interactorAcs);

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

        return solrOperations.queryForPage(INTERACTIONS, search, SearchInteraction.class,
                (batchSearch ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public long countInteractionResult(String query,
                                       boolean batchSearch,
                                       String interactorAc,
                                       Set<String> interactorSpeciesFilter,
                                       Set<String> interactorTypesFilter,
                                       Set<String> interactionDetectionMethodsFilter,
                                       Set<String> interactionTypesFilter,
                                       Set<String> interactionHostOrganismsFilter,
                                       boolean negativeFilter,
                                       boolean mutationFilter,
                                       boolean expansionFilter,
                                       double minMiScore,
                                       double maxMiScore,
                                       boolean intraSpeciesFilter,
                                       Set<Long> binaryInteractionIds,
                                       Set<String> interactorAcs) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);

        // search query
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> sQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMiScore, maxMiScore, intraSpeciesFilter, binaryInteractionIds,
                interactorAcs);

        if (!sQueries.isEmpty()) {
            for (FilterQuery sQuery : sQueries) {
                search.addFilterQuery(sQuery);
            }

            FilterQuery fq = new SimpleFilterQuery();

            Criteria cond1 = Criteria.where(AC_A_S).is(interactorAc);
            Criteria cond2 = Criteria.where(AC_B_S).is(interactorAc);
            Criteria c = cond1.or(cond2);

            fq.addCriteria(c);
            search.addFilterQuery(fq);
        }
        return solrOperations.count(INTERACTIONS, SimpleQuery.fromQuery(search),
                (batchSearch ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public long countInteractionResult(String query,
                                       boolean batchSearch,
                                       Set<String> interactorSpeciesFilter,
                                       Set<String> interactorTypesFilter,
                                       Set<String> interactionDetectionMethodsFilter,
                                       Set<String> interactionTypesFilter,
                                       Set<String> interactionHostOrganismsFilter,
                                       boolean negativeFilter,
                                       boolean mutationFilter,
                                       boolean expansionFilter,
                                       double minMIScore,
                                       double maxMIScore,
                                       boolean intraSpeciesFilter,
                                       Set<Long> binaryInteractionIds,
                                       Set<String> interactorAcs) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);

        // search query
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter, binaryInteractionIds,
                interactorAcs);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        return solrOperations.count(INTERACTIONS, SimpleQuery.fromQuery(search),
                (batchSearch ? RequestMethod.POST : RequestMethod.GET));
    }

    private Query createQuery(String query,
                              boolean batchSearch,
                              Set<String> interactorSpeciesFilter,
                              Set<String> interactorTypesFilter,
                              Set<String> interactionDetectionMethodsFilter,
                              Set<String> interactionTypesFilter,
                              Set<String> interactionHostOrganismsFilter,
                              boolean negativeFilter,
                              boolean mutationFilter,
                              boolean expansionFilter,
                              double minMIScore,
                              double maxMIScore,
                              boolean intraSpeciesFilter,
                              Set<Long> binaryInteractionIds,
                              Set<String> interactorAcs,
                              Sort sort,
                              Pageable pageable) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(query, batchSearch);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(interactorSpeciesFilter, interactorTypesFilter, interactionDetectionMethodsFilter,
                interactionTypesFilter, interactionHostOrganismsFilter, negativeFilter, mutationFilter, expansionFilter, minMIScore, maxMIScore, intraSpeciesFilter, binaryInteractionIds, interactorAcs);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        // pagination
        search.setPageRequest(pageable);

        if (sort != null) {
            search.addSort(sort);
        } else {
            //order is important to give clustering effect
            search.addSort(Sort.by(Sort.Direction.DESC, INTACT_MISCORE));
            search.addSort(Sort.by(Sort.Direction.ASC, MOLECULE_A));
            search.addSort(Sort.by(Sort.Direction.ASC, MOLECULE_B));
            search.addSort(Sort.by(Sort.Direction.ASC, PUBLICATION_PUBMED_IDENTIFIER));
            search.addSort(Sort.by(Sort.Direction.ASC, DETECTION_METHOD));
        }

        return search;
    }
}