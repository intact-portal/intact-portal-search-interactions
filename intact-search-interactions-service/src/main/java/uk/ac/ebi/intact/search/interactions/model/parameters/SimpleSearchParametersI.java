package uk.ac.ebi.intact.search.interactions.model.parameters;

public interface SimpleSearchParametersI {
    String getQuery();
    boolean isBatchSearch();
    boolean isAdvancedSearch();
}
