package uk.ac.ebi.intact.search.interactions.utils;

import uk.ac.ebi.intact.search.interactions.model.AdvancedSearchInteractionFields;

import java.util.Map;

import static uk.ac.ebi.intact.search.interactions.model.AdvancedSearchInteractionFields.SOLR_FIELD_VALUE_SEPARATOR;
import static uk.ac.ebi.intact.search.interactions.model.AdvancedSearchInteractionFields.SOLR_FIELD_VALUE_SEPARATOR_VALUE;

public class AdvancedSearchInteractionUtility {

    private static String[] miqlKeys;// MIQL fields+":"
    private static String[] asValues;// Advanced search fields+":"


    public static String getAdvancedSearchQuery(String userAdvancedQuery) {

        // remove spaces around ':' so that miql/advanced search can be detected
        String searchTerms = userAdvancedQuery.replaceAll("\\s*([:])\\s*", ":");

        Map<String, String> miqlAsMap = AdvancedSearchInteractionFields.MIQL_AS_MAP;
        for (String miqlKey : miqlAsMap.keySet()) {
            searchTerms = searchTerms.replaceAll("\\b" + miqlKey, miqlAsMap.get(miqlKey));
        }

        String formattedSearchTerms = escapeQueryChars(searchTerms);// escape solr special query characters

        return "(" + formattedSearchTerms.replaceAll(SOLR_FIELD_VALUE_SEPARATOR, SOLR_FIELD_VALUE_SEPARATOR_VALUE) + ")";
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
