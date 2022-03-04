package uk.ac.ebi.intact.search.interactions.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum AdvancedSearchInteractionFields {

    AS_ID_A(MiqlFieldConstants.ID_A, AdvancedSearchFieldConstants.ID_A),
    AS_ID_B(MiqlFieldConstants.ID_B, AdvancedSearchFieldConstants.ID_B),
    ALT_ID_A(MiqlFieldConstants.ALTID_A, AdvancedSearchFieldConstants.ALTID_A),
    ALT_ID_B(MiqlFieldConstants.ALTID_B, AdvancedSearchFieldConstants.ALTID_B),
    AS_ID(MiqlFieldConstants.ID, AdvancedSearchFieldConstants.ID),
    AS_ALIAS(MiqlFieldConstants.ALIAS, AdvancedSearchFieldConstants.ALIAS),
    AS_ALIAS_A(MiqlFieldConstants.ALIAS_A, AdvancedSearchFieldConstants.ALIAS_A),
    AS_ALIAS_B(MiqlFieldConstants.ALIAS_B, AdvancedSearchFieldConstants.ALIAS_B);

    AdvancedSearchInteractionFields(String miqlFieldName, String asFieldName) {
        this.miqlFieldName = miqlFieldName;
        this.asFieldName = asFieldName;
    }

    private String miqlFieldName;
    private String asFieldName;

    public static final Map<String, String> MIQL_AS_MAP =
            Arrays.stream(AdvancedSearchInteractionFields.values())
                    .collect(Collectors.toMap(AdvancedSearchInteractionFields::getMiqlFieldName, e -> e.getAsFieldName()));

    public String getMiqlFieldName() {
        return miqlFieldName;
    }

    public String getAsFieldName() {
        return asFieldName;
    }

    public static class MiqlFieldConstants {
        public static final String ID_A = "idA";
        public static final String ID_B = "idB";
        public static final String ID = "id";
        public static final String ALTID_A = "altidA";
        public static final String ALTID_B = "altidB";
        public static final String ALIAS_A ="aliasA";
        public static final String ALIAS_B ="aliasB";
        public static final String ALIAS ="alias";
    }

    public static class AdvancedSearchFieldConstants {
        public static final String ID_A = "as_idA";
        public static final String ID_B = "as_idB";
        public static final String ID = "as_id";
        public static final String ALTID_A = "as_altidA";
        public static final String ALTID_B = "as_altidB";
        public static final String ALIAS_A ="as_aliasA";
        public static final String ALIAS_B ="as_aliasB";
        public static final String ALIAS ="as_alias";
    }
}
