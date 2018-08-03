package uk.ac.ebi.intact.search.interactions.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Elisabet Barrera
 */
@SolrDocument(solrCoreName =Interaction.INTERACTIONS)
public class Interaction {

    public static final String INTERACTIONS = "interactions";

    @Id
    @Field(InteractionFields.UNIQUE_KEY)
    @Indexed
    private String uniqueKey;

    @Field("interaction_count")
    @Nullable
    private Integer interactionCount;

    @Field("interactions_ids")
    @Nullable
    private Set<String> interactionIds;

    @Field(InteractionFields.INTERACTOR_IDA)
    @Nullable
    private String idA;

    @Field(InteractionFields.INTERACTOR_IDB)
    @Nullable
    private String idB;

    @Field(InteractionFields.INTERACTOR_AC_A)
    @Nullable
    private String interactorAAc;

    @Field(InteractionFields.INTERACTOR_AC_B)
    @Nullable
    private String interactorBAc;

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

    @Field(InteractionFields.FEATURE_SHORTLABEL_A)
    @Nullable
    private Set<String> featureShortLabelA;

    @Field(InteractionFields.FEATURE_SHORTLABEL_B)
    @Nullable
    private Set<String> featureShortLabelB;

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

    @Field(InteractionFields.INTERACTION_DETECTION_METHOD)
    @Nullable
    private String interactionDetectionMethod;

    @Field(InteractionFields.PUBLICATION_AUTHORS)
    @Nullable
    private Set<String> authors;

    @Field(InteractionFields.PUBLICATION_ID)
    @Nullable
    private String publicationId;

    @Field(InteractionFields.SOURCE_DATABASE)
    @Nullable
    private String sourceDatabase;

    @Field(InteractionFields.INTERACTION_IDENTIFIERS)
    @Nullable
    private Set<String> interactionIdentifiers;

    @Field(InteractionFields.CONFIDENCE_VALUES)
    @Nullable
    private Set<String> confidenceValues;

    @Field(InteractionFields.EXPANSION_METHOD)
    @Nullable
    private String expansionMethod;

    @Field(InteractionFields.INTERACTION_XREFS)
    @Nullable
    private Set<String> interactionXrefs;

    @Field(InteractionFields.INTERACTION_ANNOTATIONS)
    @Nullable
    private Set<String> interactionAnnotations;

    @Field(InteractionFields.INTERACTION_PARAMETERS)
    @Nullable
    private Set<String> interactionParameters;

    @Field(InteractionFields.CREATION_DATE)
    @Nullable
    private Date creationDate;

    @Field(InteractionFields.UPDATION_DATE)
    @Nullable
    private Date updationDate;

    @Field(InteractionFields.INTERACTION_CHECKSUM)
    @Nullable
    private Set<String> interactionChecksums;

    @Field(InteractionFields.INTERACTION_NEGATIVE)
    @Nullable
    private boolean negative;

    @Field(InteractionFields.INTERACTION_TYPE)
    @Nullable
    private String interactionType;

    @Field(InteractionFields.INTERACTION_AC)
    @Nullable
    private String interactionAc;

    @Field(InteractionFields.HOST_ORGANISM)
    @Nullable
    private String hostOrganism;

    @Field(InteractionFields.INTACT_MISCORE)
    @Nullable
    private double intactMiscore;

    @Field(InteractionFields.SPECIES_A_B)
    @Nullable
    private Set<String> speciesAB=new HashSet<String>();

    @Field(InteractionFields.MOLECULE_A)
    @Nullable
    private String moleculeA;

    @Field(InteractionFields.MOLECULE_B)
    @Nullable
    private String moleculeB;

    @Field(InteractionFields.FIRST_AUTHOR)
    @Nullable
    private String firstAuthor;

    @Field(InteractionFields.EXPERIMENTAL_PREPARATIONS_A)
    @Nullable
    private Set<String> experimentalPreparationsA;

    @Field(InteractionFields.EXPERIMENTAL_PREPARATIONS_B)
    @Nullable
    private Set<String> experimentalPreparationsB;

    @Field(InteractionFields.RELEASE_DATE)
    @Nullable
    private Date releaseDate;

    @Field(InteractionFields.UNIQUE_ID_A)
    @Nullable
    private String uniqueIdA;

    @Field(InteractionFields.UNIQUE_ID_B)
    @Nullable
    private String uniqueIdB;



    public Interaction(String uniqueKey, Integer interactionCount, Set<String> interactionIds, String idA, String idB, Set<String> altIdsA, Set<String> altIdsB, Set<String> aliasesA, Set<String> aliasesB, Integer taxIdA, Integer taxIdB, String typeA, String typeB, Set<String> xrefsA, Set<String> xrefsB, Set<String> annotationsA, Set<String> annotationsB, Set<String> checksumsA, Set<String> checksumsB, String speciesA, String speciesB, String biologicalRoleA, String biologicalRoleB, String experimentalRoleA, String experimentalRoleB, Set<String> featureA, Set<String> featureB, String stoichiometryA, String stoichiometryB, Set<String> identificationMethodA, Set<String> identificationMethodB, String interactionDetectionMethod, Set<String> authors, String publicationId, Set<String> sourceDatabases, Set<String> interactionIdentifiers, Set<String> confidenceValues, String expansionMethod, Set<String> interactionXrefs, Set<String> interactionAnnotations, Set<String> interactionParameters, Date creationDate, Date updationDate, Set<String> interactionChecksums, boolean negative, String interactiontype) {
        this.uniqueKey = uniqueKey;
        this.interactionCount = interactionCount;
        this.interactionIds = interactionIds;
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
        this.interactionDetectionMethod = interactionDetectionMethod;
        this.authors = authors;
        this.publicationId = publicationId;
        this.sourceDatabase = sourceDatabase;
        this.interactionIdentifiers = interactionIdentifiers;
        this.confidenceValues = confidenceValues;
        this.expansionMethod = expansionMethod;
        this.interactionXrefs = interactionXrefs;
        this.interactionAnnotations = interactionAnnotations;
        this.interactionParameters = interactionParameters;
        this.creationDate = creationDate;
        this.updationDate = updationDate;
        this.interactionChecksums = interactionChecksums;
        this.negative = negative;

    }

    public Interaction() {
    }

    public Interaction(Set<String> author, Integer interactionCount, Set<String> interactionIds, String publicationId) {
        this.setAuthors(author);
        this.interactionCount = interactionCount;
        this.interactionIds = interactionIds;
        this.setPublicationId(publicationId);
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "uniqueKey='" + uniqueKey + '\'' +
                ", interactionCount=" + interactionCount +
                ", interactionIds=" + interactionIds +
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
                ", interactionDetectionMethod='" + interactionDetectionMethod + '\'' +
                ", authors=" + authors +
                ", publicationId='" + publicationId + '\'' +
                ", sourceDatabase=" + sourceDatabase +
                ", interactionIdentifiers=" + interactionIdentifiers +
                ", confidenceValues=" + confidenceValues +
                ", expansionMethod='" + expansionMethod + '\'' +
                ", interactionXrefs=" + interactionXrefs +
                ", interactionAnnotations=" + interactionAnnotations +
                ", interactionParameters=" + interactionParameters +
                ", creationDate=" + creationDate +
                ", updationDate=" + updationDate +
                ", interactionChecksums=" + interactionChecksums +
                ", negative=" + negative +

                '}';
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
        this.speciesAB.add(speciesA);
    }

    public String getSpeciesB() {
        return speciesB;
    }

    public void setSpeciesB(String speciesB) {
        this.speciesB = speciesB;
        this.speciesAB.add(speciesB);
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

    public String getInteractionDetectionMethod() {
        return interactionDetectionMethod;
    }

    public void setInteractionDetectionMethod(String interactionDetectionMethod) {
        this.interactionDetectionMethod = interactionDetectionMethod;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public String getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    public Set<String> getInteractionIdentifiers() {
        return interactionIdentifiers;
    }

    public void setInteractionIdentifiers(Set<String> interactionIdentifiers) {
        this.interactionIdentifiers = interactionIdentifiers;
    }

    public Set<String> getConfidenceValues() {
        return confidenceValues;
    }

    public void setConfidenceValues(Set<String> confidenceValues) {
        this.confidenceValues = confidenceValues;
    }

    public String getExpansionMethod() {
        return expansionMethod;
    }

    public void setExpansionMethod(String expansionMethod) {
        this.expansionMethod = expansionMethod;
    }

    public Set<String> getInteractionAnnotations() {
        return interactionAnnotations;
    }

    public void setInteractionAnnotations(Set<String> interactionAnnotations) {
        this.interactionAnnotations = interactionAnnotations;
    }

    public Set<String> getInteractionParameters() {
        return interactionParameters;
    }

    public void setInteractionParameters(Set<String> interactionParameters) {
        this.interactionParameters = interactionParameters;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdationDate() {
        return updationDate;
    }

    public void setUpdationDate(Date updationDate) {
        this.updationDate = updationDate;
    }

    public Set<String> getInteractionChecksums() {
        return interactionChecksums;
    }

    public void setInteractionChecksums(Set<String> interactionChecksums) {
        this.interactionChecksums = interactionChecksums;
    }


    public boolean isNegative() {
        return negative;
    }

    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    public Set<String> getInteractionXrefs() {
        return interactionXrefs;
    }

    public void setInteractionXrefs(Set<String> interactionXrefs) {
        this.interactionXrefs = interactionXrefs;
    }


    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public String getInteractionAc() {
        return interactionAc;
    }

    public void setInteractionAc(String interactionAc) {
        this.interactionAc = interactionAc;
    }


    public String getHostOrganism() {
        return hostOrganism;
    }

    public void setHostOrganism(String hostOrganism) {
        this.hostOrganism = hostOrganism;
    }

    public double getIntactMiscore() {
        return intactMiscore;
    }

    public void setIntactMiscore(double intactMiscore) {
        this.intactMiscore = intactMiscore;
    }

    public Set<String> getSpeciesAB() {
        return speciesAB;
    }

    public void setSpeciesAB(Set<String> speciesAB) {
        this.speciesAB = speciesAB;
    }

    public String getInteractorAAc() {
        return interactorAAc;
    }

    public void setInteractorAAc(String interactorAAc) {
        this.interactorAAc = interactorAAc;
    }

    public String getInteractorBAc() {
        return interactorBAc;
    }

    public void setInteractorBAc(String interactorBAc) {
        this.interactorBAc = interactorBAc;
    }

    public Set<String> getFeatureShortLabelA() {
        return featureShortLabelA;
    }

    public void setFeatureShortLabelA(Set<String> featureShortLabelA) {
        this.featureShortLabelA = featureShortLabelA;
    }

    public Set<String> getFeatureShortLabelB() {
        return featureShortLabelB;
    }

    public void setFeatureShortLabelB(Set<String> featureShortLabelB) {
        this.featureShortLabelB = featureShortLabelB;
    }

    public String getSourceDatabase() {
        return sourceDatabase;
    }

    public void setSourceDatabase(String sourceDatabase) {
        this.sourceDatabase = sourceDatabase;
    }

    public String getMoleculeA() {
        return moleculeA;
    }

    public void setMoleculeA(String moleculeA) {
        this.moleculeA = moleculeA;
    }

    public String getMoleculeB() {
        return moleculeB;
    }

    public void setMoleculeB(String moleculeB) {
        this.moleculeB = moleculeB;
    }

    public String getFirstAuthor() {
        return firstAuthor;
    }

    public void setFirstAuthor(String firstAuthor) {
        this.firstAuthor = firstAuthor;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUniqueIdA() {
        return uniqueIdA;
    }

    public void setUniqueIdA(String uniqueIdA) {
        this.uniqueIdA = uniqueIdA;
    }

    public String getUniqueIdB() {
        return uniqueIdB;
    }

    public void setUniqueIdB(String uniqueIdB) {
        this.uniqueIdB = uniqueIdB;
    }

    public Set<String> getExperimentalPreparationsB() {
        return experimentalPreparationsB;
    }

    public void setExperimentalPreparationsB(Set<String> experimentalPreparationsB) {
        this.experimentalPreparationsB = experimentalPreparationsB;
    }

    public Set<String> getExperimentalPreparationsA() {
        return experimentalPreparationsA;
    }

    public void setExperimentalPreparationsA(Set<String> experimentalPreparationsA) {
        this.experimentalPreparationsA = experimentalPreparationsA;
    }
}
