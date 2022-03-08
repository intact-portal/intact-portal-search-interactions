package uk.ac.ebi.intact.search.interactions.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.AdvancedSearchInteractionFields.AdvancedSearchFieldConstants.*;

@SolrDocument(collection = SearchInteraction.INTERACTIONS)
public class AdvancedSearchInteraction {

    @Field(ID_A)
    private Set<String> asIdA;

    @Field(ID_B)
    private Set<String> asIdB;

    @Field(ALTID_A)
    private Set<String> asAltidA;

    @Field(ALTID_B)
    private Set<String> asAltidB;

    @Field(ALIAS_A)
    private Set<String> asAliasA;

    @Field(ALIAS_B)
    private Set<String> asAliasB;

    @Field(PUB_ID)
    private Set<String> asPubId;

    @Field(INTERACTION_XREFS)
    private Set<String> asInteractionXrefs;

    public Set<String> getAsIdA() {
        return asIdA;
    }

    public void setAsIdA(Set<String> asIdA) {
        this.asIdA = asIdA;
    }

    public Set<String> getAsIdB() {
        return asIdB;
    }

    public void setAsIdB(Set<String> asIdB) {
        this.asIdB = asIdB;
    }

    public Set<String> getAsAltidA() {
        return asAltidA;
    }

    public void setAsAltidA(Set<String> asAltidA) {
        this.asAltidA = asAltidA;
    }

    public Set<String> getAsAltidB() {
        return asAltidB;
    }

    public void setAsAltidB(Set<String> asAltidB) {
        this.asAltidB = asAltidB;
    }

    public Set<String> getAsAliasA() {
        return asAliasA;
    }

    public void setAsAliasA(Set<String> asAliasA) {
        this.asAliasA = asAliasA;
    }

    public Set<String> getAsAliasB() {
        return asAliasB;
    }

    public void setAsAliasB(Set<String> asAliasB) {
        this.asAliasB = asAliasB;
    }

    public Set<String> getAsPubId() {
        return asPubId;
    }

    public void setAsPubId(Set<String> asPubId) {
        this.asPubId = asPubId;
    }

    public Set<String> getAsInteractionXrefs() {
        return asInteractionXrefs;
    }

    public void setAsInteractionXrefs(Set<String> asInteractionXrefs) {
        this.asInteractionXrefs = asInteractionXrefs;
    }
}
