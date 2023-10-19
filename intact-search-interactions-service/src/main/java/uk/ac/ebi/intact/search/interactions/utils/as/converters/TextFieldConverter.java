package uk.ac.ebi.intact.search.interactions.utils.as.converters;

import java.util.HashSet;
import java.util.Set;

public class TextFieldConverter {

    public static Set<String> indexFieldValues(String db, String value, String text) {

        Set<String> valuesToBeIndexed = new HashSet<>();

        if (db != null) {
            valuesToBeIndexed.add(db);
        }
        if (value != null) {
            valuesToBeIndexed.add(value);
        }
        if (db != null && value != null) {
            valuesToBeIndexed.add(db + ":" + value);
        }

        if (text != null) {
            valuesToBeIndexed.add(text);
        }

        return valuesToBeIndexed;

    }
}
