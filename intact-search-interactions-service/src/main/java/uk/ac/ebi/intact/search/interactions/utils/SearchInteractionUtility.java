package uk.ac.ebi.intact.search.interactions.utils;

import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.SimpleFilterQuery;

import java.util.*;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

/**
 * Created by anjali on 17/02/20.
 */
public class SearchInteractionUtility {

    public Criteria createSearchConditions(String searchTerms, boolean batchSearch) {
        Criteria conditions = null;
        Criteria userConditions = null;
        Criteria documentConditions = new Criteria(DOCUMENT_TYPE).is(Constants.INTERACTION_DOCUMENT_TYPE_VALUE);

        if (batchSearch) {
            userConditions = batchSearchConditions(Arrays.asList(searchTerms.split(",")));
        } else {
            //TODO Review query formation
            if (searchTerms != null && !searchTerms.isEmpty() && !searchTerms.trim().equals("*")) {
                String[] words = searchTerms.split(" ");
                int wordCount = 1;
                for (String word : words) {
                    if (userConditions == null) {
                        userConditions = new Criteria(DEFAULT).contains(word)
                                .or(AC_A_STR).is(word)
                                .or(AC_B_STR).is(word)
                                .or(AC_STR).is(word);
                    } else {
                        userConditions = userConditions.or(DEFAULT).contains(word)
                                .or(AC_A_STR).is(word)
                                .or(AC_B_STR).is(word)
                                .or(AC_STR).is(word);
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
