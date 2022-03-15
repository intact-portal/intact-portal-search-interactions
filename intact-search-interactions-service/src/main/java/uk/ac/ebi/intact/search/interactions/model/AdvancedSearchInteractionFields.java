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
    AS_PART_METHOD_A(MiqlFieldConstants.PART_METHOD_A, AdvancedSearchFieldConstants.PART_METHOD_A),
    AS_PART_METHOD_B(MiqlFieldConstants.PART_METHOD_B, AdvancedSearchFieldConstants.PART_METHOD_B),
    AS_PART_METHOD(MiqlFieldConstants.PART_METHOD, AdvancedSearchFieldConstants.PART_METHOD);

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
        public static final String INTERACTION_XREFS = "interaction_id";
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
        public static final String PART_METHOD_A = "pmethodA";
        public static final String PART_METHOD_B = "pmethodB";
        public static final String PART_METHOD = "pmethod";
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
        public static final String INTERACTION_XREFS = "as_interaction_id";
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
        public static final String PART_METHOD_A = "as_pmethodA";
        public static final String PART_METHOD_B = "as_pmethodB";
        public static final String PART_METHOD = "as_pmethod";
    }
}
