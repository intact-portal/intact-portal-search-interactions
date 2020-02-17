package uk.ac.ebi.intact.search.interactions.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.HashSet;
import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.SearchChildInteractorFields.*;

/**
 * @author Elisabet Barrera
 */
@SolrDocument(collection = SearchInteraction.INTERACTIONS)
public class SearchChildInteractor {

    @Id
    @Field(DOCUMENT_ID)
    private String interactorAc;

    @Field(DOCUMENT_TYPE)
    private String documentType;

    @Field(INTERACTOR_NAME)
    private String interactorName;

    @Field(INTERACTOR_DESCRIPTION)
    private String description;

    @Field(INTERACTOR_ALIAS)
    private Set<String> interactorAlias;

    @Field(INTERACTOR_ALT_IDS)
    private Set<String> interactorAltIds;

    @Field(INTERACTOR_TYPE)
    private String interactorType;

    @Field(INTERACTOR_SPECIES_NAME)
    private String interactorSpecies;

    @Field(INTERACTOR_TAX_ID)
    private Integer interactorTaxId;

    @Field(INTERACTOR_XREFS)
    private Set<String> interactorXrefs;

    @Field(INTERACTION_COUNT)
    private Integer interactionCount;

    @Field(INTERACTION_IDS)
    private Set<String> interactionIds;

    @Field(INTERACTION_DETECTION_METHODS)
    private Set<String> interactionDetectionMethods;

    @Field(INTERACTION_TYPES)
    private Set<String> interactionTypes;

    @Field(INTERACTION_EXPANSION_METHODS)
    private Set<String> interactionExpansionMethods;

    @Field(INTERACTION_NEGATIVES)
    private Set<Boolean> interactionNegatives;

    @Field(INTERACTION_MISCORES)
    private Set<Double> interactionMiScores;

    @Field(INTERACTION_HOST_ORGANISMS)
    private Set<String> interactionHostOrganisms;

    @Field(INTERACTOR_FEATURE_SHORTLABELS)
    private Set<String> interactorFeatureShortLabels;

    /**
     * This field is not part of the solr doc.
     * it is being added after a second call to interactions search service
     * to know in how many interactions the interactor appear
     **/
    @Transient
    private Long interactionSearchCount;

    public SearchChildInteractor() {
    }

    public SearchChildInteractor(String interactorAc, String interactorName, String description, Set<String> interactorAlias,
                                 Set<String> interactorAltIds, String interactorType, String species, Integer taxId,
                                 Set<String> interactorXrefs, Integer interactionCount, Long interactionSearchCount,
                                 Set<String> interactionIds, Set<String> interactionDetectionMethods, Set<String> interactionTypes,
                                 Set<String> interactionExpansionMethods, Set<Boolean> interactionNegatives,
                                 Set<Double> interactionMiScores, Set<String> interactionHostOrganisms,
                                 Set<String> interactorFeatureShortLabels, String documentType) {
        this.interactorAc = interactorAc;
        this.documentType = documentType;
        this.interactorName = interactorName;
        this.description = description;
        this.interactorAlias = interactorAlias;
        this.interactorAltIds = interactorAltIds;
        this.interactorType = interactorType;
        this.setInteractorSpecies(species);
        this.setInteractorTaxId(taxId);
        this.interactorXrefs = interactorXrefs;
        this.interactionCount = interactionCount;
        this.interactionSearchCount = interactionSearchCount;
        this.interactionIds = interactionIds;
        this.interactionDetectionMethods = interactionDetectionMethods;
        this.interactionTypes = interactionTypes;
        this.interactionExpansionMethods = interactionExpansionMethods;
        this.interactionNegatives = interactionNegatives;
        this.interactionMiScores = interactionMiScores;
        this.interactionHostOrganisms = interactionHostOrganisms;
        this.setInteractorFeatureShortLabels(interactorFeatureShortLabels);
    }

    public String getInteractorAc() {
        return interactorAc;
    }

    public void setInteractorAc(String interactorAc) {
        this.interactorAc = interactorAc;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInteractorName() {
        return interactorName;
    }

    public void setInteractorName(String interactorName) {
        this.interactorName = interactorName;
    }

    public Set<String> getInteractorAlias() {
        if (this.interactorAlias == null) {
            this.interactorAlias = new HashSet<>();
        }
        return interactorAlias;
    }

    public void setInteractorAlias(Set<String> interactorAlias) {
        this.interactorAlias = interactorAlias;
    }

    public Set<String> getInteractorAltIds() {
        if (this.interactorAltIds == null) {
            this.interactorAltIds = new HashSet<>();
        }
        return interactorAltIds;
    }

    public void setInteractorAltIds(Set<String> interactorAltIds) {
        this.interactorAltIds = interactorAltIds;
    }

    public String getInteractorType() {
        return interactorType;
    }

    public void setInteractorType(String interactorType) {
        this.interactorType = interactorType;
    }

    public Set<String> getInteractorXrefs() {
        if (this.interactorXrefs == null) {
            this.interactorXrefs = new HashSet<>();
        }
        return interactorXrefs;
    }

    public void setInteractorXrefs(Set<String> interactorXrefs) {
        this.interactorXrefs = interactorXrefs;
    }

    public Integer getInteractionCount() {
        return interactionCount;
    }

    public void setInteractionCount(Integer interactionCount) {
        this.interactionCount = interactionCount;
    }

    public Long getInteractionSearchCount() {
        return interactionSearchCount;
    }

    public void setInteractionSearchCount(Long interactionSearchCount) {
        this.interactionSearchCount = interactionSearchCount;
    }

    public Set<String> getInteractionIds() {
        if (this.interactorXrefs == null) {
            this.interactorXrefs = new HashSet<>();
        }
        return interactorXrefs;
    }

    public void setInteractionIds(Set<String> interactionIds) {
        this.interactionIds = interactionIds;
    }

    public Set<String> getInteractionDetectionMethods() {
        return interactionDetectionMethods;
    }

    public void setInteractionDetectionMethods(Set<String> interactionDetectionMethods) {
        this.interactionDetectionMethods = interactionDetectionMethods;
    }

    public Set<String> getInteractionTypes() {
        return interactionTypes;
    }

    public void setInteractionTypes(Set<String> interactionTypes) {
        this.interactionTypes = interactionTypes;
    }

    public Set<String> getInteractionExpansionMethods() {
        return interactionExpansionMethods;
    }

    public void setInteractionExpansionMethods(Set<String> interactionExpansionMethods) {
        this.interactionExpansionMethods = interactionExpansionMethods;
    }

    public Set<Boolean> getInteractionNegatives() {
        return interactionNegatives;
    }

    public void setInteractionNegatives(Set<Boolean> interactionNegatives) {
        this.interactionNegatives = interactionNegatives;
    }

    public Set<Double> getInteractionMiScores() {
        return interactionMiScores;
    }

    public void setInteractionMiScores(Set<Double> interactionMiScores) {
        this.interactionMiScores = interactionMiScores;
    }

    public Set<String> getInteractionHostOrganisms() {
        return interactionHostOrganisms;
    }

    public void setInteractionHostOrganisms(Set<String> interactionHostOrganisms) {
        this.interactionHostOrganisms = interactionHostOrganisms;
    }

    public String getInteractorSpecies() {
        return interactorSpecies;
    }

    public void setInteractorSpecies(String interactorSpecies) {
        this.interactorSpecies = interactorSpecies;
    }

    public Integer getInteractorTaxId() {
        return interactorTaxId;
    }

    public void setInteractorTaxId(Integer interactorTaxId) {
        this.interactorTaxId = interactorTaxId;
    }

    public Set<String> getInteractorFeatureShortLabels() {
        if (this.interactorFeatureShortLabels == null) {
            this.interactorFeatureShortLabels = new HashSet<>();
        }
        return interactorFeatureShortLabels;
    }

    public void setInteractorFeatureShortLabels(Set<String> interactorFeatureShortLabels) {
        this.interactorFeatureShortLabels = interactorFeatureShortLabels;
    }

    @Override
    public String toString() {
        return "SearchChildInteractor{" +
                "interactorAc='" + interactorAc + '\'' +
                ", documentType='" + documentType + '\'' +
                ", interactorName='" + interactorName + '\'' +
                ", description='" + description + '\'' +
                ", interactorAlias=" + interactorAlias +
                ", interactorAltIds=" + interactorAltIds +
                ", interactorType='" + interactorType + '\'' +
                ", interactorSpecies='" + interactorSpecies + '\'' +
                ", interactorTaxId=" + interactorTaxId +
                ", interactorXrefs=" + interactorXrefs +
                ", interactionCount=" + interactionCount +
                ", interactionIds=" + interactionIds +
                ", interactionDetectionMethods=" + interactionDetectionMethods +
                ", interactionTypes=" + interactionTypes +
                ", interactionExpansionMethods=" + interactionExpansionMethods +
                ", interactionNegatives=" + interactionNegatives +
                ", interactionMiScores=" + interactionMiScores +
                ", interactionHostOrganisms=" + interactionHostOrganisms +
                ", interactorFeatureShortLabels=" + interactorFeatureShortLabels +
                ", interactionSearchCount=" + interactionSearchCount +
                '}';
    }
}
