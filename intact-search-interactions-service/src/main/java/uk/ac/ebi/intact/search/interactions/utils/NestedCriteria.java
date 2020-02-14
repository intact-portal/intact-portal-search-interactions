package uk.ac.ebi.intact.search.interactions.utils;

import org.springframework.data.solr.core.DefaultQueryParser;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.QueryStringHolder;

/**
 * Created by anjali on 14/02/20.
 */
public class NestedCriteria  extends Criteria implements QueryStringHolder{
    private static DefaultQueryParser parser = new DefaultQueryParser(null);

    private Criteria parentCriteria;
    private Criteria childrenCriteria;

    public NestedCriteria(Criteria parentCriteria, Criteria childrenCriteria) {
        this.parentCriteria = parentCriteria;
        this.childrenCriteria = childrenCriteria;
    }

    @Override
    public String getQueryString() {
        String parentQ = parser.createQueryStringFromNode(parentCriteria);
        String childrenQ = parser.createQueryStringFromNode(childrenCriteria);

        return "{!child of="+parentQ+"} "+childrenQ+"";
    }
}
