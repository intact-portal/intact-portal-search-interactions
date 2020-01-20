package uk.ac.ebi.intact.search.interactions.model;

/**
 * Created by anjali on 10/07/18.
 */
public class SearchInteractionFields {

    /* Here Nomenclature is different from Interactor Index Fields.
     * Reason being A and B at the end of interactor fields implicitly tell you they are interactor*/

    public static final String BINARY_INTERACTION_ID = "binary_interaction_id";
    public static final String ID_A = "idA";
    public static final String ID_B = "idB";
    public static final String AC_A = "acA";
    public static final String AC_B = "acB";
    public static final String MOLECULE_A = "moleculeA";
    public static final String MOLECULE_B = "moleculeB";
    public static final String ALT_IDS_A = "altIdsA";
    public static final String ALT_IDS_B = "altIdsB";
    public static final String UNIQUE_ID_A = "uniqueIdA";
    public static final String UNIQUE_ID_B = "uniqueIdB";
    public static final String ALIASES_A = "aliasesA";
    public static final String ALIASES_B = "aliasesB";
    public static final String TAX_IDA = "taxIdA";
    public static final String TAX_IDB = "taxIdB";
    public static final String TYPE_A = "typeA";
    public static final String TYPE_B = "typeB";
    public static final String TYPE_A_STR = "typeA_str";
    public static final String TYPE_B_STR = "typeB_str";
    public static final String XREFS_A = "xrefsA";
    public static final String XREFS_B = "xrefsB";
    public static final String ANNOTATIONS_A = "annotationsA";
    public static final String ANNOTATIONS_B = "annotationsB";
    public static final String CHECKSUMS_A = "checksumsA";
    public static final String CHECKSUMS_B = "checksumsB";
    public static final String SPECIES_A = "speciesA";
    public static final String SPECIES_A_STR = "speciesA_str";
    public static final String SPECIES_B = "speciesB";
    public static final String SPECIES_B_STR = "speciesB_str";
    public static final String SPECIES_A_B = "speciesA_B";
    public static final String SPECIES_A_B_STR = "speciesA_B_str";

    //participants

    public static final String BIOLOGICAL_ROLE_A = "biological_role_A";
    public static final String BIOLOGICAL_ROLE_B = "biological_role_B";
    public static final String EXPERIMENTAL_ROLE_A = "experimental_role_A";
    public static final String EXPERIMENTAL_ROLE_B = "experimental_role_B";
    public static final String FEATURE_A = "feature_A";
    public static final String FEATURE_B = "feature_B";
    public static final String FEATURE_SHORTLABEL_A = "feature_shortlabel_A";
    public static final String FEATURE_SHORTLABEL_B = "feature_shortlabel_B";
    public static final String STOICHIOMETRY_A = "stoichiometry_A";
    public static final String STOICHIOMETRY_B = "stoichiometry_B";
    public static final String IDENTIFICATION_METHOD_A = "identification_method_A";
    public static final String IDENTIFICATION_METHOD_B = "identification_method_B";

    public static final String DETECTION_METHOD = "detection_method";
    public static final String DETECTION_METHOD_STR = "detection_method_str";
    public static final String PUBLICATION_AUTHORS = "publication_authors";
    public static final String FIRST_AUTHOR = "first_author";
    public static final String PUBLICATION_IDENTIFIERS = "publication_identifiers";
    public static final String SOURCE_DATABASE = "source_database";
    public static final String IDENTIFIERS = "identifiers";
    public static final String CONFIDENCE_VALUES = "confidence_values";
    public static final String INTACT_MISCORE = "intact_miscore";
    public static final String CONFIDENCE_VALUES_STR = "confidence_values_str";
    public static final String EXPANSION_METHOD = "expansion_method";
    public static final String XREFS = "xrefs";
    public static final String ALL_ANNOTATIONS = "all_annotations";
    public static final String ANNOTATIONS = "annotations";
    public static final String PARAMETERS = "parameters";
    public static final String CREATION_DATE = "creation_date";
    public static final String UPDATION_DATE = "updation_date";
    public static final String CHECKSUM = "checksum";
    public static final String NEGATIVE = "negative";
    public static final String TYPE = "type";
    public static final String TYPE_MI_IDENTIFIER = "type_mi_identifier";
    public static final String TYPE_MI_IDENTIFIER_STR = "type_mi_identifier_str";
    public static final String TYPE_STR = "type_str";
    public static final String HOST_ORGANISM = "host_organism";
    public static final String HOST_ORGANISM_STR = "host_organism_str";
    public static final String AC = "ac";
    public static final String AC_STR = "ac_str";
    public static final String EXPERIMENTAL_PREPARATIONS_A = "experimental_preparations_A";
    public static final String EXPERIMENTAL_PREPARATIONS_B = "experimental_preparations_B";
    public static final String TYPE_MI_A = "type_MI_A";
    public static final String TYPE_MI_A_STR = "type_MI_A_str";
    public static final String TYPE_MI_B = "type_MI_B";
    public static final String TYPE_MI_B_STR = "type_MI_B_str";
    public static final String INTERACTOR_TYPE_A_B = "interactor_type_A_B";
    public static final String INTERACTOR_TYPE_A_B_STR = "interactor_type_A_B_str";
    public static final String DISRUPTED_BY_MUTATION = "disrupted_by_mutation";
    public static final String DISRUPTED_BY_MUTATION_STR = "disrupted_by_mutation_str";
    public static final String MUTATION_A = "mutation_A";
    public static final String MUTATION_A_STR = "mutation_A_str";
    public static final String MUTATION_B = "mutation_B";
    public static final String MUTATION_B_STR = "mutation_B_str";
    public static final String FEATURE_COUNT = "feature_count";


    public static final String RELEASE_DATE = "release_date";

    public static final String COUNT = "count";

    public static final String DEFAULT = "default"; //Copy field for general search
}
