package uk.ac.ebi.intact.search.interactions.utils;

import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.intact.search.interactions.model.AdvancedSearchInteractionFields;

import java.util.Arrays;
import java.util.Map;

public class AdvancedSearchInteractionUtility {

    private static String[] miqlKeys;// MIQL fields+":"
    private static String[] asValues;// Advanced search fields+":"

    public static String getAdvancedSearchQuery(String searchTerms) {

        Map<String, String> miqlAsMap = AdvancedSearchInteractionFields.MIQL_AS_MAP;
        if (miqlKeys == null) {
            miqlKeys = miqlAsMap.keySet().toArray(new String[0]);
            Arrays.stream(miqlKeys).forEach(e -> e = e + ":");
        }
        if (asValues == null) {
            asValues = miqlAsMap.values().toArray(new String[0]);
            Arrays.stream(asValues).forEach(e -> e = e + ":");
        }

        return "(" + StringUtils.replaceEach(searchTerms, miqlKeys, asValues) + ")";
    }
}
