package uk.ac.ebi.intact.search.interactions.service.util;

import uk.ac.ebi.intact.search.interactions.model.Interaction;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

/**
 * Created by anjali on 15/08/18.
 */
public class TestUtil {

    public static Collection<Interaction> getInteractionObjFromXml(String filePath){

        XMLDecoder decoder=null;
        try {
            decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(filePath)));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File dvd.xml not found");
        }
        return (Collection<Interaction>)decoder.readObject();
    }
}
