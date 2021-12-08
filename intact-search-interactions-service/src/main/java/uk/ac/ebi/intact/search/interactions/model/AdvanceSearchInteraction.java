package uk.ac.ebi.intact.search.interactions.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.AdvanceSearchInteractionFields.AS_ID_A;

@SolrDocument(collection = SearchInteraction.INTERACTIONS)
public class AdvanceSearchInteraction {

    @Field(AS_ID_A)
    private Set<String> asIdA;

    public Set<String> getAsIdA() {
        return asIdA;
    }

    public void setAsIdA(Set<String> asIdA) {
        this.asIdA = asIdA;
    }
}
