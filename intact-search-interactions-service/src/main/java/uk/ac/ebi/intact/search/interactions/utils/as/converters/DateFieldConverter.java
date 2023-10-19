package uk.ac.ebi.intact.search.interactions.utils.as.converters;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFieldConverter {

    public static Integer indexFieldValues(Date dateToBeConverted) {

        Integer date = 0;
        if (dateToBeConverted != null) {
            String formattedDate = "";
            try {
                SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy/MM/dd");
                formattedDate = simpleFormat.format(dateToBeConverted);
                if (!formattedDate.isEmpty()) {
                    date = Integer.parseInt(formattedDate.replace("/", "")); //int representation of date
                }
            } catch (Exception e) {
                //log?
                e.printStackTrace(System.err);
                System.err.print("Error when trying to create date format yyyy/MM/dd from " + date.toString());
            }

        }
        return date;

    }
}
