package uk.ac.ebi.intact.search.interactions.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.ChildDocument;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.SearchChildInteractorFields.DOCUMENT_TYPE;
import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

/**
 * @author Elisabet Barrera
 */
@SolrDocument(collection = SearchInteraction.INTERACTIONS)
public class SearchInteraction {

    public static final String INTERACTIONS = "interactions";

    @Id
    @Field(AC)
    @Indexed
    private String ac;

    @Field(BINARY_INTERACTION_ID)
    @Indexed
    private int binaryInteractionId;

    @Field(DOCUMENT_TYPE)
    private String documentType;

    //TODO Do we need idA and idB or ar the same than acA acB?
    @Field(ID_A)
    private String idA;

    @Field(ID_B)
    private String idB;

    @Field(AC_A)
    private String acA;

    @Field(AC_B)
    private String acB;

    @Field(ALT_IDS_A)
    private Set<String> altIdsA;

    @Field(ALT_IDS_B)
    private Set<String> altIdsB;

    @Field(ALIASES_A)
    private Set<String> aliasesA;

    @Field(ALIASES_B)
    private Set<String> aliasesB;

    @Field(TAX_IDA)
    private Integer taxIdA;

    @Field(TAX_IDB)
    private Integer taxIdB;

    @Field(DESCRIPTION_A)
    private String descriptionA;

    @Field(DESCRIPTION_B)
    private String descriptionB;

    @Field(TYPE_A)
    private String typeA;

    @Field(TYPE_B)
    private String typeB;

    @Field(XREFS_A)
    private Set<String> xrefsA;

    @Field(XREFS_B)
    private Set<String> xrefsB;

    @Field(ANNOTATIONS_A)
    private Set<String> annotationsA;

    @Field(ANNOTATIONS_B)
    private Set<String> annotationsB;

    @Field(CHECKSUMS_A)
    private Set<String> checksumsA;

    @Field(CHECKSUMS_B)
    private Set<String> checksumsB;

    @Field(SPECIES_A)
    private String speciesA;

    @Field(SPECIES_B)
    private String speciesB;

    //participants

    @Field(BIOLOGICAL_ROLE_A)
    private String biologicalRoleA;

    @Field(BIOLOGICAL_ROLE_B)
    private String biologicalRoleB;

    @Field(EXPERIMENTAL_ROLE_A)
    private String experimentalRoleA;

    @Field(EXPERIMENTAL_ROLE_B)
    private String experimentalRoleB;

    @Field(FEATURE_A)
    private Set<String> featureA;

    @Field(FEATURE_B)
    private Set<String> featureB;

    @Field(FEATURE_SHORTLABEL_A)
    private Set<String> featureShortLabelA;

    @Field(FEATURE_SHORTLABEL_B)
    private Set<String> featureShortLabelB;

    @Field(FEATURE_COUNT)
    private Integer featureCount;

    @Field(STOICHIOMETRY_A)
    private String stoichiometryA;

    @Field(STOICHIOMETRY_B)
    private String stoichiometryB;

    @Field(IDENTIFICATION_METHOD_A)
    private Set<String> identificationMethodA;

    @Field(IDENTIFICATION_METHOD_B)
    private Set<String> identificationMethodB;

    @Field(DETECTION_METHOD)
    private String detectionMethod;

    @Field(PUBLICATION_AUTHORS)
    private Set<String> authors;

    @Field(SOURCE_DATABASE)
    private String sourceDatabase;

    @Field(IDENTIFIERS)
    private Set<String> identifiers;

    @Field(CONFIDENCE_VALUES)
    private Set<String> confidenceValues;

    @Field(EXPANSION_METHOD)
    private String expansionMethod;

    @Field(XREFS)
    private Set<String> xrefs;

    //TODO Comment differences between allAnnotations and annotations
    @Field(ALL_ANNOTATIONS)
    private Set<String> allAnnotations;

    @Field(ANNOTATIONS)
    private Set<String> annotations;

    @Field(PARAMETERS)
    private Set<String> parameters;

    @Field(CREATION_DATE)
    private Date creationDate;

    @Field(UPDATION_DATE)
    private Date updationDate;

    @Field(CHECKSUM)
    private Set<String> checksums;

    @Field(NEGATIVE)
    private boolean negative;

    @Field(TYPE)
    private String type;

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

    //It will be used in the cluster index. Review in the future
    @Field(COUNT)
    private Integer count;

    @Field(TYPE_MI_A)
    private String typeMIA;

    @Field(TYPE_MI_B)
    private String typeMIB;

    @Field(TYPE_MI_IDENTIFIER)
    private String typeMIIdentifier;

    @Field(DISRUPTED_BY_MUTATION)
    private boolean disruptedByMutation;

    @Field(MUTATION_A)
    private boolean mutationA;

    @Field(MUTATION_B)
    private boolean mutationB;

    @ChildDocument
    private List<SearchChildInteractor> searchChildInteractors;

    public SearchInteraction() {
    }

    public SearchInteraction(Integer count, String idA, String idB, Set<String> altIdsA, Set<String> altIdsB,
                             Set<String> aliasesA, Set<String> aliasesB, Integer taxIdA, Integer taxIdB, String typeA,
                             String typeB, Set<String> xrefsA, Set<String> xrefsB, Set<String> annotationsA,
                             Set<String> annotationsB, Set<String> checksumsA, Set<String> checksumsB, String speciesA,
                             String speciesB, String biologicalRoleA, String biologicalRoleB, String experimentalRoleA,
                             String experimentalRoleB, Set<String> featureA, Set<String> featureB, String stoichiometryA,
                             String stoichiometryB, Set<String> identificationMethodA, Set<String> identificationMethodB,
                             String detectionMethod, Set<String> authors, String sourceDatabase,
                             Set<String> identifiers, Set<String> confidenceValues, String expansionMethod,
                             Set<String> xrefs, Set<String> allAnnotations, Set<String> annotations,
                             Set<String> parameters, Date creationDate, Date updationDate,
                             Set<String> checksums, boolean negative, String type, String typeMIA, String typeMIB,
                             String typeMIIdentifier, boolean disruptedByMutation,
                             boolean mutationA, boolean mutationB, int binaryInteractionId, String acA, String acB, Integer featureCount,String descriptionA,String descriptionB, List<SearchChildInteractor> searchChildInteractors,String documentType) {
        this.count = count;
        this.idA = idA;
        this.idB = idB;
        this.acA = acA;
        this.acB = acB;
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
        this.detectionMethod = detectionMethod;
        this.authors = authors;
        this.sourceDatabase = sourceDatabase;
        this.identifiers = identifiers;
        this.confidenceValues = confidenceValues;
        this.expansionMethod = expansionMethod;
        this.xrefs = xrefs;
        this.allAnnotations = allAnnotations;
        this.annotations = annotations;
        this.parameters = parameters;
        this.creationDate = creationDate;
        this.updationDate = updationDate;
        this.checksums = checksums;
        this.negative = negative;
        this.type = type;
        this.typeMIA = typeMIA;
        this.typeMIB = typeMIB;
        this.typeMIIdentifier = typeMIIdentifier;
        this.disruptedByMutation = disruptedByMutation;
        this.mutationA = mutationA;
        this.mutationB = mutationB;
        this.featureCount = featureCount;
        this.descriptionA = descriptionA;
        this.descriptionB = descriptionB;
        this.binaryInteractionId = binaryInteractionId;
        this.searchChildInteractors= searchChildInteractors;
        this.documentType = documentType;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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

    public String getDescriptionA() {
        return descriptionA;
    }

    public void setDescriptionA(String descriptionA) {
        this.descriptionA = descriptionA;
    }

    public String getDescriptionB() {
        return descriptionB;
    }

    public void setDescriptionB(String descriptionB) {
        this.descriptionB = descriptionB;
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

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
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

    public boolean isNegative() {
        return negative;
    }

    public void setNegative(boolean negative) {
        this.negative = negative;
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

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(String detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public Set<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Set<String> identifiers) {
        this.identifiers = identifiers;
    }

    public Set<String> getXrefs() {
        return xrefs;
    }

    public void setXrefs(Set<String> xrefs) {
        this.xrefs = xrefs;
    }

    public Set<String> getAllAnnotations() {
        return allAnnotations;
    }

    public void setAllAnnotations(Set<String> allAnnotations) {
        this.allAnnotations = allAnnotations;
    }

    public Set<String> getParameters() {
        return parameters;
    }

    public void setParameters(Set<String> parameters) {
        this.parameters = parameters;
    }

    public Set<String> getChecksums() {
        return checksums;
    }

    public void setChecksums(Set<String> checksums) {
        this.checksums = checksums;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTypeMIIdentifier() {
        return typeMIIdentifier;
    }

    public void setTypeMIIdentifier(String typeMIIdentifier) {
        this.typeMIIdentifier = typeMIIdentifier;
    }

    public boolean isDisruptedByMutation() {
        return disruptedByMutation;
    }

    public void setDisruptedByMutation(boolean disruptedByMutation) {
        this.disruptedByMutation = disruptedByMutation;
    }

    public String getAcA() {
        return acA;
    }

    public void setAcA(String acA) {
        this.acA = acA;
    }

    public String getAcB() {
        return acB;
    }

    public void setAcB(String acB) {
        this.acB = acB;
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


    public String getTypeMIA() {
        return typeMIA;
    }

    public void setTypeMIA(String typeMIA) {
        this.typeMIA = typeMIA;
    }

    public String getTypeMIB() {
        return typeMIB;
    }

    public void setTypeMIB(String typeMIB) {
        this.typeMIB = typeMIB;
    }

    public boolean isMutationA() {
        return mutationA;
    }

    public void setMutationA(boolean mutationA) {
        this.mutationA = mutationA;
    }

    public boolean isMutationB() {
        return mutationB;
    }

    public void setMutationB(boolean mutationB) {
        this.mutationB = mutationB;
    }

    public int getBinaryInteractionId() {
        return binaryInteractionId;
    }

    public void setBinaryInteractionId(int binaryInteractionId) {
        this.binaryInteractionId = binaryInteractionId;
    }

    public Set<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<String> annotations) {
        this.annotations = annotations;
    }

    public Integer getFeatureCount() {
        return featureCount;
    }

    public void setFeatureCount(Integer featureCount) {
        this.featureCount = featureCount;
    }

    public List<SearchChildInteractor> getSearchChildInteractors() {
        return searchChildInteractors;
    }

    public void setSearchChildInteractors(List<SearchChildInteractor> searchChildInteractors) {
        this.searchChildInteractors = searchChildInteractors;
    }

    @Override
    public String toString() {
        return "SearchInteraction{" +
                "ac='" + ac + '\'' +
                ", binaryInteractionId=" + binaryInteractionId +
                ", documentType='" + documentType + '\'' +
                ", idA='" + idA + '\'' +
                ", idB='" + idB + '\'' +
                ", acA='" + acA + '\'' +
                ", acB='" + acB + '\'' +
                ", altIdsA=" + altIdsA +
                ", altIdsB=" + altIdsB +
                ", aliasesA=" + aliasesA +
                ", aliasesB=" + aliasesB +
                ", taxIdA=" + taxIdA +
                ", taxIdB=" + taxIdB +
                ", descriptionA='" + descriptionA + '\'' +
                ", descriptionB='" + descriptionB + '\'' +
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
                ", featureShortLabelA=" + featureShortLabelA +
                ", featureShortLabelB=" + featureShortLabelB +
                ", featureCount=" + featureCount +
                ", stoichiometryA='" + stoichiometryA + '\'' +
                ", stoichiometryB='" + stoichiometryB + '\'' +
                ", identificationMethodA=" + identificationMethodA +
                ", identificationMethodB=" + identificationMethodB +
                ", detectionMethod='" + detectionMethod + '\'' +
                ", authors=" + authors +
                ", sourceDatabase='" + sourceDatabase + '\'' +
                ", identifiers=" + identifiers +
                ", confidenceValues=" + confidenceValues +
                ", expansionMethod='" + expansionMethod + '\'' +
                ", xrefs=" + xrefs +
                ", allAnnotations=" + allAnnotations +
                ", annotations=" + annotations +
                ", parameters=" + parameters +
                ", creationDate=" + creationDate +
                ", updationDate=" + updationDate +
                ", checksums=" + checksums +
                ", negative=" + negative +
                ", type='" + type + '\'' +
                ", hostOrganism='" + hostOrganism + '\'' +
                ", intactMiscore=" + intactMiscore +
                ", speciesAB=" + speciesAB +
                ", moleculeA='" + moleculeA + '\'' +
                ", moleculeB='" + moleculeB + '\'' +
                ", firstAuthor='" + firstAuthor + '\'' +
                ", experimentalPreparationsA=" + experimentalPreparationsA +
                ", experimentalPreparationsB=" + experimentalPreparationsB +
                ", releaseDate=" + releaseDate +
                ", uniqueIdA='" + uniqueIdA + '\'' +
                ", uniqueIdB='" + uniqueIdB + '\'' +
                ", publicationIdentifiers=" + publicationIdentifiers +
                ", count=" + count +
                ", typeMIA='" + typeMIA + '\'' +
                ", typeMIB='" + typeMIB + '\'' +
                ", typeMIIdentifier='" + typeMIIdentifier + '\'' +
                ", disruptedByMutation=" + disruptedByMutation +
                ", mutationA=" + mutationA +
                ", mutationB=" + mutationB +
                ", searchChildInteractors=" + searchChildInteractors +
                '}';
    }
}
