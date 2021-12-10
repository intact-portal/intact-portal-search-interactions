package uk.ac.ebi.intact.search.interactions.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum AdvancedSearchInteractionFields {

    AS_ID_A(MiqlFieldConstants.ID_A, AdvancedSearchFieldConstants.ID_A),
    AS_ID_B(MiqlFieldConstants.ID_B, AdvancedSearchFieldConstants.ID_B),
    ALT_ID_A(MiqlFieldConstants.ALTID_A, AdvancedSearchFieldConstants.ALTID_A),
    ALT_ID_B(MiqlFieldConstants.ALTID_B, AdvancedSearchFieldConstants.ALTID_B),
    AS_ID(MiqlFieldConstants.ID, AdvancedSearchFieldConstants.ID);

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
    }

    public static class AdvancedSearchFieldConstants {
        public static final String ID_A = "as_idA";
        public static final String ID_B = "as_idB";
        public static final String ID = "as_id";
        public static final String ALTID_A = "as_altidA";
        public static final String ALTID_B = "as_altidB";
    }
}
