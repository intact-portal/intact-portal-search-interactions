package uk.ac.ebi.intact.search.interactions.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.lang.Nullable;

import java.util.Set;

/**
 * @author Elisabet Barrera
 */
@SolrDocument(solrCoreName = "interactions")
public class Interaction {

    @Id
    @Field(InteractionFields.UNIQUE_KEY)
    @Indexed
    private String uniqueKey;

    @Field("author")
    @Nullable
    private String author;

    @Field("interaction_count")
    @Nullable
    private Integer interactionCount;

    @Field("interactions_ids")
    @Nullable
    private Set<String> interactionIds;

    @Field("publication_id")
    @Nullable
    private Set<String> publicationId;

    @Field(InteractionFields.INTERACTOR_IDA)
    @Nullable
    private String idA;

    @Field(InteractionFields.INTERACTOR_IDB)
    @Nullable
    private String idB;

    @Field(InteractionFields.ALT_IDS_A)
    @Nullable
    private Set<String> altIdsA;

    @Field(InteractionFields.ALT_IDS_B)
    @Nullable
    private Set<String> altIdsB;

    @Field(InteractionFields.ALIASES_A)
    @Nullable
    private Set<String> aliasesA;

    @Field(InteractionFields.ALIASES_B)
    @Nullable
    private Set<String> aliasesB;

    @Field(InteractionFields.TAX_IDA)
    @Nullable
    private Integer taxIdA;

    @Field(InteractionFields.TAX_IDB)
    @Nullable
    private Integer taxIdB;

    @Field(InteractionFields.TYPE_A)
    @Nullable
    private String typeA;

    @Field(InteractionFields.TYPE_B)
    @Nullable
    private String typeB;

    @Field(InteractionFields.XREFS_A)
    @Nullable
    private Set<String> xrefsA;

    @Field(InteractionFields.XREFS_B)
    @Nullable
    private Set<String> xrefsB;

    @Field(InteractionFields.ANNOTATIONS_A)
    @Nullable
    private Set<String> annotationsA;

    @Field(InteractionFields.ANNOTATIONS_B)
    @Nullable
    private Set<String> annotationsB;

    @Field(InteractionFields.CHECKSUMS_A)
    @Nullable
    private Set<String> checksumsA;

    @Field(InteractionFields.CHECKSUMS_B)
    @Nullable
    private Set<String> checksumsB;

    @Field(InteractionFields.SPECIES_A)
    @Nullable
    private String speciesA;

    @Field(InteractionFields.SPECIES_B)
    @Nullable
    private String speciesB;

    //participants

    @Field(InteractionFields.BIOLOGICAL_ROLE_A)
    @Nullable
    private String biologicalRoleA;

    @Field(InteractionFields.BIOLOGICAL_ROLE_B)
    @Nullable
    private String biologicalRoleB;

    @Field(InteractionFields.EXPERIMENTAL_ROLE_A)
    @Nullable
    private String experimentalRoleA;

    @Field(InteractionFields.EXPERIMENTAL_ROLE_B)
    @Nullable
    private String experimentalRoleB;

    @Field(InteractionFields.FEATURE_A)
    @Nullable
    private Set<String> featureA;

    @Field(InteractionFields.FEATURE_B)
    @Nullable
    private Set<String> featureB;

    @Field(InteractionFields.STOICHIOMETRY_A)
    @Nullable
    private String stoichiometryA;

    @Field(InteractionFields.STOICHIOMETRY_B)
    @Nullable
    private String stoichiometryB;

    @Field(InteractionFields.IDENTIFICATION_METHOD_A)
    @Nullable
    private Set<String> identificationMethodA;

    @Field(InteractionFields.IDENTIFICATION_METHOD_B)
    @Nullable
    private Set<String> identificationMethodB;



    public Interaction() {
    }

    public Interaction(String author, Integer interactionCount, Set<String> interactionIds, Set<String> publicationId) {
        this.author = author;
        this.uniqueKey=author;// this is for the current tests to run
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

    public Set<String> getInteractionIds() {
        return interactionIds;
    }

    public void setInteractionIds(Set<String> interactionIds) {
        this.interactionIds = interactionIds;
    }

    public Set<String> getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(Set<String> publicationId) {
        this.publicationId = publicationId;
    }


    public String getIdA() {
        return idA;
    }

    public void setIdA(String idA) {
        this.idA = idA;
    }

    public String getIdB() {
        return idB;
    }

    public void setIdB(String idB) {
        this.idB = idB;
    }

    public Set<String> getAltIdsA() {
        return altIdsA;
    }

    public void setAltIdsA(Set<String> altIdsA) {
        this.altIdsA = altIdsA;
    }

    public Set<String> getAltIdsB() {
        return altIdsB;
    }

    public void setAltIdsB(Set<String> altIdsB) {
        this.altIdsB = altIdsB;
    }

    public Set<String> getAliasesA() {
        return aliasesA;
    }

    public void setAliasesA(Set<String> aliasesA) {
        this.aliasesA = aliasesA;
    }

    public Set<String> getAliasesB() {
        return aliasesB;
    }

    public void setAliasesB(Set<String> aliasesB) {
        this.aliasesB = aliasesB;
    }

    public Integer getTaxIdA() {
        return taxIdA;
    }

    public void setTaxIdA(Integer taxIdA) {
        this.taxIdA = taxIdA;
    }

    public Integer getTaxIdB() {
        return taxIdB;
    }

    public void setTaxIdB(Integer taxIdB) {
        this.taxIdB = taxIdB;
    }

    public String getTypeA() {
        return typeA;
    }

    public void setTypeA(String typeA) {
        this.typeA = typeA;
    }

    public String getTypeB() {
        return typeB;
    }

    public void setTypeB(String typeB) {
        this.typeB = typeB;
    }

    public Set<String> getXrefsA() {
        return xrefsA;
    }

    public void setXrefsA(Set<String> xrefsA) {
        this.xrefsA = xrefsA;
    }

    public Set<String> getXrefsB() {
        return xrefsB;
    }

    public void setXrefsB(Set<String> xrefsB) {
        this.xrefsB = xrefsB;
    }

    public Set<String> getAnnotationsA() {
        return annotationsA;
    }

    public void setAnnotationsA(Set<String> annotationsA) {
        this.annotationsA = annotationsA;
    }

    public Set<String> getAnnotationsB() {
        return annotationsB;
    }

    public void setAnnotationsB(Set<String> annotationsB) {
        this.annotationsB = annotationsB;
    }

    public Set<String> getChecksumsA() {
        return checksumsA;
    }

    public void setChecksumsA(Set<String> checksumsA) {
        this.checksumsA = checksumsA;
    }

    public Set<String> getChecksumsB() {
        return checksumsB;
    }

    public void setChecksumsB(Set<String> checksumsB) {
        this.checksumsB = checksumsB;
    }

    public String getSpeciesA() {
        return speciesA;
    }

    public void setSpeciesA(String speciesA) {
        this.speciesA = speciesA;
    }

    public String getSpeciesB() {
        return speciesB;
    }

    public void setSpeciesB(String speciesB) {
        this.speciesB = speciesB;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }


    public String getBiologicalRoleA() {
        return biologicalRoleA;
    }

    public void setBiologicalRoleA(String biologicalRoleA) {
        this.biologicalRoleA = biologicalRoleA;
    }

    public String getBiologicalRoleB() {
        return biologicalRoleB;
    }

    public void setBiologicalRoleB(String biologicalRoleB) {
        this.biologicalRoleB = biologicalRoleB;
    }

    public String getExperimentalRoleA() {
        return experimentalRoleA;
    }

    public void setExperimentalRoleA(String experimentalRoleA) {
        this.experimentalRoleA = experimentalRoleA;
    }

    public String getExperimentalRoleB() {
        return experimentalRoleB;
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "uniqueKey='" + uniqueKey + '\'' +
                ", author='" + author + '\'' +
                ", interactionCount=" + interactionCount +
                ", interactionIds=" + interactionIds +
                ", publicationId=" + publicationId +
                ", idA='" + idA + '\'' +
                ", idB='" + idB + '\'' +
                ", altIdsA=" + altIdsA +
                ", altIdsB=" + altIdsB +
                ", aliasesA=" + aliasesA +
                ", aliasesB=" + aliasesB +
                ", taxIdA=" + taxIdA +
                ", taxIdB=" + taxIdB +
                ", typeA='" + typeA + '\'' +
                ", typeB='" + typeB + '\'' +
                ", xrefsA=" + xrefsA +
                ", xrefsB=" + xrefsB +
                ", annotationsA=" + annotationsA +
                ", annotationsB=" + annotationsB +
                ", checksumsA=" + checksumsA +
                ", checksumsB=" + checksumsB +
                ", speciesA='" + speciesA + '\'' +
                ", speciesB='" + speciesB + '\'' +
                ", biologicalRoleA='" + biologicalRoleA + '\'' +
                ", biologicalRoleB='" + biologicalRoleB + '\'' +
                ", experimentalRoleA='" + experimentalRoleA + '\'' +
                ", experimentalRoleB='" + experimentalRoleB + '\'' +
                ", featureA=" + featureA +
                ", featureB=" + featureB +
                ", stoichiometryA='" + stoichiometryA + '\'' +
                ", stoichiometryB='" + stoichiometryB + '\'' +
                ", identificationMethodA=" + identificationMethodA +
                ", identificationMethodB=" + identificationMethodB +
                '}';
    }

    public Interaction(String uniqueKey, String author, Integer interactionCount, Set<String> interactionIds, Set<String> publicationId, String idA, String idB, Set<String> altIdsA, Set<String> altIdsB, Set<String> aliasesA, Set<String> aliasesB, Integer taxIdA, Integer taxIdB, String typeA, String typeB, Set<String> xrefsA, Set<String> xrefsB, Set<String> annotationsA, Set<String> annotationsB, Set<String> checksumsA, Set<String> checksumsB, String speciesA, String speciesB, String biologicalRoleA, String biologicalRoleB, String experimentalRoleA, String experimentalRoleB, Set<String> featureA, Set<String> featureB, String stoichiometryA, String stoichiometryB, Set<String> identificationMethodA, Set<String> identificationMethodB) {
        this.uniqueKey = uniqueKey;
        this.author = author;
        this.interactionCount = interactionCount;
        this.interactionIds = interactionIds;
        this.publicationId = publicationId;
        this.idA = idA;
        this.idB = idB;
        this.altIdsA = altIdsA;
        this.altIdsB = altIdsB;
        this.aliasesA = aliasesA;
        this.aliasesB = aliasesB;
        this.taxIdA = taxIdA;
        this.taxIdB = taxIdB;
        this.typeA = typeA;
        this.typeB = typeB;
        this.xrefsA = xrefsA;
        this.xrefsB = xrefsB;
        this.annotationsA = annotationsA;
        this.annotationsB = annotationsB;
        this.checksumsA = checksumsA;
        this.checksumsB = checksumsB;
        this.speciesA = speciesA;
        this.speciesB = speciesB;
        this.biologicalRoleA = biologicalRoleA;
        this.biologicalRoleB = biologicalRoleB;
        this.experimentalRoleA = experimentalRoleA;
        this.experimentalRoleB = experimentalRoleB;
        this.featureA = featureA;
        this.featureB = featureB;
        this.stoichiometryA = stoichiometryA;
        this.stoichiometryB = stoichiometryB;
        this.identificationMethodA = identificationMethodA;
        this.identificationMethodB = identificationMethodB;
    }

    public void setExperimentalRoleB(String experimentalRoleB) {
        this.experimentalRoleB = experimentalRoleB;
    }

    public String getStoichiometryA() {
        return stoichiometryA;
    }

    public void setStoichiometryA(String stoichiometryA) {
        this.stoichiometryA = stoichiometryA;
    }

    public String getStoichiometryB() {
        return stoichiometryB;
    }

    public void setStoichiometryB(String stoichiometryB) {
        this.stoichiometryB = stoichiometryB;
    }

    public Set<String> getFeatureA() {
        return featureA;
    }

    public void setFeatureA(Set<String> featureA) {
        this.featureA = featureA;
    }

    public Set<String> getFeatureB() {
        return featureB;
    }

    public void setFeatureB(Set<String> featureB) {
        this.featureB = featureB;
    }

    public Set<String> getIdentificationMethodA() {
        return identificationMethodA;
    }

    public void setIdentificationMethodA(Set<String> identificationMethodA) {
        this.identificationMethodA = identificationMethodA;
    }

    public Set<String> getIdentificationMethodB() {
        return identificationMethodB;
    }

    public void setIdentificationMethodB(Set<String> identificationMethodB) {
        this.identificationMethodB = identificationMethodB;
    }
}
