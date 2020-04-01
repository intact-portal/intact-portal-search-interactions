package uk.ac.ebi.intact.search.interactions.utils;

import org.springframework.data.solr.core.DefaultQueryParser;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.QueryStringHolder;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields;

import java.util.List;
import java.util.Objects;

/**
 * Created by anjali on 14/02/20.
 */
public class NestedCriteria extends Criteria implements QueryStringHolder {
    private static DefaultQueryParser parser = new DefaultQueryParser(null);
    List<FilterQuery> interactionFilterQueries;
    private Criteria interactionSearchCriteria;

    public NestedCriteria(Criteria interactionSearchCriteria, List<FilterQuery> interactionFilterQueries) {
        this.interactionSearchCriteria = interactionSearchCriteria;
        this.interactionFilterQueries = interactionFilterQueries;
    }

    @Override
    public String getQueryString() {
        String interactionSearchQ = parser.createQueryStringFromNode(interactionSearchCriteria, SearchInteraction.class);
        StringBuilder interactionFilterQ = new StringBuilder();

        if (!interactionFilterQueries.isEmpty()) {
            int counter = 1;
            for (FilterQuery interactionFilterQuery : interactionFilterQueries) {
                String parsedQuery = parser.createQueryStringFromNode(Objects.requireNonNull(interactionFilterQuery.getCriteria()), SearchInteraction.class);
                // Removes the tags that are needed for the general query.
                // This is a HACK, but because we use spring-data-solr it doesn't recognize the filters=$parentfq (needed for the tag to work)
                // of the query and try to encode special characters like = instead of recognise them as part of the filter. The query that works without the need of the hack is like:
                // http://localhost:8983/solr/interactions/select?q={!child%20of=document_type:interaction%20filters=$parentfq}document_type:interaction+AND+
                // (default:kinase+OR+acA_str:kinase+OR+acB_str:kinase+OR+ac_str:kinase)&parentfq={!tag=SPECIES}speciesA_B_str:%22Mus%20musculus%22
                // &parentfq=negative:false&parentfq=intact_miscore:[0.0+TO+1.0]&start=0&rows=10
                // &group=true&group.main=true&group.format=grouped&group.field=id&group.limit=1&group.ngroups=false&group.facet=false&group.truncate=false

                parsedQuery = parsedQuery.replaceAll("^\\{.*\\}", "");
                if (counter == 1) {
                    interactionFilterQ = new StringBuilder(" +" + parsedQuery);
                } else {
                    interactionFilterQ.append(" +").append(parsedQuery);
                }
                counter++;
            }
        }
        return "{!child of=" + SearchInteractionFields.DOCUMENT_TYPE + ":" + DocumentType.INTERACTION + "}"
                + interactionSearchQ + interactionFilterQ.toString();
    }
}
