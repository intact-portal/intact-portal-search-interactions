package uk.ac.ebi.intact.search.interactions.utils;

import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

/**
 * Created by anjali on 17/02/20.
 */
public class SearchInteractionUtility {

    //Custom version of ClientUtils.escapeQueryChars from solrJ
    public static String escapeQueryChars(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
                    || c == '^' || c == '[' || c == ']' || c == '{' || c == '}' || c == '~'
                    || c == '?' || c == '|' || c == '&' || c == ';' || c == '/'
                    || Character.isWhitespace(c)) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static String lowerCaseWord(String s) {
        return s.toLowerCase();
    }

    public Criteria createSearchConditions(String searchTerms, boolean batchSearch) {
        Criteria conditions;
        Criteria userConditions = null;
        Criteria documentConditions = new Criteria(DOCUMENT_TYPE).is(DocumentType.INTERACTION);
        List<String> words = new ArrayList<>();

        //We prepare the term to split by several characters

        searchTerms = searchTerms.trim();
        if (searchTerms != null && !searchTerms.isEmpty()) {
            if (searchTerms.startsWith("\"") && searchTerms.endsWith("\"")) {
                words.add(searchTerms);
            } else {
                words = Arrays.asList(searchTerms.split("[\\s,\\n]"));
            }

            if (batchSearch) {
                userConditions = batchSearchConditions(words);
            } else {
                //TODO Review query formation
                if (!searchTerms.trim().equals("*")) {
                    for (String word : words) {
                        word = word.trim();
                        if (!word.isEmpty()) {
                            if (word.equals("AND") || word.equals("OR") || word.equals("NOT")) {// solr treats these Capital words as logical words
                                word = lowerCaseWord(word);
                            }
                            if (userConditions == null) {
                                if (isEBIAc(word)) {
                                    userConditions = new Criteria(AC_A_STR).is(word)
                                            .or(AC_B_STR).is(word)
                                            .or(AC_STR).is(word);
                                } else {
                                    word = escapeQueryChars(word);
                                    userConditions = new Criteria(DEFAULT).expression(word);
                                }
                            } else {
                                if (isEBIAc(word)) {
                                    Criteria criteria = new Criteria(AC_A_STR).is(word)
                                            .or(AC_B_STR).is(word)
                                            .or(AC_STR).is(word);
                                    userConditions.or(criteria);
                                } else {
                                    word = escapeQueryChars(word);
                                    userConditions = userConditions.or(DEFAULT).expression(word);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (userConditions != null) {
            conditions = documentConditions.and(userConditions);
        } else {
            conditions = documentConditions;
        }
        return conditions;
    }

    public Criteria batchSearchConditions(List<String> batchSearchTerms) {
        Criteria userConditions = null;

        //TODO Review query formation
        if (batchSearchTerms != null && !batchSearchTerms.isEmpty()) {
            userConditions = new Criteria(INTERACTOR_IDENTIFIERS).in(batchSearchTerms);
        }

        return userConditions;
    }

    public List<FilterQuery> createFilterQuery(Set<String> interactorSpeciesFilter,
                                               Set<String> interactorTypesFilter,
                                               Set<String> interactionDetectionMethodsFilter,
                                               Set<String> interactionTypesFilter,
                                               Set<String> interactionHostOrganismsFilter,
                                               boolean negativeFilter,
                                               boolean mutationFilter,
                                               double minMIScore,
                                               double maxMIScore,
                                               boolean intraSpeciesFilter,
                                               Set<Integer> binaryInteractionIdFilter,
                                               Set<String> interactorAcFilter) {

        List<FilterQuery> filterQueries = new ArrayList<FilterQuery>();

        //Interactor species filter
        createInteractorSpeciesFilterCriteria("{!tag=SPECIES}", interactorSpeciesFilter, intraSpeciesFilter, filterQueries);

        //Interactor type filter
        createInteractorTypeFilterCriteria("{!tag=TYPE}", interactorTypesFilter, filterQueries);

        //Interaction detection method filter
        createFilterCriteriaForStringValues("{!tag=DETECTION_METHOD}", interactionDetectionMethodsFilter, DETECTION_METHOD_STR, filterQueries);

        //Interaction type filter
        createFilterCriteriaForStringValues("{!tag=INTERACTION_TYPE}", interactionTypesFilter, TYPE_STR, filterQueries);

        //Interaction host organism filter
        createFilterCriteriaForStringValues("{!tag=HOST_ORGANISM}", interactionHostOrganismsFilter, HOST_ORGANISM_STR, filterQueries);

        //isNegative filter
        createNegativeFilterCriteria("{!tag=NEGATIVE_INTERACTION}", negativeFilter, filterQueries);

        //isNegative filter
        createMutationFilterCriteria("{!tag=MUTATION}", mutationFilter, filterQueries);

        //miscore filter
        createMiScoreFilterCriteria("{!tag=MI_SCORE}", minMIScore, maxMIScore, filterQueries);

        //binaryInteractionIdFilter filter
        createFilterCriteriaForIntegerValues("{!tag=GRAPH_FILTER}", binaryInteractionIdFilter, BINARY_INTERACTION_ID, filterQueries);

        //interactorAcFilter filter
        createInteractorAcsFilter("{!tag=GRAPH_FILTER}", interactorAcFilter, filterQueries);

        return filterQueries;
    }

    private void createFilterCriteriaForStringValues(String tagForExcludingFacets, Set<String> values, String field, List<FilterQuery> filterQueries) {

        if (values != null && !values.isEmpty()) {
            Criteria conditions = new Criteria(tagForExcludingFacets + field).in(values);
            conditions.isOr();
            filterQueries.add(new SimpleFilterQuery(conditions));

        }
    }

    private void createFilterCriteriaForIntegerValues(String tagForExcludingFacets, Set<Integer> values, String field, List<FilterQuery> filterQueries) {

        if (values != null && !values.isEmpty()) {
            Criteria conditions = new Criteria(tagForExcludingFacets + field).in(values);
            conditions.isOr();
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    private void createInteractorAcsFilter(String tagForExcludingFacets, Set<String> values, List<FilterQuery> filterQueries) {
        if (values != null && !values.isEmpty()) {
            Criteria conditions = new Criteria(tagForExcludingFacets + AC_A_STR).in(values)
                    .or(AC_B_STR).in(values);
            filterQueries.add(new SimpleFilterQuery(conditions));

        }
    }

    private void createNegativeFilterCriteria(String tagForExcludingFacets, boolean value, List<FilterQuery> filterQueries) {

        if (value) {
            Criteria conditions = new Criteria(tagForExcludingFacets + NEGATIVE).is(value);
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    private void createMutationFilterCriteria(String tagForExcludingFacets, boolean value, List<FilterQuery> filterQueries) {

        if (value) {
            Criteria conditions = new Criteria(tagForExcludingFacets + DISRUPTED_BY_MUTATION).is(value);
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    private void createMiScoreFilterCriteria(String tagForExcludingFacets, double minScore, double maxScore, List<FilterQuery> filterQueries) {

        Criteria conditions = new Criteria(tagForExcludingFacets + INTACT_MISCORE).between(minScore, maxScore);
        filterQueries.add(new SimpleFilterQuery(conditions));
    }

    // intraSpeciesFilter: if false it creates 'and' condition between two species
    //               if true it creates 'or' condition between set of species
    // Adds tags in solr to allow calculate properly the facets for multiselection in species and interactor type
    private void createInteractorSpeciesFilterCriteria(String tagForExcludingFacets, Set<String> species, boolean intraSpeciesFilter, List<FilterQuery> filterQueries) {

        if (species != null && !species.isEmpty()) {
            Criteria conditions;
            if (!intraSpeciesFilter) {
                conditions = new Criteria(tagForExcludingFacets + SPECIES_A_B_STR).in(species);
                conditions.isOr();
            } else { // Return only interactions from exactly the same species in both interactors.
                conditions = new Criteria(tagForExcludingFacets + SPECIES_A_STR).in(species).and(new Criteria(SPECIES_B_STR).in(species));
            }
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    // Adds tags in solr to allow calculate properly the facets for multiselection in species and interactor type
    private void createInteractorTypeFilterCriteria(String tagForExcludingFacets, Set<String> interactorTypesFilter, List<FilterQuery> filterQueries) {

        if (interactorTypesFilter != null && !interactorTypesFilter.isEmpty()) {
            Criteria conditions = null;

            conditions = new Criteria(tagForExcludingFacets + TYPE_A_B_STR).in(interactorTypesFilter);
            conditions.isOr();

            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    private boolean isEBIAc(String term) {
        String pattern = "EBI-" + "[0-9]+";
        Pattern r1 = Pattern.compile(pattern);

        Matcher m = r1.matcher(term);
        boolean matched = m.matches();

        if (matched) {
            return true;
        }
        return false;
    }
}
