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
    private Set<String> aliasA;

    @Field(ALIAS_B)
    private Set<String> aliasB;

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

    public Set<String> getAliasA() {
        return aliasA;
    }

    public void setAliasA(Set<String> aliasA) {
        this.aliasA = aliasA;
    }

    public Set<String> getAliasB() {
        return aliasB;
    }

    public void setAliasB(Set<String> aliasB) {
        this.aliasB = aliasB;
    }
}
