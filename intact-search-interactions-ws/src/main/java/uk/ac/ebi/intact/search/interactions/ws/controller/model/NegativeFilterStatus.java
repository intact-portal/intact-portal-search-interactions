package uk.ac.ebi.intact.search.interactions.ws.controller.model;

public enum NegativeFilterStatus {
    POSITIVE_ONLY(false),
    POSITIVE_AND_NEGATIVE(null),
    NEGATIVE_ONLY(true);

    public final Boolean booleanValue;

    NegativeFilterStatus(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
}
