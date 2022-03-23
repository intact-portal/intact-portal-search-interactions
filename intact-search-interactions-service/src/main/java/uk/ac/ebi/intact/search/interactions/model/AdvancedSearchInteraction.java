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

    public Set<String> getAsIdA() {
        return asIdA;
    }

    public void setAsIdA(Set<String> asIdA) {
        this.asIdA = asIdA;
    }

    public Set<String> getAsIdB() {
        return asIdB;
    }

    public void setAsIdB(Set<String> asIdB) {
        this.asIdB = asIdB;
    }

    public Set<String> getAsAltidA() {
        return asAltidA;
    }

    public void setAsAltidA(Set<String> asAltidA) {
        this.asAltidA = asAltidA;
    }

    public Set<String> getAsAltidB() {
        return asAltidB;
    }

    public void setAsAltidB(Set<String> asAltidB) {
        this.asAltidB = asAltidB;
    }

    public Set<String> getAsAliasA() {
        return asAliasA;
    }

    public void setAsAliasA(Set<String> asAliasA) {
        this.asAliasA = asAliasA;
    }

    public Set<String> getAsAliasB() {
        return asAliasB;
    }

    public void setAsAliasB(Set<String> asAliasB) {
        this.asAliasB = asAliasB;
    }

    public Set<String> getAsPubId() {
        return asPubId;
    }

    public void setAsPubId(Set<String> asPubId) {
        this.asPubId = asPubId;
    }

    public Set<String> getAsInteractionXrefs() {
        return asInteractionXrefs;
    }

    public void setAsInteractionXrefs(Set<String> asInteractionXrefs) {
        this.asInteractionXrefs = asInteractionXrefs;
    }

    public Set<String> getAsTaxIdA() {
        return asTaxIdA;
    }

    public void setAsTaxIdA(Set<String> asTaxIdA) {
        this.asTaxIdA = asTaxIdA;
    }

    public Set<String> getAsTaxIdB() {
        return asTaxIdB;
    }

    public void setAsTaxIdB(Set<String> asTaxIdB) {
        this.asTaxIdB = asTaxIdB;
    }

    public Set<String> getAsPubAuthors() {
        return asPubAuthors;
    }

    public void setAsPubAuthors(Set<String> asPubAuthors) {
        this.asPubAuthors = asPubAuthors;
    }

    public Set<String> getAsPubFirstAuthor() {
        return asPubFirstAuthor;
    }

    public void setAsPubFirstAuthor(Set<String> asPubFirstAuthor) {
        this.asPubFirstAuthor = asPubFirstAuthor;
    }

    public Integer getAsPubYear() {
        return asPubYear;
    }

    public void setAsPubYear(Integer asPubYear) {
        this.asPubYear = asPubYear;
    }

    public Set<String> getAsType() {
        return asType;
    }

    public void setAsType(Set<String> asType) {
        this.asType = asType;
    }

    public Set<String> getAsIDetectionMethod() {
        return asIDetectionMethod;
    }

    public void setAsIDetectionMethod(Set<String> asIDetectionMethod) {
        this.asIDetectionMethod = asIDetectionMethod;
    }

    public Set<String> getAsHostOrganism() {
        return asHostOrganism;
    }

    public void setAsHostOrganism(Set<String> asHostOrganism) {
        this.asHostOrganism = asHostOrganism;
    }

    public Set<String> getAsBioRoleA() {
        return asBioRoleA;
    }

    public void setAsBioRoleA(Set<String> asBioRoleA) {
        this.asBioRoleA = asBioRoleA;
    }

    public Set<String> getAsBioRoleB() {
        return asBioRoleB;
    }

    public void setAsBioRoleB(Set<String> asBioRoleB) {
        this.asBioRoleB = asBioRoleB;
    }

    public Set<String> getAsTypeA() {
        return asTypeA;
    }

    public void setAsTypeA(Set<String> asTypeA) {
        this.asTypeA = asTypeA;
    }

    public Set<String> getAsTypeB() {
        return asTypeB;
    }

    public void setAsTypeB(Set<String> asTypeB) {
        this.asTypeB = asTypeB;
    }

    public Set<String> getAsFeatureTypesA() {
        return asFeatureTypesA;
    }

    public void setAsFeatureTypesA(Set<String> asFeatureTypesA) {
        this.asFeatureTypesA = asFeatureTypesA;
    }

    public Set<String> getAsFeatureTypesB() {
        return asFeatureTypesB;
    }

    public void setAsFeatureTypesB(Set<String> asFeatureTypesB) {
        this.asFeatureTypesB = asFeatureTypesB;
    }

    public Set<String> getAsIdentificationMethodsA() {
        return asIdentificationMethodsA;
    }

    public void setAsIdentificationMethodsA(Set<String> asIdentificationMethodsA) {
        this.asIdentificationMethodsA = asIdentificationMethodsA;
    }

    public Set<String> getAsIdentificationMethodsB() {
        return asIdentificationMethodsB;
    }

    public void setAsIdentificationMethodsB(Set<String> asIdentificationMethodsB) {
        this.asIdentificationMethodsB = asIdentificationMethodsB;
    }

    public Set<String> getAsXrefsA() {
        return asXrefsA;
    }

    public void setAsXrefsA(Set<String> asXrefsA) {
        this.asXrefsA = asXrefsA;
    }

    public Set<String> getAsXrefsB() {
        return asXrefsB;
    }

    public void setAsXrefsB(Set<String> asXrefsB) {
        this.asXrefsB = asXrefsB;
    }

    public Set<String> getAsInteractionIds() {
        return asInteractionIds;
    }

    public void setAsInteractionIds(Set<String> asInteractionIds) {
        this.asInteractionIds = asInteractionIds;
    }

    public Set<String> getAsSource() {
        return asSource;
    }

    public void setAsSource(Set<String> asSource) {
        this.asSource = asSource;
    }

    public Set<String> getAsExpansionMethod() {
        return asExpansionMethod;
    }

    public void setAsExpansionMethod(Set<String> asExpansionMethod) {
        this.asExpansionMethod = asExpansionMethod;
    }

    public Integer getAsUpdationDate() {
        return asUpdationDate;
    }

    public void setAsUpdationDate(Integer asUpdationDate) {
        this.asUpdationDate = asUpdationDate;
    }

    public double getAsIntactMiscore() {
        return asIntactMiscore;
    }

    public void setAsIntactMiscore(double asIntactMiscore) {
        this.asIntactMiscore = asIntactMiscore;
    }

    public Integer getAsReleaseDate() {
        return asReleaseDate;
    }

    public void setAsReleaseDate(Integer asReleaseDate) {
        this.asReleaseDate = asReleaseDate;
    }

    public boolean isAsNegative() {
        return asNegative;
    }

    public void setAsNegative(boolean asNegative) {
        this.asNegative = asNegative;
    }

    public boolean isAsParam() {
        return asParam;
    }

    public void setAsParam(boolean asParam) {
        this.asParam = asParam;
    }

    public boolean isAsStoichiometry() {
        return asStoichiometry;
    }

    public void setAsStoichiometry(boolean asStoichiometry) {
        this.asStoichiometry = asStoichiometry;
    }

    public boolean isAsAffectedByMutation() {
        return asAffectedByMutation;
    }

    public void setAsAffectedByMutation(boolean asAffectedByMutation) {
        this.asAffectedByMutation = asAffectedByMutation;
    }

    public boolean isAsMutationA() {
        return asMutationA;
    }

    public void setAsMutationA(boolean asMutationA) {
        this.asMutationA = asMutationA;
    }

    public boolean isAsMutationB() {
        return asMutationB;
    }

    public void setAsMutationB(boolean asMutationB) {
        this.asMutationB = asMutationB;
    }
}
