package uk.ac.ebi.intact.search.interactions.model;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Set;

import static uk.ac.ebi.intact.search.interactions.model.AdvancedSearchInteractionFields.AdvancedSearchFieldConstants.*;

@SolrDocument(collection = SearchInteraction.INTERACTIONS)
public class AdvancedSearchInteraction {

    @Field(ID_A)
    private Set<String> asIdA;

    @Field(ID_B)
    private Set<String> asIdB;

    @Field(ALTID_A)
    private Set<String> asAltidA;

    @Field(ALTID_B)
    private Set<String> asAltidB;

    @Field(ALIAS_A)
    private Set<String> asAliasA;

    @Field(ALIAS_B)
    private Set<String> asAliasB;

    @Field(PUB_ID)
    private Set<String> asPubId;

    @Field(INTERACTION_XREFS)
    private Set<String> asInteractionXrefs;

    @Field(TAX_ID_A)
    private Set<String> asTaxIdA;

    @Field(TAX_ID_B)
    private Set<String> asTaxIdB;

    @Field(PUB_AUTHORS)
    private Set<String> asPubAuthors;

    @Field(PUB_FIRST_AUTHOR)
    private Set<String> asPubFirstAuthor;

    @Field(PUB_YEAR)
    private Integer asPubYear;

    @Field(TYPE)
    private Set<String> asType;

    @Field(INTERACTION_DETECTION_METHOD)
    private Set<String> asIDetectionMethod;

    @Field(HOST_ORGANISM)
    private Set<String> asHostOrganism;

    @Field(BIOROLE_A)
    private Set<String> asBioRoleA;

    @Field(BIOROLE_B)
    private Set<String> asBioRoleB;

    @Field(TYPE_A)
    private Set<String> asTypeA;

    @Field(TYPE_B)
    private Set<String> asTypeB;

    @Field(FEATURE_TYPE_A)
    private Set<String> asFeatureTypesA;

    @Field(FEATURE_TYPE_B)
    private Set<String> asFeatureTypesB;

    @Field(IDENTIFICATION_METHOD_A)
    private Set<String> asIdentificationMethodsA;

    @Field(IDENTIFICATION_METHOD_B)
    private Set<String> asIdentificationMethodsB;

    @Field(XREFS_A)
    private Set<String> asXrefsA;

    @Field(XREFS_B)
    private Set<String> asXrefsB;

    @Field(INTERACTION_IDS)
    private Set<String> asInteractionIds;

    @Field(SOURCE)
    private Set<String> asSource;

    @Field(EXPANSION_METHOD)
    private Set<String> asExpansionMethod;

    @Field(LAST_UPDATED)
    private Integer asUpdationDate;

    @Field(RELEASE_DATE)
    private Integer asReleaseDate;

    @Field(INTACT_MI_SCORE)
    private double asIntactMiscore;

    @Field(NEGATIVE)
    private boolean asNegative;

    @Field(PARAM)
    private boolean asParam;

    @Field(STC)
    private boolean asStoichiometry;

    @Field(MUTATION)
    private boolean asAffectedByMutation;

    @Field(MUTATION_A)
    private boolean asMutationA;

    @Field(MUTATION_B)
    private boolean asMutationB;

    @Field(ANNOTATIONS)
    private Set<String> asAnnotations;

    @Field(GENE_NAME_A)
    private String asGeneNameA;

    @Field(GENE_NAME_B)
    private String asGeneNameB;

    public void setAsIdA(Set<String> asIdA) {
        this.asIdA = asIdA;
    }

    public void setAsIdB(Set<String> asIdB) {
        this.asIdB = asIdB;
    }

    public void setAsAltidA(Set<String> asAltidA) {
        this.asAltidA = asAltidA;
    }

    public void setAsAltidB(Set<String> asAltidB) {
        this.asAltidB = asAltidB;
    }

    public void setAsAliasA(Set<String> asAliasA) {
        this.asAliasA = asAliasA;
    }

    public void setAsAliasB(Set<String> asAliasB) {
        this.asAliasB = asAliasB;
    }

    public void setAsPubId(Set<String> asPubId) {
        this.asPubId = asPubId;
    }

    public void setAsInteractionXrefs(Set<String> asInteractionXrefs) {
        this.asInteractionXrefs = asInteractionXrefs;
    }

    public void setAsTaxIdA(Set<String> asTaxIdA) {
        this.asTaxIdA = asTaxIdA;
    }

    public void setAsTaxIdB(Set<String> asTaxIdB) {
        this.asTaxIdB = asTaxIdB;
    }

    public void setAsPubAuthors(Set<String> asPubAuthors) {
        this.asPubAuthors = asPubAuthors;
    }

    public void setAsPubFirstAuthor(Set<String> asPubFirstAuthor) {
        this.asPubFirstAuthor = asPubFirstAuthor;
    }

    public void setAsPubYear(Integer asPubYear) {
        this.asPubYear = asPubYear;
    }

    public void setAsType(Set<String> asType) {
        this.asType = asType;
    }

    public void setAsIDetectionMethod(Set<String> asIDetectionMethod) {
        this.asIDetectionMethod = asIDetectionMethod;
    }

    public void setAsHostOrganism(Set<String> asHostOrganism) {
        this.asHostOrganism = asHostOrganism;
    }

    public void setAsBioRoleA(Set<String> asBioRoleA) {
        this.asBioRoleA = asBioRoleA;
    }

    public void setAsBioRoleB(Set<String> asBioRoleB) {
        this.asBioRoleB = asBioRoleB;
    }

    public void setAsTypeA(Set<String> asTypeA) {
        this.asTypeA = asTypeA;
    }

    public void setAsTypeB(Set<String> asTypeB) {
        this.asTypeB = asTypeB;
    }

    public void setAsFeatureTypesA(Set<String> asFeatureTypesA) {
        this.asFeatureTypesA = asFeatureTypesA;
    }

    public void setAsFeatureTypesB(Set<String> asFeatureTypesB) {
        this.asFeatureTypesB = asFeatureTypesB;
    }

    public void setAsIdentificationMethodsA(Set<String> asIdentificationMethodsA) {
        this.asIdentificationMethodsA = asIdentificationMethodsA;
    }

    public void setAsIdentificationMethodsB(Set<String> asIdentificationMethodsB) {
        this.asIdentificationMethodsB = asIdentificationMethodsB;
    }

    public void setAsXrefsA(Set<String> asXrefsA) {
        this.asXrefsA = asXrefsA;
    }

    public void setAsXrefsB(Set<String> asXrefsB) {
        this.asXrefsB = asXrefsB;
    }

    public void setAsInteractionIds(Set<String> asInteractionIds) {
        this.asInteractionIds = asInteractionIds;
    }

    public void setAsSource(Set<String> asSource) {
        this.asSource = asSource;
    }

    public void setAsExpansionMethod(Set<String> asExpansionMethod) {
        this.asExpansionMethod = asExpansionMethod;
    }

    public void setAsUpdationDate(Integer asUpdationDate) {
        this.asUpdationDate = asUpdationDate;
    }

    public void setAsReleaseDate(Integer asReleaseDate) {
        this.asReleaseDate = asReleaseDate;
    }

    public void setAsIntactMiscore(double asIntactMiscore) {
        this.asIntactMiscore = asIntactMiscore;
    }

    public void setAsNegative(boolean asNegative) {
        this.asNegative = asNegative;
    }

    public void setAsParam(boolean asParam) {
        this.asParam = asParam;
    }

    public void setAsStoichiometry(boolean asStoichiometry) {
        this.asStoichiometry = asStoichiometry;
    }

    public void setAsAffectedByMutation(boolean asAffectedByMutation) {
        this.asAffectedByMutation = asAffectedByMutation;
    }

    public void setAsMutationA(boolean asMutationA) {
        this.asMutationA = asMutationA;
    }

    public void setAsMutationB(boolean asMutationB) {
        this.asMutationB = asMutationB;
    }

    public void setAsAnnotations(Set<String> asAnnotations) {
        this.asAnnotations = asAnnotations;
    }

    public void setAsGeneNameA(String asGeneNameA) {
        this.asGeneNameA = asGeneNameA;
    }

    public void setAsGeneNameB(String asGeneNameB) {
        this.asGeneNameB = asGeneNameB;
    }
}
