package uk.ac.ebi.intact.search.interactions.utils.as.converters;

import java.util.HashSet;
import java.util.Set;

public class XrefFieldConverter {

    public static Set<String> indexFieldValues(String db, String value) {

        Set<String> xrefValuesToBeIndexed = new HashSet<>();

        if (db != null) {
            xrefValuesToBeIndexed.add(db);
        }
        if (value != null) {
            xrefValuesToBeIndexed.add(value);
        }
        if (db != null && value != null) {
            xrefValuesToBeIndexed.add(db + ":" + value);
        }

        return xrefValuesToBeIndexed;

    }
}
