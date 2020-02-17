package uk.ac.ebi.intact.search.interactions.utils;

import org.springframework.data.solr.core.DefaultQueryParser;
import org.springframework.data.solr.core.query.*;

import java.util.List;

/**
 * Created by anjali on 14/02/20.
 */
public class NestedCriteria extends Criteria implements QueryStringHolder {
    private static DefaultQueryParser parser = new DefaultQueryParser(null);

    private Criteria parentCriteria;
    private Criteria childrenCriteria;
    List<FilterQuery> filterQueries;

    public NestedCriteria(Criteria parentCriteria, Criteria childrenCriteria, List<FilterQuery> filterQueries) {
        this.parentCriteria = parentCriteria;
        this.childrenCriteria = childrenCriteria;
        this.filterQueries = filterQueries;
    }

    @Override
    public String getQueryString() {
        String parentQ = parser.createQueryStringFromNode(parentCriteria);
        String childrenQ = parser.createQueryStringFromNode(childrenCriteria);
        String filterQ="";

        if (!filterQueries.isEmpty()) {
            int counter=1;
            for (FilterQuery filterQuery : filterQueries) {
                if(counter==1){
                    filterQ="("+parser.createQueryStringFromNode((filterQuery.getCriteria()))+")";
                }else {
                    filterQ = filterQ + " AND (" + parser.createQueryStringFromNode((filterQuery.getCriteria())) + ")";
                }
                counter++;
            }

        }

        return "{!child of=" + parentQ + "} " + childrenQ + " fq="+filterQ;
    }
}
