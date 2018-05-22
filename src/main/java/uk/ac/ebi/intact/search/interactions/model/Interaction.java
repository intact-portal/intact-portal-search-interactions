package uk.ac.ebi.intact.search.interactions.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.List;

/**
 * @author Elisabet Barrera
 */
@SolrDocument(solrCoreName = "interactions")
public class Interaction {

    @Id
    @Field("author")
    @Indexed
    private String author;

    @Field("interaction_count")
    private Integer interactionCount;

    @Field("interactions_ids")
    private List<String> interactionIds;

    @Field("publication_id")
    private List<String> publicationId;


    public Interaction() {
    }

    public Interaction(String author, Integer interactionCount, List<String> interactionIds, List<String> publicationId) {
        this.author = author;
        this.interactionCount = interactionCount;
        this.interactionIds = interactionIds;
        this.publicationId = publicationId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getInteractionCount() {
        return interactionCount;
    }

    public void setInteractionCount(Integer interactionCount) {
        this.interactionCount = interactionCount;
    }

    public List<String> getInteractionIds() {
        return interactionIds;
    }

    public void setInteractionIds(List<String> interactionIds) {
        this.interactionIds = interactionIds;
    }

    public List<String> getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(List<String> publicationId) {
        this.publicationId = publicationId;
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "author='" + author + '\'' +
                ", interactionCount=" + interactionCount +
                ", interactionIds=" + interactionIds +
                ", publicationId=" + publicationId +
                '}';
    }
}
