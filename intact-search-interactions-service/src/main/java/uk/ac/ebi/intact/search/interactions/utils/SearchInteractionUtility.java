package uk.ac.ebi.intact.search.interactions.utils;

import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleStringCriteria;
import uk.ac.ebi.intact.search.interactions.model.parameters.InteractionSearchParameters;
import uk.ac.ebi.intact.search.interactions.model.parameters.SimpleSearchParametersI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

/**
 * Created by anjali on 17/02/20.
 */
public class SearchInteractionUtility {

    private static final Pattern MI_ID_PATTERN = Pattern.compile("MI:.*");
    private static final Pattern TAX_ID_PATTERN = Pattern.compile("-?[0-9]+");

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

    public Criteria createSearchConditions(SimpleSearchParametersI parameters) {
        Criteria conditions;
        Criteria userConditions = null;
        Criteria documentConditions = new Criteria(DOCUMENT_TYPE).is(DocumentType.INTERACTION);
        List<String> words = new ArrayList<>();

        String searchTerms = parameters.getQuery();
        boolean batchSearch = parameters.isBatchSearch();
        boolean advancedSearch = parameters.isAdvancedSearch();

        //We prepare the term to split by several characters

        if (searchTerms != null) {
            searchTerms = searchTerms.trim();
            if (!searchTerms.isEmpty()) {
                if (advancedSearch) {
                    userConditions = new SimpleStringCriteria(AdvancedSearchInteractionUtility.getAdvancedSearchQuery(searchTerms));
                } else {
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
                                            userConditions = new Criteria(AC_A_S).is(word)
                                                    .or(AC_B_S).is(word)
                                                    .or(AC_S).is(word);
                                        } else {
                                            word = escapeQueryChars(word);
                                            userConditions = new Criteria(DEFAULT).expression(word);
                                        }
                                    } else {
                                        if (isEBIAc(word)) {
                                            Criteria criteria = new Criteria(AC_A_S).is(word)
                                                    .or(AC_B_S).is(word)
                                                    .or(AC_S).is(word);
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

    public List<FilterQuery> createFilterQuery(InteractionSearchParameters parameters) {

        List<FilterQuery> filterQueries = new ArrayList<>();

        //Interactor species filter
        createInteractorSpeciesFilterCriteria(parameters.getInteractorSpeciesFilter(), parameters.isIntraSpeciesFilter(), filterQueries);

        //Interactor type filter
        createInteractorTypeFilterCriteria(parameters.getInteractorTypesFilter(), filterQueries);

        //Interaction detection method filter
        createInteractionDetectionMethodsFilterCriteria(parameters.getInteractionDetectionMethodsFilter(), filterQueries);

        //Participant detection method filter
        createParticipantDetectionMethodsFilterCriteria(parameters.getParticipantDetectionMethodsFilter(), filterQueries);

        //Interaction type filter
        createInteractionTypeFilterCriteria(parameters.getInteractionTypesFilter(), filterQueries);

        //Interaction host organism filter
        createInteractionHostOrganismsFilterCriteria(parameters.getInteractionHostOrganismsFilter(), filterQueries);

        //Negative filter
        createNegativeInteractionsFilterCriteria(parameters.getNegativeFilter().booleanValue, filterQueries);

        //Mutation filter
        createFilterCriteriaForBoolean("{!tag=MUTATION}", parameters.isMutationFilter(), AFFECTED_BY_MUTATION, filterQueries);

        //Expansion filter
        createExpansionFilterCriteria(parameters.isExpansionFilter(), filterQueries);

        //MIScore filter
        createMIScoreFilterCriteria(parameters.getMinMIScore(), parameters.getMaxMIScore(), filterQueries);

        //binaryInteractionIds filter
        createBinaryInteractionIdsFilterCriteria(parameters.getBinaryInteractionIds(), filterQueries);

        //InteractorAcs filter
        createInteractorAcsFilter(parameters.getInteractorAcs(), filterQueries);

        return filterQueries;
    }

    private Criteria createCriteriaForLongValues(String tagForExcludingFacets, String field, Set<Long> values) {
        Criteria conditions = null;
        if (values != null && !values.isEmpty()) {
            conditions = new Criteria(tagForExcludingFacets + field).in(values);
        }
        return conditions;
    }

    private Criteria createCriteriaForStringValues(String tagForExcludingFacets, String field, Set<String> values) {
        Criteria conditions = null;
        if (values != null && !values.isEmpty()) {
            conditions = new Criteria(tagForExcludingFacets + field).in(values);
        }
        return conditions;
    }

    private void createInteractorAcsFilter(Set<String> values, List<FilterQuery> filterQueries) {
        if (values != null && !values.isEmpty()) {
            String tagForExcludingFacets = "{!tag=GRAPH_FILTER}";
            Criteria conditions = new Criteria(tagForExcludingFacets + AC_A_S).in(values)
                    .or(AC_B_S).in(values);
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    private void createFilterCriteriaForBoolean(String tagForExcludingFacets, Boolean value, String field, List<FilterQuery> filterQueries) {
        if (value != null) {
            Criteria conditions = new Criteria(tagForExcludingFacets + field).is(value);
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    private void createInteractionDetectionMethodsFilterCriteria(Set<String> values, List<FilterQuery> filterQueries) {
        String tagForExcludingFacets = "{!tag=DETECTION_METHOD}";
        createStringLabelsOrMiIdsFilterCriteria(tagForExcludingFacets, values, DETECTION_METHOD_MI_IDENTIFIER_S, DETECTION_METHOD_S, filterQueries);
    }

    private void createParticipantDetectionMethodsFilterCriteria(Set<String> values, List<FilterQuery> filterQueries) {
        String tagForExcludingFacets = "{!tag=PARTICIPANT_DETECTION_METHOD}";
        createStringLabelsOrMiIdsFilterCriteria(tagForExcludingFacets, values, IDENTIFICATION_METHODS_MI_IDS_A_B_S, IDENTIFICATION_METHODS_A_B_S, filterQueries);
    }

    private void createBinaryInteractionIdsFilterCriteria(Set<Long> values, List<FilterQuery> filterQueries) {
        if (values != null && !values.isEmpty()) {
            String tagForExcludingFacets = "{!tag=GRAPH_FILTER}";
            Criteria conditions = createCriteriaForLongValues(tagForExcludingFacets, BINARY_INTERACTION_ID, values);
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    private void createNegativeInteractionsFilterCriteria(Boolean value, List<FilterQuery> filterQueries) {
        if (value != null) {
            String tagForExcludingFacets = "{!tag=NEGATIVE_INTERACTION}";
            createFilterCriteriaForBoolean(tagForExcludingFacets, value, NEGATIVE, filterQueries);
        }
    }

    private void createExpansionFilterCriteria(boolean value, List<FilterQuery> filterQueries) {
        // Expansion filter meaning:
        // true: hides spoke expanded interactions
        // false: keeps every interaction
        if (value) {
            String tagForExcludingFacets = "{!tag=EXPANSION}";
            Criteria conditions = new Criteria(tagForExcludingFacets + "-" + EXPANSION_METHOD).is("spoke expansion");
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }


    private void createMIScoreFilterCriteria(double minScore, double maxScore, List<FilterQuery> filterQueries) {
        String tagForExcludingFacets = "{!tag=MI_SCORE}";
        Criteria conditions = new Criteria(tagForExcludingFacets + INTACT_MISCORE).between(minScore, maxScore);
        filterQueries.add(new SimpleFilterQuery(conditions));
    }

    // intraSpeciesFilter: if false it creates 'and' condition between two species
    //               if true it creates 'or' condition between set of species
    // Adds tags in solr to allow calculate properly the facets for multiselection in species and interactor type
    private void createInteractorSpeciesFilterCriteria(Set<String> species, boolean intraSpeciesFilter, List<FilterQuery> filterQueries) {
        if (intraSpeciesFilter) {
            if (species != null && !species.isEmpty()) {
                // Return only interactions from exactly the same species in both interactors.
                String tagForExcludingFacets = "{!tag=INTRA_SPECIES}";
                createStringLabelsOrTaxIdsFilterCriteria(
                        tagForExcludingFacets, species, INTRA_TAX_ID, INTRA_SPECIES, filterQueries);
            } else {
                Criteria conditions = new Criteria("{!tag=INTRA_SPECIES}" + INTRA_TAX_ID).isNotNull();
                filterQueries.add(new SimpleFilterQuery(conditions));
            }
        } else if (species != null && !species.isEmpty()) {
            String tagForExcludingFacets = "{!tag=SPECIES}";
            createStringLabelsOrTaxIdsFilterCriteria(
                    tagForExcludingFacets, species, TAX_ID_A_B, SPECIES_A_B_STR, filterQueries);
        }
    }

    // Adds tags in solr to allow calculate properly the facets for multiselection in species and interactor type
    private void createInteractorTypeFilterCriteria(Set<String> interactorTypesFilter, List<FilterQuery> filterQueries) {
        String tagForExcludingFacets = "{!tag=TYPE}";
        createStringLabelsOrMiIdsFilterCriteria(tagForExcludingFacets, interactorTypesFilter, TYPE_MI_A_B_STR, TYPE_A_B_STR, filterQueries);
    }

    private void createInteractionTypeFilterCriteria(Set<String> interactionTypesFilter, List<FilterQuery> filterQueries) {
        String tagForExcludingFacets = "{!tag=INTERACTION_TYPE}";
        createStringLabelsOrMiIdsFilterCriteria(tagForExcludingFacets, interactionTypesFilter, TYPE_MI_IDENTIFIER_S, TYPE_S, filterQueries);
    }

    private void createInteractionHostOrganismsFilterCriteria(Set<String> interactionHostOrganismsFilter, List<FilterQuery> filterQueries) {
        if (interactionHostOrganismsFilter != null && !interactionHostOrganismsFilter.isEmpty()) {
            String tagForExcludingFacets = "{!tag=HOST_ORGANISM}";
            createStringLabelsOrTaxIdsFilterCriteria(
                    tagForExcludingFacets, interactionHostOrganismsFilter, HOST_ORGANISM_TAX_ID, HOST_ORGANISM_S, filterQueries);
        }
    }

    private void createStringLabelsOrTaxIdsFilterCriteria(
            String tagForExcludingFacets,
            Set<String> values,
            String taxIdField,
            String labelField,
            List<FilterQuery> filterQueries) {

        if (values != null && !values.isEmpty()) {
            Set<Long> taxIds = filterTaxIdsValues(values);
            Set<String> labels = filterNotTaxIdsValues(values);

            List<Criteria> conditions = new ArrayList<>();
            if (!taxIds.isEmpty()) {
                if (!labels.isEmpty()) {
                    conditions.add(createCriteriaForLongValues(tagForExcludingFacets, taxIdField, taxIds));
                    conditions.add(createCriteriaForStringValues(tagForExcludingFacets, labelField, labels));
                } else {
                    conditions.add(createCriteriaForLongValues(tagForExcludingFacets, taxIdField, taxIds));
                }
            } else if (!labels.isEmpty()) {
                conditions.add(createCriteriaForStringValues(tagForExcludingFacets, labelField, labels));
            }

            conditions.forEach(condition -> filterQueries.add(new SimpleFilterQuery(condition)));
        }
    }

    private void createStringLabelsOrMiIdsFilterCriteria(
            String tagForExcludingFacets,
            Set<String> values,
            String miIdField,
            String labelField,
            List<FilterQuery> filterQueries) {

        if (values != null && !values.isEmpty()) {
            Set<String> miIds = filterMiIdsValues(values);
            Set<String> labels = filterNotMiIdsValues(values);

            List<Criteria> conditions = new ArrayList<>();
            if (!miIds.isEmpty()) {
                conditions.add(createCriteriaForStringValues(tagForExcludingFacets, miIdField, miIds));
            }
            if (!labels.isEmpty()) {
                conditions.add(createCriteriaForStringValues(tagForExcludingFacets, labelField, labels));
            }
            conditions.forEach(condition -> filterQueries.add(new SimpleFilterQuery(condition)));
        }
    }

    private boolean isEBIAc(String term) {
        String pattern = "EBI-" + "[0-9]+";
        Pattern r1 = Pattern.compile(pattern);
        Matcher m = r1.matcher(term);

        return m.matches();
    }

    private Set<String> filterMiIdsValues(Set<String> values) {
        return values.stream()
                .filter(this::isMiId)
                .collect(Collectors.toSet());
    }

    private Set<String> filterNotMiIdsValues(Set<String> values) {
        return values.stream()
                .filter(value -> !isMiId(value))
                .collect(Collectors.toSet());
    }

    private Set<Long> filterTaxIdsValues(Set<String> values) {
        return values.stream()
                .flatMap(this::splitTerm)
                .filter(this::isTaxId)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    private Set<String> filterNotTaxIdsValues(Set<String> values) {
        return values.stream()
                .flatMap(this::splitTerm)
                .filter(value -> !isTaxId(value))
                .collect(Collectors.toSet());
    }

    private boolean isMiId(String term) {
        return MI_ID_PATTERN.matcher(term).matches();
    }

    private boolean isTaxId(String term) {
        return TAX_ID_PATTERN.matcher(term).matches();
    }

    private Stream<String> splitTerm(String term) {
        return Arrays.stream(term.split("__"));
    }
}