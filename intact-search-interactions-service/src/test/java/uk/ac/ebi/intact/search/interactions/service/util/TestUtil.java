package uk.ac.ebi.intact.search.interactions.service.util;

import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Created by anjali on 15/08/18.
 */
public class TestUtil {

    public static Collection<SearchInteraction> getInteractionObjFromXml(String filePath) {

        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filePath)));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File Interactions.xml not found");
        }
        return (Collection<SearchInteraction>) decoder.readObject();
    }

    public static <T> Set<T> merge(Collection<? extends T>... collections) {
        return Arrays.stream(collections).flatMap(Collection::stream).collect(toSet());
    }
}
