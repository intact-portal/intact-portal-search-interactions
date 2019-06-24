package uk.ac.ebi.intact.search.interactions.controller.result;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by anjali on 17/05/19.
 */
public class SearchGraphJson {

    @JsonProperty("links")
    private List<SearchGraphLink> interactions;

    @JsonProperty("nodes")
    private List<SearchGraphNode> interactors;


    public List<SearchGraphLink> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<SearchGraphLink> interactions) {
        this.interactions = interactions;
    }

    public List<SearchGraphNode> getInteractors() {
        return interactors;
    }

    public void setInteractors(List<SearchGraphNode> interactors) {
        this.interactors = interactors;
    }
}
