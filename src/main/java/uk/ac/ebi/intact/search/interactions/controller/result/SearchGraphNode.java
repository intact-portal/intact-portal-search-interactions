package uk.ac.ebi.intact.search.interactions.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by anjali on 17/05/19.
 */
public class SearchGraphNode {

    @JsonProperty("id")
    private String id;

    @JsonProperty("species")
    private String speciesName;

    @JsonProperty("tax_id")
    private int taxId;

    @JsonProperty("color")
    private String color;

    @JsonProperty("label")
    private String interactorId;

    @JsonProperty("interactor_type")
    private String interactorType;

    @JsonProperty("interactor_name")
    private String interactorName;

    @JsonProperty("preferred_id")
    private String preferredId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getInteractorId() {
        return interactorId;
    }

    public void setInteractorId(String interactorId) {
        this.interactorId = interactorId;
    }

    public String getInteractorType() {
        return interactorType;
    }

    public void setInteractorType(String interactorType) {
        this.interactorType = interactorType;
    }

    public String getInteractorName() {
        return interactorName;
    }

    public void setInteractorName(String interactorName) {
        this.interactorName = interactorName;
    }

    public String getPreferredId() {
        return preferredId;
    }

    public void setPreferredId(String preferredId) {
        this.preferredId = preferredId;
    }
}
