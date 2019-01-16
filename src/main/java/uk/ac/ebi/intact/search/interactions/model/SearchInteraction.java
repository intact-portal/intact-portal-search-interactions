package uk.ac.ebi.intact.search.interactions.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

/**
 * @author Elisabet Barrera
 */
@SolrDocument(solrCoreName =SearchInteraction.INTERACTIONS)
public class SearchInteraction {

    public static final String INTERACTIONS = "interactions";

    @Id
    @Field(SearchInteractionFields.UNIQUE_KEY)
    @Indexed
    private String uniqueKey;

    //It will be used in the cluster index. Review in the future
    @Field("interaction_count")
    private Integer interactionCount;

    @Field(SearchInteractionFields.INTERACTOR_IDA)
    private String idA;

    @Field(SearchInteractionFields.INTERACTOR_IDB)
    private String idB;

    @Field(SearchInteractionFields.INTERACTOR_AC_A)
    private String interactorAAc;

    @Field(SearchInteractionFields.INTERACTOR_AC_B)
    private String interactorBAc;

    @Field(SearchInteractionFields.ALT_IDS_A)
    private Set<String> altIdsA;

    @Field(SearchInteractionFields.ALT_IDS_B)
    private Set<String> altIdsB;

    @Field(SearchInteractionFields.ALIASES_A)
    private Set<String> aliasesA;

    @Field(SearchInteractionFields.ALIASES_B)
    private Set<String> aliasesB;

    @Field(SearchInteractionFields.TAX_IDA)
    private Integer taxIdA;

    @Field(SearchInteractionFields.TAX_IDB)
    private Integer taxIdB;

    @Field(SearchInteractionFields.TYPE_A)
    private String typeA;

    @Field(SearchInteractionFields.TYPE_B)
    private String typeB;

    @Field(SearchInteractionFields.XREFS_A)
    private Set<String> xrefsA;

    @Field(SearchInteractionFields.XREFS_B)
    private Set<String> xrefsB;

    @Field(SearchInteractionFields.ANNOTATIONS_A)
    private Set<String> annotationsA;

    @Field(SearchInteractionFields.ANNOTATIONS_B)
    private Set<String> annotationsB;

    @Field(SearchInteractionFields.CHECKSUMS_A)
    private Set<String> checksumsA;

    @Field(SearchInteractionFields.CHECKSUMS_B)
    private Set<String> checksumsB;

    @Field(SearchInteractionFields.SPECIES_A)
    private String speciesA;

    @Field(SearchInteractionFields.SPECIES_B)
    private String speciesB;

    //participants

    @Field(SearchInteractionFields.BIOLOGICAL_ROLE_A)
    private String biologicalRoleA;

    @Field(SearchInteractionFields.BIOLOGICAL_ROLE_B)
    private String biologicalRoleB;

    @Field(SearchInteractionFields.EXPERIMENTAL_ROLE_A)
    private String experimentalRoleA;

    @Field(SearchInteractionFields.EXPERIMENTAL_ROLE_B)
    private String experimentalRoleB;

    @Field(SearchInteractionFields.FEATURE_A)
    private Set<String> featureA;

    @Field(SearchInteractionFields.FEATURE_B)
    private Set<String> featureB;

    @Field(SearchInteractionFields.FEATURE_SHORTLABEL_A)
    private Set<String> featureShortLabelA;

    @Field(SearchInteractionFields.FEATURE_SHORTLABEL_B)
    private Set<String> featureShortLabelB;

    @Field(SearchInteractionFields.STOICHIOMETRY_A)
    private String stoichiometryA;

    @Field(SearchInteractionFields.STOICHIOMETRY_B)
    private String stoichiometryB;

    @Field(SearchInteractionFields.IDENTIFICATION_METHOD_A)
    private Set<String> identificationMethodA;

    @Field(SearchInteractionFields.IDENTIFICATION_METHOD_B)
    private Set<String> identificationMethodB;

    @Field(SearchInteractionFields.INTERACTION_DETECTION_METHOD)
    private String interactionDetectionMethod;

    @Field(SearchInteractionFields.PUBLICATION_AUTHORS)
    private LinkedHashSet<String> authors;

    @Field(SOURCE_DATABASE)
    private String sourceDatabase;

    @Field(INTERACTION_IDENTIFIERS)
    private Set<String> interactionIdentifiers;

    @Field(CONFIDENCE_VALUES)
    private Set<String> confidenceValues;

    @Field(EXPANSION_METHOD)
    private String expansionMethod;

    @Field(INTERACTION_XREFS)
    private Set<String> interactionXrefs;

    @Field(INTERACTION_ANNOTATIONS)
    private Set<String> interactionAnnotations;

    @Field(INTERACTION_PARAMETERS)
    private Set<String> interactionParameters;

    @Field(CREATION_DATE)
    private Date creationDate;

    @Field(UPDATION_DATE)
    private Date updationDate;

    @Field(INTERACTION_CHECKSUM)
    private Set<String> interactionChecksums;

    @Field(INTERACTION_NEGATIVE)
    private boolean negative;

    @Field(INTERACTION_TYPE)
    private String interactionType;

    @Field(INTERACTION_AC)
    private String interactionAc;

    @Field(HOST_ORGANISM)
    private String hostOrganism;

    @Field(INTACT_MISCORE)
    private double intactMiscore;

    @Field(SPECIES_A_B)
    private Set<String> speciesAB = new HashSet<>();

    @Field(MOLECULE_A)
    private String moleculeA;

    @Field(MOLECULE_B)
    private String moleculeB;

    @Field(FIRST_AUTHOR)
    private String firstAuthor;

    @Field(EXPERIMENTAL_PREPARATIONS_A)
    private Set<String> experimentalPreparationsA;

    @Field(EXPERIMENTAL_PREPARATIONS_B)
    private Set<String> experimentalPreparationsB;

    @Field(RELEASE_DATE)
    private Date releaseDate;

    @Field(UNIQUE_ID_A)
    private String uniqueIdA;

    @Field(UNIQUE_ID_B)
    private String uniqueIdB;

    @Field(PUBLICATION_IDENTIFIERS)
    private Set<String> publicationIdentifiers;

    public SearchInteraction() {
    }

    public SearchInteraction(String uniqueKey, Integer interactionCount, String idA,
                             String idB, Set<String> altIdsA, Set<String> altIdsB, Set<String> aliasesA,
                             Set<String> aliasesB, Integer taxIdA, Integer taxIdB, String typeA, String typeB,
                             Set<String> xrefsA, Set<String> xrefsB, Set<String> annotationsA, Set<String> annotationsB,
                             Set<String> checksumsA, Set<String> checksumsB, String speciesA, String speciesB,
                             String biologicalRoleA, String biologicalRoleB, String experimentalRoleA,
                             String experimentalRoleB, Set<String> featureA, Set<String> featureB, String stoichiometryA,
                             String stoichiometryB, Set<String> identificationMethodA, Set<String> identificationMethodB,
                             String interactionDetectionMethod, LinkedHashSet<String> authors,
                             String sourceDatabase, Set<String> interactionIdentifiers, Set<String> confidenceValues,
                             String expansionMethod, Set<String> interactionXrefs, Set<String> interactionAnnotations,
                             Set<String> interactionParameters, Date creationDate, Date updationDate,
                             Set<String> interactionChecksums, boolean negative, String interactionType) {
        this.uniqueKey = uniqueKey;
        this.interactionCount = interactionCount;
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
        this.interactionType = interactionType;
    }

    @Override
    public String toString() {
        return "SearchInteraction{" +
                "uniqueKey='" + uniqueKey + '\'' +
                ", interactionCount=" + interactionCount +
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

    public LinkedHashSet<String> getAuthors() {
        return authors;
    }

    public void setAuthors(LinkedHashSet<String> authors) {
        this.authors = authors;
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

    public Set<String> getPublicationIdentifiers() {
        return publicationIdentifiers;
    }

    public void setPublicationIdentifiers(Set<String> publicationIdentifiers) {
        this.publicationIdentifiers = publicationIdentifiers;
    }
}
