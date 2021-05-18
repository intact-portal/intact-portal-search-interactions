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
    /* IntAct Identifier */
    private String interactorAc;

    @Field(DOCUMENT_TYPE)
    private String documentType;

    @Field(INTERACTOR_NAME)
    /* Prefer name of the interactor */
    private String interactorName;

    @Field(INTERACTOR_INTACT_NAME)
    /* Intact name/shortlabel of the interactor */
    private String interactorIntactName;

    @Field(INTERACTOR_PREFERRED_ID)
    /* Prefer identifier of the interactor */
    private String interactorPreferredIdentifier;

    @Field(INTERACTOR_DESCRIPTION)
    private String interactorDescription;

    @Field(INTERACTOR_ALIAS)
    private Set<String> interactorAlias;

    @Field(INTERACTOR_ALT_IDS)
    private Set<String> interactorAltIds;

    @Field(INTERACTOR_TYPE)
    private String interactorType;

    @Field(INTERACTOR_TYPE_MI_IDENTIFIER)
    private String interactorTypeMIIdentifier;

    @Field(INTERACTOR_SPECIES_NAME)
    private String interactorSpecies;

    @Field(INTERACTOR_TAX_ID)
    private Integer interactorTaxId;

    @Field(INTERACTOR_XREFS)
    private Set<String> interactorXrefs;

    @Field(INTERACTION_COUNT)
    private Integer interactionCount;

    @Field(INTERACTOR_FEATURE_SHORTLABELS)
    private Set<String> interactorFeatureShortLabels;

    /* Fields related with the styling of the network and other visual components */
    @Field(INTERACTOR_TAX_ID_STYLED)
    private String interactorTaxIdStyled;

    @Field(INTERACTOR_TYPE_MI_IDENTIFIER_STYLED)
    private String interactorTypeMIIdentifierStyled;

    /**
     * This field is not part of the solr doc.
     * it is being added after a second call to interactions search service
     * to know in how many interactions the interactor appear
     **/
    @Transient
    private Long interactionSearchCount;

    public SearchChildInteractor() {
    }

    public SearchChildInteractor(String interactorAc, String interactorName, String interactorPreferredIdentifier, String interactorDescription, Set<String> interactorAlias,
                                 Set<String> interactorAltIds, String interactorType, String interactorSpecies, Integer interactorTaxId,
                                 Set<String> interactorXrefs, Integer interactionCount, Long interactionSearchCount,
                                 Set<String> interactorFeatureShortLabels, String interactorIntactName, String interactorTypeMIIdentifier, String documentType) {
        this.interactorAc = interactorAc;
        this.interactorName = interactorName;
        this.interactorPreferredIdentifier = interactorPreferredIdentifier;
        this.interactorDescription = interactorDescription;
        this.interactorAlias = interactorAlias;
        this.interactorAltIds = interactorAltIds;
        this.interactorType = interactorType;
        this.interactorSpecies = interactorSpecies;
        this.interactorTaxId = interactorTaxId;
        this.interactorXrefs = interactorXrefs;
        this.interactionCount = interactionCount;
        this.interactionSearchCount = interactionSearchCount;
        this.interactorFeatureShortLabels = interactorFeatureShortLabels;
        this.interactorIntactName = interactorIntactName;
        this.interactorTypeMIIdentifier = interactorTypeMIIdentifier;
        this.documentType = documentType;
    }

    public String getInteractorTypeMIIdentifier() {
        return interactorTypeMIIdentifier;
    }

    public void setInteractorTypeMIIdentifier(String interactorTypeMIIdentifier) {
        this.interactorTypeMIIdentifier = interactorTypeMIIdentifier;
    }

    public String getInteractorIntactName() {
        return interactorIntactName;
    }

    public void setInteractorIntactName(String interactorIntactName) {
        this.interactorIntactName = interactorIntactName;
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

    public String getInteractorDescription() {
        return interactorDescription;
    }

    public void setInteractorDescription(String interactorDescription) {
        this.interactorDescription = interactorDescription;
    }

    public String getInteractorName() {
        return interactorName;
    }

    public void setInteractorName(String interactorName) {
        this.interactorName = interactorName;
    }

    public String getInteractorPreferredIdentifier() {
        return interactorPreferredIdentifier;
    }

    public void setInteractorPreferredIdentifier(String interactorPreferredIdentifier) {
        this.interactorPreferredIdentifier = interactorPreferredIdentifier;
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

    public String getInteractorTaxIdStyled() {
        return interactorTaxIdStyled;
    }

    public void setInteractorTaxIdStyled(String interactorTaxIdStyled) {
        this.interactorTaxIdStyled = interactorTaxIdStyled;
    }

    public String getInteractorTypeMIIdentifierStyled() {
        return interactorTypeMIIdentifierStyled;
    }

    public void setInteractorTypeMIIdentifierStyled(String interactorTypeMIIdentifierStyled) {
        this.interactorTypeMIIdentifierStyled = interactorTypeMIIdentifierStyled;
    }

    @Override
    public String toString() {
        return "SearchChildInteractor{" +
                "interactorAc='" + interactorAc + '\'' +
                ", documentType='" + documentType + '\'' +
                ", interactorName='" + interactorName + '\'' +
                ", interactorIntactName='" + interactorIntactName + '\'' +
                ", interactorPreferredIdentifier='" + interactorPreferredIdentifier + '\'' +
                ", interactorDescription='" + interactorDescription + '\'' +
                ", interactorAlias=" + interactorAlias +
                ", interactorAltIds=" + interactorAltIds +
                ", interactorType='" + interactorType + '\'' +
                ", interactorTypeMIIdentifier='" + interactorTypeMIIdentifier + '\'' +
                ", interactorSpecies='" + interactorSpecies + '\'' +
                ", interactorTaxId=" + interactorTaxId +
                ", interactorXrefs=" + interactorXrefs +
                ", interactionCount=" + interactionCount +
                ", interactorFeatureShortLabels=" + interactorFeatureShortLabels +
                ", interactionSearchCount=" + interactionSearchCount +
                '}';
    }
}