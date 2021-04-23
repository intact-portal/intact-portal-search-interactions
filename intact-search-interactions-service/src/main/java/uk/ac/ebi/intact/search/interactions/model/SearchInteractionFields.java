package uk.ac.ebi.intact.search.interactions.model;

/**
 * Created by anjali on 10/07/18.
 */
public class SearchInteractionFields {

    /* Here Nomenclature is different from Interactor Index Fields.
     * Reason being A and B at the end of interactor fields implicitly tell you they are interactor*/

    public static final String BINARY_INTERACTION_ID = "binary_interaction_id";
    public static final String DOCUMENT_TYPE = "document_type";
    public static final String ID_A = "idA";
    public static final String ID_B = "idB";
    public static final String AC_A = "acA";
    public static final String AC_B = "acB";
    public static final String AC_A_S = "acA_s";
    public static final String AC_B_S = "acB_s";
    public static final String MOLECULE_A = "moleculeA";
    public static final String MOLECULE_B = "moleculeB";
    public static final String INTACT_NAME_A = "intactNameA";
    public static final String INTACT_NAME_B = "intactNameB";
    public static final String ALT_IDS_A = "altIdsA";
    public static final String ALT_IDS_B = "altIdsB";
    public static final String DESCRIPTION_A = "descriptionA";
    public static final String DESCRIPTION_B = "descriptionB";
    public static final String UNIQUE_ID_A = "uniqueIdA";
    public static final String UNIQUE_ID_B = "uniqueIdB";
    public static final String ALIASES_A = "aliasesA";
    public static final String ALIASES_B = "aliasesB";
    public static final String TAX_IDA = "taxIdA";
    public static final String TAX_IDB = "taxIdB";
    public static final String INTRA_TAX_ID = "intra_taxId";
    public static final String TYPE_A = "typeA";
    public static final String TYPE_B = "typeB";
    public static final String XREFS_A = "xrefsA";
    public static final String XREFS_B = "xrefsB";
    public static final String ANNOTATIONS_A = "annotationsA";
    public static final String ANNOTATIONS_B = "annotationsB";
    public static final String CHECKSUMS_A = "checksumsA";
    public static final String CHECKSUMS_B = "checksumsB";
    public static final String SPECIES_A = "speciesA";
    // CopyField
    public static final String SPECIES_A_S = "speciesA_s";
    public static final String SPECIES_B = "speciesB";
    // CopyField
    public static final String SPECIES_B_S = "speciesB_s";
    // CopyField
    public static final String SPECIES_A_B_STR = "speciesA_B_str";
    public static final String INTRA_SPECIES = "intra_species";

    //participants

    public static final String BIOLOGICAL_ROLE_A = "biological_role_A";
    public static final String BIOLOGICAL_ROLE_B = "biological_role_B";
    public static final String EXPERIMENTAL_ROLE_A = "experimental_role_A";
    public static final String EXPERIMENTAL_ROLE_B = "experimental_role_B";
    public static final String BIOLOGICAL_ROLE_MI_IDENTIFIER_A = "biological_role_mi_identifier_A";
    public static final String BIOLOGICAL_ROLE_MI_IDENTIFIER_B = "biological_role_mi_identifier_B";
    public static final String EXPERIMENTAL_ROLE_MI_IDENTIFIER_A = "experimental_role_mi_identifier_A";
    public static final String EXPERIMENTAL_ROLE_MI_IDENTIFIER_B = "experimental_role_mi_identifier_B";
    public static final String FEATURE_A = "feature_A";
    public static final String FEATURE_B = "feature_B";
    public static final String FEATURE_SHORTLABEL_A = "feature_shortlabel_A";
    public static final String FEATURE_SHORTLABEL_B = "feature_shortlabel_B";
    public static final String FEATURE_TYPE_A = "feature_type_A";
    public static final String FEATURE_TYPE_B = "feature_type_B";
    public static final String STOICHIOMETRY_A = "stoichiometry_A";
    public static final String STOICHIOMETRY_B = "stoichiometry_B";
    public static final String IDENTIFICATION_METHODS_A = "identification_method_A";
    public static final String IDENTIFICATION_METHODS_B = "identification_method_B";
    public static final String IDENTIFICATION_METHOD_MI_IDENTIFIERS_A = "identification_method_mi_identifier_A";
    public static final String IDENTIFICATION_METHOD_MI_IDENTIFIERS_B = "identification_method_mi_identifier_B";

    public static final String DETECTION_METHOD = "detection_method";
    public static final String DETECTION_METHOD_S = "detection_method_s";
    public static final String DETECTION_METHOD_MI_IDENTIFIER = "detection_method_mi_identifier";
    public static final String PUBLICATION_AUTHORS = "publication_authors";
    public static final String FIRST_AUTHOR = "first_author";
    public static final String PUBLICATION_IDENTIFIERS = "publication_identifiers";
    public static final String PUBLICATION_PUBMED_IDENTIFIER = "publication_id";
    public static final String SOURCE_DATABASE = "source_database";
    public static final String IDENTIFIERS = "identifiers";
    public static final String CONFIDENCE_VALUES = "confidence_values";
    public static final String INTACT_MISCORE = "intact_miscore";
    public static final String EXPANSION_METHOD = "expansion_method";
    public static final String XREFS = "xrefs";
    public static final String ALL_ANNOTATIONS = "all_annotations";
    public static final String ANNOTATIONS = "annotations";
    public static final String PARAMETERS = "parameters";
    public static final String PARAMETER_TYPES = "parameter_types";
    public static final String CREATION_DATE = "creation_date";
    public static final String UPDATION_DATE = "updation_date";
    public static final String CHECKSUMS = "checksum";
    public static final String NEGATIVE = "negative";
    public static final String TYPE = "type";
    public static final String TYPE_MI_IDENTIFIER = "type_mi_identifier";
    public static final String TYPE_S = "type_s";
    public static final String HOST_ORGANISM = "host_organism";
    public static final String HOST_ORGANISM_TAX_ID = "host_organism_taxId";
    public static final String HOST_ORGANISM_S = "host_organism_s";
    public static final String AC = "ac";
    public static final String AC_S = "ac_s";
    public static final String EXPERIMENTAL_PREPARATIONS_A = "experimental_preparations_A";
    public static final String EXPERIMENTAL_PREPARATIONS_B = "experimental_preparations_B";
    public static final String TYPE_MI_A = "type_MI_A";
    public static final String TYPE_MI_B = "type_MI_B";
    public static final String TYPE_A_B_STR = "typeA_B_str";
    public static final String DISRUPTED_BY_MUTATION = "disrupted_by_mutation";
    public static final String MUTATION_A = "mutation_A";
    public static final String MUTATION_B = "mutation_B";
    public static final String FEATURE_COUNT = "feature_count";
    public static final String PUBLICATION_ANNOTATIONS = "publication_annotations";
    public static final String FEATURE_RANGES_A = "feature_ranges_A";
    public static final String FEATURE_RANGES_B = "feature_ranges_B";


    public static final String RELEASE_DATE = "release_date";

    public static final String COUNT = "count";

    public static final String DEFAULT = "default"; //Copy field for general search
    public static final String INTERACTOR_DEFAULT = "interactor_default"; //Copy field for interactor search
    public static final String INTERACTOR_IDENTIFIERS = "interactor_identifiers";

    /* Fields related with the styling of the network and other visual components */

    // Taxonomy inter intra species
    public static final String TAX_IDA_STYLED = "taxIdA_styled";
    public static final String TAX_IDB_STYLED = "taxIdB_styled";
    // CopyField
    public static final String TAX_ID_A_B_STYLED = "taxId_A_B_styled";
    public static final String INTRA_TAX_ID_STYLED = "intra_taxId_styled";

    // Interaction type
    public static final String TYPE_MI_IDENTIFIER_STYLED = "type_mi_identifier_styled";

    // Interactor type
    public static final String TYPE_MI_A_STYLED = "type_MI_A_styled";
    public static final String TYPE_MI_B_STYLED = "type_MI_B_styled";
    // CopyField
    public static final String TYPE_MI_A_B_STYLED = "type_MI_A_B_styled";


}