package uk.ac.ebi.intact.search.interactions.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum AdvancedSearchInteractionFields {

    AS_ID_A(MiqlFieldConstants.ID_A,AdvancedSearchFieldConstants.ID_A);

    AdvancedSearchInteractionFields(String miqlFieldName, String asFieldName){
        this.miqlFieldName=miqlFieldName;
        this.asFieldName=asFieldName;
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
    }

    public static class AdvancedSearchFieldConstants {
        public static final String ID_A = "as_idA";
    }
}
