package uk.ac.ebi.intact.search.interactions.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.RequestMethod;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Repository;
import uk.ac.ebi.intact.search.interactions.model.parameters.SimpleInteractionQueryParameters;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.parameters.InteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedFormattedInteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.PagedInteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.utils.SearchInteractionUtility;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteraction.INTERACTIONS;
import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

/**
 * @author Elisabet Barrera
 */

@Repository
public class CustomizedInteractionRepositoryImpl implements CustomizedInteractionRepository {

    private static final Set<String> EXCLUDED_FORMAT_FIELDS = Set.of(
            JSON_FORMAT,
            XML_25_FORMAT,
            XML_30_FORMAT,
            TAB_25_FORMAT,
            TAB_26_FORMAT,
            TAB_27_FORMAT);

    private static final String[] SEARCH_INTERACTION_FIELDS = searchInteractionFields();

    // default minimum counts for faceting
    private static final int FACET_MIN_COUNT = 10000;
    private SolrOperations solrOperations;
    private SearchInteractionUtility searchInteractionUtility = new SearchInteractionUtility();
    @Resource
    private boolean isEmbeddedSolr;

    //default sorting for the query results
    //TODO Solve problems with multivalue fields that are not allow to be sorted. Schema-less create all the fields as multivalues
    //private static final Sort DEFAULT_QUERY_SORT_WITH_QUERY = new Sort(Sort.Direction.DESC, SearchInteractorFields.INTERACTION_COUNT);

    @Autowired
    public CustomizedInteractionRepositoryImpl(SolrOperations solrOperations) {
        this.solrOperations = solrOperations;
    }

    @Override
    public FacetPage<SearchInteraction> findInteractionFacets(InteractionSearchParameters parameters) {
        return findInteractionWithFacet(PagedInteractionSearchParameters.copyParameters(parameters).pageSize(1).build());
    }

    @Override
    public Page<Long> findBinaryInteractionIds(SimpleInteractionQueryParameters parameters) {
        // search query
        SimpleFacetQuery search = new SimpleFacetQuery();

        // search criteria
        Criteria conditions = searchInteractionUtility.createSearchConditions(parameters);
        search.addCriteria(conditions);

        // Retrieve only binary interaction id
        search.addProjectionOnFields(BINARY_INTERACTION_ID);

        // pagination
        search.setPageRequest(PageRequest.of(parameters.getPage(), parameters.getPageSize()));

        return solrOperations.queryForFacetPage(INTERACTIONS, search, SearchInteraction.class, RequestMethod.GET)
                .map(SearchInteraction::getBinaryInteractionId);
    }

    @Override
    public FacetPage<SearchInteraction> findInteractionWithFacet(PagedInteractionSearchParameters parameters) {

        // search query
        SimpleFacetQuery search = new SimpleFacetQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(parameters);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(parameters);

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
                "{!ex=DETECTION_METHOD,GRAPH_FILTER}" + DETECTION_METHOD_MI_STYLED,
                "{!ex=INTERACTION_TYPE,GRAPH_FILTER}" + TYPE_MI_IDENTIFIER_STYLED,
                "{!ex=HOST_ORGANISM,GRAPH_FILTER}" + HOST_ORGANISM_TAXID_STYLED,
                "{!ex=NEGATIVE_INTERACTION,GRAPH_FILTER}" + NEGATIVE,
                "{!ex=MUTATION,GRAPH_FILTER}" + AFFECTED_BY_MUTATION_STYLED,
                "{!ex=MI_SCORE,GRAPH_FILTER}" + INTACT_MISCORE);

        facetOptions.addFacetQuery(new SimpleFacetQuery(new Criteria("{!ex=EXPANSION,GRAPH_FILTER key=expansion_true} -" + EXPANSION_METHOD_S).is("spoke expansion")));
        facetOptions.addFacetQuery(new SimpleFacetQuery(new Criteria("{!ex=EXPANSION,GRAPH_FILTER key=expansion_false}" + EXPANSION_METHOD_S).is("spoke expansion")));

        facetOptions.setFacetLimit(FACET_MIN_COUNT);

        facetOptions.getFieldsWithParameters().add(new FacetOptions.FieldWithFacetParameters(TAX_ID_A_B_STYLED).setMethod("enum"));
        facetOptions.getFieldsWithParameters().add(new FacetOptions.FieldWithFacetParameters(TYPE_MI_A_B_STYLED).setMethod("enum"));

        /*facetOptions.setFacetSort(FacetOptions.FacetSort.COUNT);*/
        search.setFacetOptions(facetOptions);

        // pagination
        search.setPageRequest(PageRequest.of(parameters.getPage(), parameters.getPageSize()));

        // fields
        search.addProjectionOnFields(SEARCH_INTERACTION_FIELDS);

        // sorting
        if (parameters.getSort() != null) {
            search.addSort(parameters.standardiseSort());
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
                ((parameters.isBatchSearch() && !isEmbeddedSolr) ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public Page<SearchInteraction> findInteractions(PagedInteractionSearchParameters parameters) {

        SimpleQuery search = (SimpleQuery) createQuery(
                parameters);

        // isEmbeddedSolr parameter is needed for the batch search tests to work
        // check https://issues.apache.org/jira/browse/SOLR-12858 for embedded POST request issue
        return solrOperations.queryForPage(INTERACTIONS, search, SearchInteraction.class,
                ((parameters.isBatchSearch() && !isEmbeddedSolr) ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public Page<SearchInteraction> findInteractionForGraphJson(PagedInteractionSearchParameters parameters) {

        // Important : override the binaryInteractionIds and interactors acs to null
        parameters = parameters.toBuilder()
                .binaryInteractionIds(null)
                .interactorAcs(null)
                .build();

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(parameters);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(parameters);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        // pagination
        search.setPageRequest(PageRequest.of(parameters.getPage(), parameters.getPageSize()));

        // sorting
        if (parameters.getSort() != null) {
            search.addSort(parameters.standardiseSort());
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
                (parameters.isBatchSearch() ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public FacetPage<SearchInteraction> findInteractionForGraphJsonWithFacet(PagedInteractionSearchParameters parameters) {

        // Important : override the binaryInteractionIds and interactors acs to null
        parameters = parameters.toBuilder()
                .binaryInteractionIds(null)
                .interactorAcs(null)
                .build();

        // search query
        SimpleFacetQuery search = new SimpleFacetQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(parameters);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(parameters);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        // pagination
        search.setPageRequest(PageRequest.of(parameters.getPage(), parameters.getPageSize()));

        // sorting
        if (parameters.getSort() != null) {
            search.addSort(parameters.standardiseSort());
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
        search.addProjectionOnField(new SimpleField(NEGATIVE));

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
                (parameters.isBatchSearch() ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public long countInteractionsForGraphJson(InteractionSearchParameters parameters) {

        // Important : override the binaryInteractionIds and interactors acs to null
        parameters = parameters.toBuilder()
                .binaryInteractionIds(null)
                .interactorAcs(null)
                .build();

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(parameters);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(parameters);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        return solrOperations.count(INTERACTIONS, SimpleQuery.fromQuery(search),
                (parameters.isBatchSearch() ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public Page<SearchInteraction> findInteractionIdentifiers(PagedInteractionSearchParameters parameters) {

        // search query
        SimpleQuery search = queryToFindInteractions(parameters);

        return solrOperations.queryForPage(INTERACTIONS, search, SearchInteraction.class,
                (parameters.isBatchSearch() ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public Page<SearchInteraction> findInteractionIdentifiersWithFormat(PagedFormattedInteractionSearchParameters parameters) {

        // search query
        SimpleQuery search = queryToFindInteractions(parameters);

        //interaction format
        switch (parameters.getFormat()) {
            case miJSON:
                search.addProjectionOnField(new SimpleField(JSON_FORMAT));
                break;
            case miXML25:
                search.addProjectionOnField(new SimpleField(XML_25_FORMAT));
                break;
            case miXML30:
                search.addProjectionOnField(new SimpleField(XML_30_FORMAT));
                break;
            case miTab25:
                search.addProjectionOnField(new SimpleField(TAB_25_FORMAT));
                break;
            case miTab26:
                search.addProjectionOnField(new SimpleField(TAB_26_FORMAT));
                break;
            case miTab27:
                search.addProjectionOnField(new SimpleField(TAB_27_FORMAT));
                break;
        }

        return solrOperations.queryForPage(INTERACTIONS, search, SearchInteraction.class,
                (parameters.isBatchSearch() ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public long countInteractionResult(String interactorAc, InteractionSearchParameters parameters) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(parameters);

        // search query
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> sQueries = searchInteractionUtility.createFilterQuery(parameters);

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
                (parameters.isBatchSearch() ? RequestMethod.POST : RequestMethod.GET));
    }

    @Override
    public long countInteractionResult(InteractionSearchParameters parameters) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(parameters);

        // search query
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(parameters);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        return solrOperations.count(INTERACTIONS, SimpleQuery.fromQuery(search),
                (parameters.isBatchSearch() ? RequestMethod.POST : RequestMethod.GET));
    }

    private Query createQuery(PagedInteractionSearchParameters parameters) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(parameters);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(parameters);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        // pagination
        search.setPageRequest(PageRequest.of(parameters.getPage(), parameters.getPageSize()));

        // fields
        search.addProjectionOnFields(SEARCH_INTERACTION_FIELDS);

        // sorting
        if (parameters.getSort() != null) {
            search.addSort(parameters.standardiseSort());
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

    private SimpleQuery queryToFindInteractions(PagedInteractionSearchParameters parameters) {

        // search query
        SimpleQuery search = new SimpleQuery();

        // search criterias
        Criteria conditions = searchInteractionUtility.createSearchConditions(parameters);
        search.addCriteria(conditions);

        // filters
        List<FilterQuery> filterQueries = searchInteractionUtility.createFilterQuery(parameters);

        if (!filterQueries.isEmpty()) {
            for (FilterQuery filterQuery : filterQueries) {
                search.addFilterQuery(filterQuery);
            }
        }

        // pagination
        search.setPageRequest(PageRequest.of(parameters.getPage(), parameters.getPageSize()));

        // sorting
        if (parameters.getSort() != null) {
            search.addSort(parameters.standardiseSort());
        }

        //projection

        //interaction details
        search.addProjectionOnField(new SimpleField(AC));
        search.addProjectionOnField(new SimpleField(BINARY_INTERACTION_ID));

        return search;
    }

    private static String[] searchInteractionFields() {
        // SOLR search does not allow to exclude fields to fetch, and by default it fetches all fields.
        // To not fetch the data from the fields with the interactions serialised in different formats, we get
        // all the SearchInteraction fields, and exclude the format ones from that list.
        // We then use addProjectionOnField to specify which fields to fetch with a SOLR search.

        List<String> fields = new ArrayList<>();
        for (Field field : SearchInteraction.class.getDeclaredFields()) {
            if (field.getAnnotation(org.apache.solr.client.solrj.beans.Field.class) != null) {
                String fieldValue = field.getAnnotation(org.apache.solr.client.solrj.beans.Field.class).value();
                if (!EXCLUDED_FORMAT_FIELDS.contains(fieldValue)) {
                    fields.add(fieldValue);
                }
            }
        }
        return fields.toArray(new String[0]);
    }
}