package uk.ac.ebi.intact.search.interactions.model;

public class SearchChildInteractorFields {

    public static final String DOCUMENT_ID = "id";// this is needed in nested documents
    public static final String DOCUMENT_TYPE = "document_type";// this is needed in nested documents

    public static final String INTERACTOR_AC = "interactor_ac";
    public static final String INTERACTOR_AC_STR = "interactor_ac_str";

    public static final String INTERACTOR_NAME = "interactor_name";
    public static final String INTERACTOR_INTACT_NAME = "interactor_intact_name";
    public static final String INTERACTOR_PREFERRED_ID = "interactor_preferred_id";

    public static final String INTERACTOR_DESCRIPTION = "interactor_description";

    public static final String INTERACTOR_ALIAS = "interactor_alias";

    public static final String INTERACTOR_ALT_IDS = "interactor_alt_ids";

    public static final String INTERACTOR_TAX_ID = "interactor_tax_id";

    public static final String INTERACTOR_XREFS = "interactor_xrefs";

    public static final String INTERACTOR_TYPE = "interactor_type";
    public static final String INTERACTOR_TYPE_STR = "interactor_type_str";
    public static final String INTERACTOR_TYPE_MI_IDENTIFIER = "interactor_type_mi_identifier";

    public static final String INTERACTOR_SPECIES_NAME = "interactor_species_name";
    public static final String INTERACTOR_SPECIES_NAME_STR = "interactor_species_name_str";

    public static final String INTERACTION_COUNT = "interaction_count";

    public static final String DEFAULT = "default"; //Copy field for general search

    public static final String INTERACTOR_FEATURE_SHORTLABELS = "interactor_feature_shortlabels";

    /* Fields related with the styling of the network and other visual components */
    public static final String INTERACTOR_TAX_ID_STYLED = "interactor_tax_id_styled";
    public static final String INTERACTOR_TYPE_MI_IDENTIFIER_STYLED = "interactor_type_mi_identifier_styled";
}