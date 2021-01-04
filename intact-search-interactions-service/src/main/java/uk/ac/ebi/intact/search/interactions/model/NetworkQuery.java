package uk.ac.ebi.intact.search.interactions.model;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

public class NetworkQuery implements Serializable {
    @NotEmpty(message = "Query must not be empty")
    private String query;
    private boolean batchSearch;
    private Set<String> interactorSpeciesFilter;
    private Set<String> interactorTypeFilter;
    private  Set<String> interactionDetectionMethodFilter;
    private Set<String> interactionTypeFilter;
    private Set<String> interactionHostOrganismFilter;
    private boolean isNegativeFilter;
    private double minMiScore;
    private double maxMiScore;
    private boolean interSpecies;
    private boolean isCompound;
    private int page;
    private int pageSize;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isBatchSearch() {
        return batchSearch;
    }

    public void setBatchSearch(boolean batchSearch) {
        this.batchSearch = batchSearch;
    }

    public Set<String> getInteractorSpeciesFilter() {
        return interactorSpeciesFilter;
    }

    public void setInteractorSpeciesFilter(Set<String> interactorSpeciesFilter) {
        this.interactorSpeciesFilter = interactorSpeciesFilter;
    }

    public Set<String> getInteractorTypeFilter() {
        return interactorTypeFilter;
    }

    public void setInteractorTypeFilter(Set<String> interactorTypeFilter) {
        this.interactorTypeFilter = interactorTypeFilter;
    }

    public Set<String> getInteractionDetectionMethodFilter() {
        return interactionDetectionMethodFilter;
    }

    public void setInteractionDetectionMethodFilter(Set<String> interactionDetectionMethodFilter) {
        this.interactionDetectionMethodFilter = interactionDetectionMethodFilter;
    }

    public Set<String> getInteractionTypeFilter() {
        return interactionTypeFilter;
    }

    public void setInteractionTypeFilter(Set<String> interactionTypeFilter) {
        this.interactionTypeFilter = interactionTypeFilter;
    }

    public Set<String> getInteractionHostOrganismFilter() {
        return interactionHostOrganismFilter;
    }

    public void setInteractionHostOrganismFilter(Set<String> interactionHostOrganismFilter) {
        this.interactionHostOrganismFilter = interactionHostOrganismFilter;
    }

    public boolean isNegativeFilter() {
        return isNegativeFilter;
    }

    public void setNegativeFilter(boolean negativeFilter) {
        isNegativeFilter = negativeFilter;
    }

    public double getMinMiScore() {
        return minMiScore;
    }

    public void setMinMiScore(double minMiScore) {
        this.minMiScore = minMiScore;
    }

    public double getMaxMiScore() {
        return maxMiScore;
    }

    public void setMaxMiScore(double maxMiScore) {
        this.maxMiScore = maxMiScore;
    }

    public boolean isInterSpecies() {
        return interSpecies;
    }

    public void setInterSpecies(boolean interSpecies) {
        this.interSpecies = interSpecies;
    }

    public boolean isCompound() {
        return isCompound;
    }

    public void setCompound(boolean compound) {
        isCompound = compound;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
