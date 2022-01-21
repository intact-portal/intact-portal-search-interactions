package uk.ac.ebi.intact.search.interactions.utils;

import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.intact.search.interactions.model.AdvancedSearchInteractionFields;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class AdvancedSearchInteractionUtility {

    private static String[] miqlKeys;// MIQL fields+":"
    private static String[] asValues;// Advanced search fields+":"
    private static String SOLR_FIELD_VALUE_SEPARATOR = "SOLR_FIELD_VALUE_SEPARATOR";

    public static String getAdvancedSearchQuery(String userAdvancedQuery) {

        // remove spaces around ':' so that miql/advanced search can be detected
        String searchTerms = userAdvancedQuery.replaceAll("\\s*([:])\\s*", ":");

        Map<String, String> miqlAsMap = AdvancedSearchInteractionFields.MIQL_AS_MAP;
        if (miqlKeys == null) {
            miqlKeys = miqlAsMap.keySet().toArray(new String[0]);
            Arrays.stream(miqlKeys).map(e -> e + ":").collect(Collectors.toList()).toArray(miqlKeys);
        }
        if (asValues == null) {
            asValues = miqlAsMap.values().toArray(new String[0]);
            // so that we can escape solr special query characters
            Arrays.stream(asValues).map(e -> e + SOLR_FIELD_VALUE_SEPARATOR).collect(Collectors.toList()).toArray(asValues);
        }

        String formattedSearchTerms = escapeQueryChars(
                StringUtils.replaceEach(searchTerms, miqlKeys, asValues));// escape solr special query characters

        return "(" + formattedSearchTerms.replaceAll(SOLR_FIELD_VALUE_SEPARATOR, ":") + ")";
    }

    /* Escape advanced query solr special characters */
    public static String escapeQueryChars(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the advanced query syntax and must be escaped
            if (c == ':') {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
