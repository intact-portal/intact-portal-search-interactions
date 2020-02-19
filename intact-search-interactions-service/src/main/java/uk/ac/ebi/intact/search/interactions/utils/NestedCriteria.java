package uk.ac.ebi.intact.search.interactions.utils;

import org.springframework.data.solr.core.DefaultQueryParser;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.QueryStringHolder;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;
import uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields;

import java.util.List;

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
        String interactionFilterQ = "";

        if (!interactionFilterQueries.isEmpty()) {
            int counter = 1;
            for (FilterQuery interactionFilterQuery : interactionFilterQueries) {
                String parsedQuery = parser.createQueryStringFromNode(interactionFilterQuery.getCriteria(), SearchInteraction.class);
                if (counter == 1) {
                    interactionFilterQ = "(" + parsedQuery + ")";
                } else {
                    interactionFilterQ = interactionFilterQ + " AND (" + parsedQuery + ")";
                }
                counter++;
            }
        }
        return "{!child of= " + SearchInteractionFields.DOCUMENT_TYPE + ":" + Constants.INTERACTION_DOCUMENT_TYPE_VALUE + "} "
                + interactionSearchQ + " fq=" + interactionFilterQ;
    }
}
