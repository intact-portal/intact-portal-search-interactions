package uk.ac.ebi.intact.search.interactions.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum AdvancedSearchInteractionFields {

    ALT_ID_A(MiqlFieldConstants.ALTID_A, AdvancedSearchFieldConstants.ALTID_A),
    ALT_ID_B(MiqlFieldConstants.ALTID_B, AdvancedSearchFieldConstants.ALTID_B),
    AS_ALIAS(MiqlFieldConstants.ALIAS, AdvancedSearchFieldConstants.ALIAS),
    AS_ALIAS_A(MiqlFieldConstants.ALIAS_A, AdvancedSearchFieldConstants.ALIAS_A),
    AS_ALIAS_B(MiqlFieldConstants.ALIAS_B, AdvancedSearchFieldConstants.ALIAS_B),
    AS_IDENTIFIER(MiqlFieldConstants.IDENTIFIER, AdvancedSearchFieldConstants.IDENTIFIER),
    AS_PUB_ID(MiqlFieldConstants.PUB_ID, AdvancedSearchFieldConstants.PUB_ID),
    AS_INTERACTION_XREFS(MiqlFieldConstants.INTERACTION_XREFS, AdvancedSearchFieldConstants.INTERACTION_XREFS),
    AS_TAX_ID_A(MiqlFieldConstants.TAX_ID_A, AdvancedSearchFieldConstants.TAX_ID_A),
    AS_TAX_ID_B(MiqlFieldConstants.TAX_ID_B, AdvancedSearchFieldConstants.TAX_ID_B),
    AS_SPECIES(MiqlFieldConstants.SPECIES, AdvancedSearchFieldConstants.SPECIES),
    AS_ID_A(MiqlFieldConstants.ID_A, AdvancedSearchFieldConstants.ID_A),
    AS_ID_B(MiqlFieldConstants.ID_B, AdvancedSearchFieldConstants.ID_B),
    AS_ID(MiqlFieldConstants.ID, AdvancedSearchFieldConstants.ID),
    AS_PUB_AUTHORS(MiqlFieldConstants.PUB_AUTHORS, AdvancedSearchFieldConstants.PUB_AUTHORS),
    AS_PUB_FIRST_AUTHOR(MiqlFieldConstants.PUB_FIRST_AUTHOR, AdvancedSearchFieldConstants.PUB_FIRST_AUTHOR),
    AS_PUB_YEAR(MiqlFieldConstants.PUB_YEAR, AdvancedSearchFieldConstants.PUB_YEAR),
    AS_TYPE(MiqlFieldConstants.TYPE, AdvancedSearchFieldConstants.TYPE),
    AS_HOST_ORGANISM(MiqlFieldConstants.HOST_ORGANISM, AdvancedSearchFieldConstants.HOST_ORGANISM),
    AS_INTERACTION_DETECTION_METHOD(MiqlFieldConstants.INTERACTION_DETECTION_METHOD, AdvancedSearchFieldConstants.INTERACTION_DETECTION_METHOD),
    AS_BIOROLE_A(MiqlFieldConstants.BIOROLE_A, AdvancedSearchFieldConstants.BIOROLE_A),
    AS_BIOROLE_B(MiqlFieldConstants.BIOROLE_B, AdvancedSearchFieldConstants.BIOROLE_B),
    AS_BIOROLE(MiqlFieldConstants.BIOROLE, AdvancedSearchFieldConstants.BIOROLE),
    AS_TYPE_A(MiqlFieldConstants.TYPE_A, AdvancedSearchFieldConstants.TYPE_A),
    AS_TYPE_B(MiqlFieldConstants.TYPE_B, AdvancedSearchFieldConstants.TYPE_B),
    AS_INTERACTOR_TYPE(MiqlFieldConstants.INTERACTOR_TYPE, AdvancedSearchFieldConstants.INTERACTOR_TYPE),
    AS_FEATURE_TYPE_A(MiqlFieldConstants.FEATURE_TYPE_A, AdvancedSearchFieldConstants.FEATURE_TYPE_A),
    AS_FEATURE_TYPE_B(MiqlFieldConstants.FEATURE_TYPE_B, AdvancedSearchFieldConstants.FEATURE_TYPE_B),
    AS_FEATURE_TYPE(MiqlFieldConstants.FEATURE_TYPE, AdvancedSearchFieldConstants.FEATURE_TYPE),
    AS_IDENTIFICATION_METHOD_A(MiqlFieldConstants.IDENTIFICATION_METHOD_A, AdvancedSearchFieldConstants.IDENTIFICATION_METHOD_A),
    AS_IDENTIFICATION_METHOD_B(MiqlFieldConstants.IDENTIFICATION_METHOD_B, AdvancedSearchFieldConstants.IDENTIFICATION_METHOD_B),
    AS_IDENTIFICATION_METHOD(MiqlFieldConstants.IDENTIFICATION_METHOD, AdvancedSearchFieldConstants.IDENTIFICATION_METHOD),
    AS_XREFS_A(MiqlFieldConstants.XREFS_A, AdvancedSearchFieldConstants.XREFS_A),
    AS_XREFS_B(MiqlFieldConstants.XREFS_B, AdvancedSearchFieldConstants.XREFS_B),
    AS_INTERACTOR_XREFS(MiqlFieldConstants.INTERACTOR_XREFS, AdvancedSearchFieldConstants.INTERACTOR_XREFS),
    AS_INTERACTION_IDS(MiqlFieldConstants.INTERACTION_IDS, AdvancedSearchFieldConstants.INTERACTION_IDS),
    AS_EXPANSION_METHOD(MiqlFieldConstants.EXPANSION_METHOD, AdvancedSearchFieldConstants.EXPANSION_METHOD),
    AS_SOURCE(MiqlFieldConstants.SOURCE, AdvancedSearchFieldConstants.SOURCE),
    AS_LAST_UPDATED(MiqlFieldConstants.LAST_UPDATED, AdvancedSearchFieldConstants.LAST_UPDATED),
    AS_RELEASE_DATE(MiqlFieldConstants.RELEASE_DATE, AdvancedSearchFieldConstants.RELEASE_DATE),
    AS_INTACT_MI_SCORE(MiqlFieldConstants.INTACT_MI_SCORE, AdvancedSearchFieldConstants.INTACT_MI_SCORE),
    AS_NEGATIVE(MiqlFieldConstants.NEGATIVE, AdvancedSearchFieldConstants.NEGATIVE),
    AS_PARAM(MiqlFieldConstants.PARAM, AdvancedSearchFieldConstants.PARAM),
    AS_STC(MiqlFieldConstants.STC, AdvancedSearchFieldConstants.STC),
    AS_MUTATION(MiqlFieldConstants.MUTATION, AdvancedSearchFieldConstants.MUTATION),
    AS_MUTATION_A(MiqlFieldConstants.MUTATION_A, AdvancedSearchFieldConstants.MUTATION_A),
    AS_MUTATION_B(MiqlFieldConstants.MUTATION_B, AdvancedSearchFieldConstants.MUTATION_B),
    AS_ANNOTATIONS(MiqlFieldConstants.ANNOTATIONS, AdvancedSearchFieldConstants.ANNOTATIONS),
    AS_GENE_NAME_A(MiqlFieldConstants.GENE_NAME_A, AdvancedSearchFieldConstants.GENE_NAME_A),
    AS_GENE_NAME_B(MiqlFieldConstants.GENE_NAME_B, AdvancedSearchFieldConstants.GENE_NAME_B),
    AS_GENE_NAME(MiqlFieldConstants.GENE_NAME, AdvancedSearchFieldConstants.GENE_NAME);

    AdvancedSearchInteractionFields(String miqlFieldName, String asFieldName) {
        this.miqlFieldName = miqlFieldName;
        this.asFieldName = asFieldName;
    }

    private String miqlFieldName;
    private String asFieldName;
    public static String SOLR_FIELD_VALUE_SEPARATOR = "SOLR_FIELD_VALUE_SEPARATOR";
    public static String SOLR_FIELD_VALUE_SEPARATOR_VALUE = ":";

    public static final Map<String, String> MIQL_AS_MAP =
            Arrays.stream(AdvancedSearchInteractionFields.values())
                    .collect(Collectors.toMap(AdvancedSearchInteractionFields::getMiqlFieldName, e -> e.getAsFieldName()));

    public String getMiqlFieldName() {
        return miqlFieldName + SOLR_FIELD_VALUE_SEPARATOR_VALUE;
    }

    public String getAsFieldName() {
        return asFieldName + SOLR_FIELD_VALUE_SEPARATOR;
    }

    public static class MiqlFieldConstants {
        public static final String ID_A = "idA";
        public static final String ID_B = "idB";
        public static final String ID = "id";
        public static final String ALTID_A = "altidA";
        public static final String ALTID_B = "altidB";
        public static final String ALIAS_A = "aliasA";
        public static final String ALIAS_B = "aliasB";
        public static final String ALIAS = "alias";
        public static final String IDENTIFIER = "identifier";
        public static final String PUB_ID = "pubid";
        public static final String INTERACTION_XREFS = "xref";
        public static final String TAX_ID_A = "taxidA";
        public static final String TAX_ID_B = "taxidB";
        public static final String SPECIES = "species";
        public static final String PUB_AUTHORS = "pubauthors";
        public static final String PUB_FIRST_AUTHOR = "pubauth";
        public static final String PUB_YEAR = "pubyear";
        public static final String TYPE = "type";
        public static final String HOST_ORGANISM = "taxidHost";
        public static final String INTERACTION_DETECTION_METHOD = "detmethod";
        public static final String BIOROLE_A = "pbioroleA";
        public static final String BIOROLE_B = "pbioroleB";
        public static final String BIOROLE = "pbiorole";
        public static final String TYPE_A = "ptypeA";
        public static final String TYPE_B = "ptypeB";
        public static final String INTERACTOR_TYPE = "ptype";
        public static final String FEATURE_TYPE_A = "ftypeA";
        public static final String FEATURE_TYPE_B = "ftypeB";
        public static final String FEATURE_TYPE = "ftype";
        public static final String IDENTIFICATION_METHOD_A = "pmethodA";
        public static final String IDENTIFICATION_METHOD_B = "pmethodB";
        public static final String IDENTIFICATION_METHOD = "pmethod";
        public static final String XREFS_A = "pxrefA";
        public static final String XREFS_B = "pxrefB";
        public static final String INTERACTOR_XREFS = "pxref";
        public static final String INTERACTION_IDS = "interaction_id";
        public static final String EXPANSION_METHOD = "complex";
        public static final String SOURCE = "source";
        public static final String LAST_UPDATED = "udate";
        public static final String RELEASE_DATE = "rdate";
        public static final String INTACT_MI_SCORE = "intact-miscore";
        public static final String NEGATIVE = "negative";
        public static final String PARAM = "param";
        public static final String STC = "stc";
        public static final String MUTATION = "mutation";
        public static final String MUTATION_A = "mutationA";
        public static final String MUTATION_B = "mutationB";
        public static final String ANNOTATIONS = "annot";
        public static final String GENE_NAME = "geneName";
        public static final String GENE_NAME_A = "geneNameA";
        public static final String GENE_NAME_B = "geneNameB";
    }

    public static class AdvancedSearchFieldConstants {
        public static final String ID_A = "as_idA";
        public static final String ID_B = "as_idB";
        public static final String ID = "as_id";
        public static final String ALTID_A = "as_altidA";
        public static final String ALTID_B = "as_altidB";
        public static final String ALIAS_A = "as_aliasA";
        public static final String ALIAS_B = "as_aliasB";
        public static final String ALIAS = "as_alias";
        public static final String IDENTIFIER = "as_identifier";
        public static final String PUB_ID = "as_pubid";
        public static final String INTERACTION_XREFS = "as_xref";
        public static final String TAX_ID_A = "as_taxidA";
        public static final String TAX_ID_B = "as_taxidB";
        public static final String SPECIES = "as_species";
        public static final String PUB_AUTHORS = "as_pubauthors";
        public static final String PUB_FIRST_AUTHOR = "as_pubauth";
        public static final String PUB_YEAR = "as_pubyear";
        public static final String TYPE = "as_type";
        public static final String HOST_ORGANISM = "as_taxidHost";
        public static final String INTERACTION_DETECTION_METHOD = "as_detmethod";
        public static final String BIOROLE_A = "as_pbioroleA";
        public static final String BIOROLE_B = "as_pbioroleB";
        public static final String BIOROLE = "as_pbiorole";
        public static final String TYPE_A = "as_ptypeA";
        public static final String TYPE_B = "as_ptypeB";
        public static final String INTERACTOR_TYPE = "as_ptype";
        public static final String FEATURE_TYPE_A = "as_ftypeA";
        public static final String FEATURE_TYPE_B = "as_ftypeB";
        public static final String FEATURE_TYPE = "as_ftype";
        public static final String IDENTIFICATION_METHOD_A = "as_pmethodA";
        public static final String IDENTIFICATION_METHOD_B = "as_pmethodB";
        public static final String IDENTIFICATION_METHOD = "as_pmethod";
        public static final String XREFS_A = "as_pxrefA";
        public static final String XREFS_B = "as_pxrefB";
        public static final String INTERACTOR_XREFS = "as_pxref";
        public static final String INTERACTION_IDS = "as_interaction_id";
        public static final String EXPANSION_METHOD = "as_complex";
        public static final String SOURCE = "as_source";
        public static final String LAST_UPDATED = "as_udate";
        public static final String RELEASE_DATE = "as_rdate";
        public static final String INTACT_MI_SCORE = "as_intact-miscore";
        public static final String NEGATIVE = "as_negative";
        public static final String PARAM = "as_param";
        public static final String STC = "as_stc";
        public static final String MUTATION = "as_mutation";
        public static final String MUTATION_A = "as_mutationA";
        public static final String MUTATION_B = "as_mutationB";
        public static final String ANNOTATIONS = "as_annot";
        public static final String GENE_NAME = "as_geneName";
        public static final String GENE_NAME_A = "as_geneNameA";
        public static final String GENE_NAME_B = "as_geneNameB";
    }
}
