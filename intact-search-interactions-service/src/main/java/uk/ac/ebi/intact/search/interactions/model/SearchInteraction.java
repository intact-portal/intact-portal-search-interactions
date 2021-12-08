package uk.ac.ebi.intact.search.interactions.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.ChildDocument;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

/**
 * @author Elisabet Barrera
 */
@SolrDocument(collection = SearchInteraction.INTERACTIONS)
public class SearchInteraction extends AdvanceSearchInteraction {

    public static final String INTERACTIONS = "interactions";

    @Id
    @Field(AC)
    @Indexed
    private String ac;

    @Field(BINARY_INTERACTION_ID)
    @Indexed
    private long binaryInteractionId;

    @Field(DOCUMENT_TYPE)
    private String documentType;

    //Stores preferred identifier + database e.g "O95644 (uniprotkb)"
    @Field(ID_A)
    private String idA;

    //Stores preferred identifier + database e.g "Q8WXI9 (uniprotkb)"
    @Field(ID_B)
    private String idB;

    //Stores IntAct accession: "EBI-6907210"
    @Field(AC_A)
    private String acA;

    //Stores IntAct accession: "EBI-923440"
    @Field(AC_B)
    private String acB;

    @Field(UNIQUE_ID_A)
    private String uniqueIdA;

    @Field(UNIQUE_ID_B)
    private String uniqueIdB;

    @Field(MOLECULE_A)
    private String moleculeA;

    @Field(MOLECULE_B)
    private String moleculeB;

    @Field(INTACT_NAME_A)
    private String intactNameA;

    @Field(INTACT_NAME_B)
    private String intactNameB;

    @Field(MUTATION_A)
    private boolean mutationA;

    @Field(MUTATION_B)
    private boolean mutationB;

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

    @Field(INTRA_TAX_ID)
    private Integer intraTaxId;

    @Field(DESCRIPTION_A)
    private String descriptionA;

    @Field(DESCRIPTION_B)
    private String descriptionB;

    @Field(TYPE_A)
    private String typeA;

    @Field(TYPE_B)
    private String typeB;

    @Field(TYPE_MI_A)
    private String typeMIA;

    @Field(TYPE_MI_B)
    private String typeMIB;

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

    @Field(INTRA_SPECIES)
    private String intraSpecies;

    //participants

    @Field(BIOLOGICAL_ROLE_A)
    private String biologicalRoleA;

    @Field(BIOLOGICAL_ROLE_B)
    private String biologicalRoleB;

    @Field(EXPERIMENTAL_ROLE_A)
    private String experimentalRoleA;

    @Field(EXPERIMENTAL_ROLE_B)
    private String experimentalRoleB;

    @Field(BIOLOGICAL_ROLE_MI_IDENTIFIER_A)
    private String biologicalRoleMIIdentifierA;

    @Field(BIOLOGICAL_ROLE_MI_IDENTIFIER_B)
    private String biologicalRoleMIIdentifierB;

    @Field(EXPERIMENTAL_ROLE_MI_IDENTIFIER_A)
    private String experimentalRoleMIIdentifierA;

    @Field(EXPERIMENTAL_ROLE_MI_IDENTIFIER_B)
    private String experimentalRoleMIIdentifierB;

    @Field(FEATURE_A)
    private Set<String> featuresA;

    @Field(FEATURE_B)
    private Set<String> featuresB;

    @Field(FEATURE_SHORTLABEL_A)
    private Set<String> featureShortLabelsA;

    @Field(FEATURE_SHORTLABEL_B)
    private Set<String> featureShortLabelsB;

    @Field(FEATURE_TYPE_A)
    private Set<String> featureTypesA;

    @Field(FEATURE_TYPE_B)
    private Set<String> featureTypesB;

    @Field(FEATURE_RANGES_A)
    private Set<String> featureRangesA;

    @Field(FEATURE_RANGES_B)
    private Set<String> featureRangesB;

    @Field(FEATURE_COUNT)
    private Integer featureCount;

    @Field(STOICHIOMETRY_A)
    private String stoichiometryA;

    @Field(STOICHIOMETRY_B)
    private String stoichiometryB;

    @Field(IDENTIFICATION_METHODS_A)
    private Set<String> identificationMethodsA;

    @Field(IDENTIFICATION_METHODS_B)
    private Set<String> identificationMethodsB;

    @Field(IDENTIFICATION_METHOD_MI_IDENTIFIERS_A)
    private Set<String> identificationMethodMIIdentifiersA;

    @Field(IDENTIFICATION_METHOD_MI_IDENTIFIERS_B)
    private Set<String> identificationMethodMIIdentifiersB;

    @Field(EXPERIMENTAL_PREPARATIONS_A)
    private Set<String> experimentalPreparationsA;

    @Field(EXPERIMENTAL_PREPARATIONS_B)
    private Set<String> experimentalPreparationsB;

    // Interaction
    @Field(DETECTION_METHOD)
    private String detectionMethod;

    @Field(DETECTION_METHOD_MI_IDENTIFIER)
    private String detectionMethodMIIdentifier;

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

    @Field(PARAMETER_TYPES)
    private Set<String> parameterTypes;

    @Field(CREATION_DATE)
    private Date creationDate;

    @Field(UPDATION_DATE)
    private Date updationDate;

    @Field(CHECKSUMS)
    private Set<String> checksums;

    @Field(NEGATIVE)
    private boolean negative;

    @Field(TYPE)
    private String type;

    @Field(HOST_ORGANISM)
    private String hostOrganism;

    @Field(HOST_ORGANISM_TAX_ID)
    private Integer hostOrganismTaxId;

    @Field(INTACT_MISCORE)
    private double intactMiscore;

    @Field(PUBLICATION_IDENTIFIERS)
    private Set<String> publicationIdentifiers;

    @Field(PUBLICATION_ANNOTATIONS)
    private Set<String> publicationAnnotations;

    @Field(PUBLICATION_PUBMED_IDENTIFIER)
    private String publicationPubmedIdentifier;

    //It will be used in the cluster index. Review in the future
    @Field(COUNT)
    private Integer count;

    @Field(TYPE_MI_IDENTIFIER)
    private String typeMIIdentifier;

    @Field(AFFECTED_BY_MUTATION)
    private boolean affectedByMutation;

    @Field(FIRST_AUTHOR)
    private String firstAuthor;

    @Field(RELEASE_DATE)
    private Date releaseDate;

    /* Fields related with the styling of the network and other visual components */
    @Field(INTRA_TAX_ID_STYLED)
    private String intraTaxIdStyled;

    @Field(TAX_IDA_STYLED)
    private String taxIdAStyled;

    @Field(TAX_IDB_STYLED)
    private String taxIdBStyled;

    @Field(TYPE_MI_IDENTIFIER_STYLED)
    private String typeMIIdentifierStyled;

    @Field(TYPE_MI_A_STYLED)
    private String typeMIAStyled;

    @Field(TYPE_MI_B_STYLED)
    private String typeMIBStyled;

    @Field(HOST_ORGANISM_TAXID_STYLED)
    private String hostOrganismTaxIdStyled;

    @Field(AFFECTED_BY_MUTATION_STYLED)
    private String affectedByMutationStyled;

    @ChildDocument
    private List<SearchChildInteractor> searchChildInteractors;

    public SearchInteraction() {
    }

    public SearchInteraction(Integer count, String idA, String idB, Set<String> altIdsA, Set<String> altIdsB,
                             Set<String> aliasesA, Set<String> aliasesB, Integer taxIdA, Integer taxIdB, String typeA,
                             String typeB, Set<String> xrefsA, Set<String> xrefsB, Set<String> annotationsA,
                             Set<String> annotationsB, Set<String> checksumsA, Set<String> checksumsB, String speciesA,
                             String speciesB, String biologicalRoleA, String biologicalRoleB, String experimentalRoleA,
                             String experimentalRoleB, Set<String> featuresA, Set<String> featuresB, String stoichiometryA,
                             String stoichiometryB, Set<String> identificationMethodsA, Set<String> identificationMethodB,
                             String detectionMethod, Set<String> authors, String sourceDatabase,
                             Set<String> identifiers, Set<String> confidenceValues, String expansionMethod,
                             Set<String> xrefs, Set<String> allAnnotations, Set<String> annotations,
                             Set<String> parameters, Date creationDate, Date updationDate,
                             Set<String> checksums, boolean negative, String type, String typeMIA, String typeMIB,
                             String typeMIIdentifier, boolean affectedByMutation,
                             boolean mutationA, boolean mutationB, int binaryInteractionId, String acA, String acB,
                             Integer featureCount, String descriptionA, String descriptionB,
                             List<SearchChildInteractor> searchChildInteractors, String intactNameA, String intactNameB,
                             String publicationPubmedIdentifier, Set<String> featureTypesA, Set<String> featureTypesB,
                             Set<String> parameterTypes, String detectionMethodMIIdentifier,
                             Set<String> identificationMethodMIIdentifiersA, Set<String> identificationMethodMIIdentifiersB,
                             String biologicalRoleMIIdentifierA, String biologicalRoleMIIdentifierB, String experimentalRoleMIIdentifierA,
                             String experimentalRoleMIIdentifierB, Set<String> featureRangesA, Set<String> featureRangesB,
                             Set<String> publicationAnnotations, Integer hostOrganismTaxId, String documentType) {
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
        this.featuresA = featuresA;
        this.featuresB = featuresB;
        this.stoichiometryA = stoichiometryA;
        this.stoichiometryB = stoichiometryB;
        this.identificationMethodsA = identificationMethodsA;
        this.identificationMethodsB = identificationMethodB;
        this.identificationMethodMIIdentifiersA = identificationMethodMIIdentifiersA;
        this.identificationMethodMIIdentifiersB = identificationMethodMIIdentifiersB;
        this.detectionMethod = detectionMethod;
        this.detectionMethodMIIdentifier = detectionMethodMIIdentifier;
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
        this.affectedByMutation = affectedByMutation;
        this.mutationA = mutationA;
        this.mutationB = mutationB;
        this.featureCount = featureCount;
        this.descriptionA = descriptionA;
        this.descriptionB = descriptionB;
        this.binaryInteractionId = binaryInteractionId;
        this.searchChildInteractors = searchChildInteractors;
        this.intactNameA = intactNameA;
        this.intactNameB = intactNameB;
        this.publicationPubmedIdentifier = publicationPubmedIdentifier;
        this.featureTypesA = featureTypesA;
        this.featureTypesB = featureTypesB;
        this.parameterTypes = parameterTypes;
        this.biologicalRoleMIIdentifierA = biologicalRoleMIIdentifierA;
        this.biologicalRoleMIIdentifierB = biologicalRoleMIIdentifierB;
        this.experimentalRoleMIIdentifierA = experimentalRoleMIIdentifierA;
        this.experimentalRoleMIIdentifierB = experimentalRoleMIIdentifierB;
        this.featureRangesA = featureRangesA;
        this.featureRangesB = featureRangesB;
        this.publicationAnnotations = publicationAnnotations;
        this.hostOrganismTaxId = hostOrganismTaxId;
        this.documentType = documentType;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public long getBinaryInteractionId() {
        return binaryInteractionId;
    }

    public void setBinaryInteractionId(long binaryInteractionId) {
        this.binaryInteractionId = binaryInteractionId;
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

    public String getIntactNameA() {
        return intactNameA;
    }

    public void setIntactNameA(String intactNameA) {
        this.intactNameA = intactNameA;
    }

    public String getIntactNameB() {
        return intactNameB;
    }

    public void setIntactNameB(String intactNameB) {
        this.intactNameB = intactNameB;
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

    public Integer getIntraTaxId() {
        return intraTaxId;
    }

    public void setIntraTaxId(Integer intraTaxId) {
        this.intraTaxId = intraTaxId;
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

    public String getIntraSpecies() {
        return intraSpecies;
    }

    public void setIntraSpecies(String intraSpecies) {
        this.intraSpecies = intraSpecies;
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

    public String getBiologicalRoleMIIdentifierA() {
        return biologicalRoleMIIdentifierA;
    }

    public void setBiologicalRoleMIIdentifierA(String biologicalRoleMIIdentifierA) {
        this.biologicalRoleMIIdentifierA = biologicalRoleMIIdentifierA;
    }

    public String getBiologicalRoleMIIdentifierB() {
        return biologicalRoleMIIdentifierB;
    }

    public void setBiologicalRoleMIIdentifierB(String biologicalRoleMIIdentifierB) {
        this.biologicalRoleMIIdentifierB = biologicalRoleMIIdentifierB;
    }

    public String getExperimentalRoleMIIdentifierA() {
        return experimentalRoleMIIdentifierA;
    }

    public void setExperimentalRoleMIIdentifierA(String experimentalRoleMIIdentifierA) {
        this.experimentalRoleMIIdentifierA = experimentalRoleMIIdentifierA;
    }

    public String getExperimentalRoleMIIdentifierB() {
        return experimentalRoleMIIdentifierB;
    }

    public void setExperimentalRoleMIIdentifierB(String experimentalRoleMIIdentifierB) {
        this.experimentalRoleMIIdentifierB = experimentalRoleMIIdentifierB;
    }

    public Set<String> getFeaturesA() {
        return featuresA;
    }

    public void setFeaturesA(Set<String> featuresA) {
        this.featuresA = featuresA;
    }

    public Set<String> getFeaturesB() {
        return featuresB;
    }

    public void setFeaturesB(Set<String> featuresB) {
        this.featuresB = featuresB;
    }

    public Set<String> getFeatureShortLabelsA() {
        return featureShortLabelsA;
    }

    public void setFeatureShortLabelsA(Set<String> featureShortLabelsA) {
        this.featureShortLabelsA = featureShortLabelsA;
    }

    public Set<String> getFeatureShortLabelsB() {
        return featureShortLabelsB;
    }

    public void setFeatureShortLabelsB(Set<String> featureShortLabelsB) {
        this.featureShortLabelsB = featureShortLabelsB;
    }

    public Set<String> getFeatureTypesA() {
        return featureTypesA;
    }

    public void setFeatureTypesA(Set<String> featureTypesA) {
        this.featureTypesA = featureTypesA;
    }

    public Set<String> getFeatureTypesB() {
        return featureTypesB;
    }

    public void setFeatureTypesB(Set<String> featureTypesB) {
        this.featureTypesB = featureTypesB;
    }

    public Set<String> getFeatureRangesA() {
        return featureRangesA;
    }

    public void setFeatureRangesA(Set<String> featureRangesA) {
        this.featureRangesA = featureRangesA;
    }

    public Set<String> getFeatureRangesB() {
        return featureRangesB;
    }

    public void setFeatureRangesB(Set<String> featureRangesB) {
        this.featureRangesB = featureRangesB;
    }

    public Integer getFeatureCount() {
        return featureCount;
    }

    public void setFeatureCount(Integer featureCount) {
        this.featureCount = featureCount;
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

    public Set<String> getIdentificationMethodsA() {
        return identificationMethodsA;
    }

    public void setIdentificationMethodsA(Set<String> identificationMethodsA) {
        this.identificationMethodsA = identificationMethodsA;
    }

    public Set<String> getIdentificationMethodsB() {
        return identificationMethodsB;
    }

    public void setIdentificationMethodsB(Set<String> identificationMethodsB) {
        this.identificationMethodsB = identificationMethodsB;
    }

    public Set<String> getIdentificationMethodMIIdentifiersA() {
        return identificationMethodMIIdentifiersA;
    }

    public void setIdentificationMethodMIIdentifiersA(Set<String> identificationMethodMIIdentifiersA) {
        this.identificationMethodMIIdentifiersA = identificationMethodMIIdentifiersA;
    }

    public Set<String> getIdentificationMethodMIIdentifiersB() {
        return identificationMethodMIIdentifiersB;
    }

    public void setIdentificationMethodMIIdentifiersB(Set<String> identificationMethodMIIdentifiersB) {
        this.identificationMethodMIIdentifiersB = identificationMethodMIIdentifiersB;
    }

    public Set<String> getExperimentalPreparationsA() {
        return experimentalPreparationsA;
    }

    public void setExperimentalPreparationsA(Set<String> experimentalPreparationsA) {
        this.experimentalPreparationsA = experimentalPreparationsA;
    }

    public Set<String> getExperimentalPreparationsB() {
        return experimentalPreparationsB;
    }

    public void setExperimentalPreparationsB(Set<String> experimentalPreparationsB) {
        this.experimentalPreparationsB = experimentalPreparationsB;
    }

    public String getDetectionMethod() {
        return detectionMethod;
    }

    public void setDetectionMethod(String detectionMethod) {
        this.detectionMethod = detectionMethod;
    }

    public String getDetectionMethodMIIdentifier() {
        return detectionMethodMIIdentifier;
    }

    public void setDetectionMethodMIIdentifier(String detectionMethodMIIdentifier) {
        this.detectionMethodMIIdentifier = detectionMethodMIIdentifier;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public String getSourceDatabase() {
        return sourceDatabase;
    }

    public void setSourceDatabase(String sourceDatabase) {
        this.sourceDatabase = sourceDatabase;
    }

    public Set<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Set<String> identifiers) {
        this.identifiers = identifiers;
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

    public Set<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<String> annotations) {
        this.annotations = annotations;
    }

    public Set<String> getParameters() {
        return parameters;
    }

    public void setParameters(Set<String> parameters) {
        this.parameters = parameters;
    }

    public Set<String> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Set<String> parameterTypes) {
        this.parameterTypes = parameterTypes;
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

    public Set<String> getChecksums() {
        return checksums;
    }

    public void setChecksums(Set<String> checksums) {
        this.checksums = checksums;
    }

    public boolean isNegative() {
        return negative;
    }

    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHostOrganism() {
        return hostOrganism;
    }

    public void setHostOrganism(String hostOrganism) {
        this.hostOrganism = hostOrganism;
    }

    public Integer getHostOrganismTaxId() {
        return hostOrganismTaxId;
    }

    public void setHostOrganismTaxId(Integer hostOrganismTaxId) {
        this.hostOrganismTaxId = hostOrganismTaxId;
    }

    public double getIntactMiscore() {
        return intactMiscore;
    }

    public void setIntactMiscore(double intactMiscore) {
        this.intactMiscore = intactMiscore;
    }

    public Set<String> getPublicationIdentifiers() {
        return publicationIdentifiers;
    }

    public void setPublicationIdentifiers(Set<String> publicationIdentifiers) {
        this.publicationIdentifiers = publicationIdentifiers;
    }

    public Set<String> getPublicationAnnotations() {
        return publicationAnnotations;
    }

    public void setPublicationAnnotations(Set<String> publicationAnnotations) {
        this.publicationAnnotations = publicationAnnotations;
    }

    public String getPublicationPubmedIdentifier() {
        return publicationPubmedIdentifier;
    }

    public void setPublicationPubmedIdentifier(String publicationPubmedIdentifier) {
        this.publicationPubmedIdentifier = publicationPubmedIdentifier;
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

    public boolean isAffectedByMutation() {
        return affectedByMutation;
    }

    public void setAffectedByMutation(boolean affectedByMutation) {
        this.affectedByMutation = affectedByMutation;
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

    public String getIntraTaxIdStyled() {
        return intraTaxIdStyled;
    }

    public void setIntraTaxIdStyled(String intraTaxIdStyled) {
        this.intraTaxIdStyled = intraTaxIdStyled;
    }

    public String getTaxIdAStyled() {
        return taxIdAStyled;
    }

    public void setTaxIdAStyled(String taxIdAStyled) {
        this.taxIdAStyled = taxIdAStyled;
    }

    public String getTaxIdBStyled() {
        return taxIdBStyled;
    }

    public void setTaxIdBStyled(String taxIdBStyled) {
        this.taxIdBStyled = taxIdBStyled;
    }

    public String getTypeMIIdentifierStyled() {
        return typeMIIdentifierStyled;
    }

    public void setTypeMIIdentifierStyled(String typeMIIdentifierStyled) {
        this.typeMIIdentifierStyled = typeMIIdentifierStyled;
    }

    public String getTypeMIAStyled() {
        return typeMIAStyled;
    }

    public void setTypeMIAStyled(String typeMIAStyled) {
        this.typeMIAStyled = typeMIAStyled;
    }

    public String getTypeMIBStyled() {
        return typeMIBStyled;
    }

    public void setTypeMIBStyled(String typeMIBStyled) {
        this.typeMIBStyled = typeMIBStyled;
    }

    public String getHostOrganismTaxIdStyled() {
        return hostOrganismTaxIdStyled;
    }

    public void setHostOrganismTaxIdStyled(String hostOrganismTaxIdStyled) {
        this.hostOrganismTaxIdStyled = hostOrganismTaxIdStyled;
    }

    public String getAffectedByMutationStyled() {
        return affectedByMutationStyled;
    }

    public void setAffectedByMutationStyled(String affectedByMutationStyled) {
        this.affectedByMutationStyled = affectedByMutationStyled;
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
                ", biologicalRoleMIIdentifierA='" + biologicalRoleMIIdentifierA + '\'' +
                ", biologicalRoleMIIdentifierB='" + biologicalRoleMIIdentifierB + '\'' +
                ", experimentalRoleMIIdentifierA='" + experimentalRoleMIIdentifierA + '\'' +
                ", experimentalRoleMIIdentifierB='" + experimentalRoleMIIdentifierB + '\'' +
                ", featureA=" + featuresA +
                ", featureB=" + featuresB +
                ", featureShortLabelA=" + featureShortLabelsA +
                ", featureShortLabelB=" + featureShortLabelsB +
                ", featureTypesA=" + featureTypesA +
                ", featureTypesB=" + featureTypesB +
                ", featureRangesA=" + featureRangesA +
                ", featureRangesB=" + featureRangesB +
                ", featureCount=" + featureCount +
                ", stoichiometryA='" + stoichiometryA + '\'' +
                ", stoichiometryB='" + stoichiometryB + '\'' +
                ", identificationMethodA=" + identificationMethodsA +
                ", identificationMethodB=" + identificationMethodsB +
                ", identificationMethodMIIdentifierA=" + identificationMethodMIIdentifiersA +
                ", identificationMethodMIIdentifierB=" + identificationMethodMIIdentifiersB +
                ", detectionMethod='" + detectionMethod + '\'' +
                ", detectionMethodMIIdentifier='" + detectionMethodMIIdentifier + '\'' +
                ", authors=" + authors +
                ", sourceDatabase='" + sourceDatabase + '\'' +
                ", identifiers=" + identifiers +
                ", confidenceValues=" + confidenceValues +
                ", expansionMethod='" + expansionMethod + '\'' +
                ", xrefs=" + xrefs +
                ", allAnnotations=" + allAnnotations +
                ", annotations=" + annotations +
                ", parameters=" + parameters +
                ", parameterTypes=" + parameterTypes +
                ", creationDate=" + creationDate +
                ", updationDate=" + updationDate +
                ", checksums=" + checksums +
                ", negative=" + negative +
                ", type='" + type + '\'' +
                ", hostOrganism='" + hostOrganism + '\'' +
                ", hostOrganismTaxId=" + hostOrganismTaxId +
                ", intactMiscore=" + intactMiscore +
                ", moleculeA='" + moleculeA + '\'' +
                ", moleculeB='" + moleculeB + '\'' +
                ", intactNameA='" + intactNameA + '\'' +
                ", intactNameB='" + intactNameB + '\'' +
                ", firstAuthor='" + firstAuthor + '\'' +
                ", experimentalPreparationsA=" + experimentalPreparationsA +
                ", experimentalPreparationsB=" + experimentalPreparationsB +
                ", releaseDate=" + releaseDate +
                ", uniqueIdA='" + uniqueIdA + '\'' +
                ", uniqueIdB='" + uniqueIdB + '\'' +
                ", publicationIdentifiers=" + publicationIdentifiers +
                ", publicationAnnotations=" + publicationAnnotations +
                ", publicationPubmedIdentifier='" + publicationPubmedIdentifier + '\'' +
                ", count=" + count +
                ", typeMIA='" + typeMIA + '\'' +
                ", typeMIB='" + typeMIB + '\'' +
                ", typeMIIdentifier='" + typeMIIdentifier + '\'' +
                ", disruptedByMutation=" + affectedByMutation +
                ", mutationA=" + mutationA +
                ", mutationB=" + mutationB +
                ", searchChildInteractors=" + searchChildInteractors +
                '}';
    }
}