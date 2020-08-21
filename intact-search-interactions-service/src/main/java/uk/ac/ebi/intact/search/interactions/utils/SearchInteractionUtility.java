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

    public Criteria createSearchConditions(String searchTerms, boolean batchSearch) {
        Criteria conditions;
        Criteria userConditions = null;
        Criteria documentConditions = new Criteria(DOCUMENT_TYPE).is(DocumentType.INTERACTION);
        List<String> words;

        //We prepare the term to split by several characters

        if (searchTerms != null && !searchTerms.isEmpty()) {
            words = Arrays.asList(searchTerms.split("[\\s,\\n]"));

            if (batchSearch) {
                userConditions = batchSearchConditions(words);
            } else {
                //TODO Review query formation
                if (!searchTerms.trim().equals("*")) {
                    for (String word : words) {
                        if (userConditions == null) {
                            if (isEBIAc(word)) {
                                userConditions = new Criteria(AC_A_STR).is(word)
                                        .or(AC_B_STR).is(word)
                                        .or(AC_STR).is(word);
                            } else {
                                userConditions = new Criteria(DEFAULT).is(word);
                            }
                        } else {
                            if (isEBIAc(word)) {
                                userConditions = userConditions.or(AC_A_STR).is(word)
                                        .or(AC_B_STR).is(word)
                                        .or(AC_STR).is(word);
                            } else {
                                userConditions = userConditions.or(DEFAULT).is(word);
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
            userConditions = new Criteria(INTERACTOR_DEFAULT).in(batchSearchTerms);
        }

        return userConditions;
    }

    public List<FilterQuery> createFilterQuery(Set<String> interactorSpeciesFilter,
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
                    conditions = conditions.or(new Criteria(field).is(value));
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


    // interSpecies: if false it creates 'and' condition between two species
    //               if true it creates 'or' condition between set of species
    // Adds tags in solr to allow calculate properly the facets for multiselection in species and interactor type
    //TODO rename variable creates confusion
    private void createInteractorSpeciesFilterCriteria(Set<String> species, boolean interSpecies, List<FilterQuery> filterQueries) {

        if (species != null && !species.isEmpty()) {
            Criteria conditions;
            if (!interSpecies) {
                conditions = new Criteria("{!tag=SPECIES}" + SPECIES_A_B_STR).in(species);
                conditions.isOr();
            } else { // Return only interactions from exactly the same species in both interactors.
                // If more than one we keep only the first one
                Iterator<String> iterator = species.iterator();
                String intraSpecies = iterator.next();
                conditions = new Criteria(SPECIES_A_STR).is(intraSpecies).and(new Criteria(SPECIES_B_STR).is(intraSpecies));
            }
            filterQueries.add(new SimpleFilterQuery(conditions));
        }
    }

    // Adds tags in solr to allow calculate properly the facets for multiselection in species and interactor type
    private void createInteractorTypeFilterCriteria(Set<String> interactorTypes, List<FilterQuery> filterQueries) {

        if (interactorTypes != null && !interactorTypes.isEmpty()) {
            Criteria conditions = null;

            conditions = new Criteria("{!tag=TYPE}" + TYPE_A_B_STR).in(interactorTypes);
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
