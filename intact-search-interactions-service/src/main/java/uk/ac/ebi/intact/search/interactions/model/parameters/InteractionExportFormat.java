package uk.ac.ebi.intact.search.interactions.model.parameters;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by anjali on 26/02/19.
 */
@Getter
@RequiredArgsConstructor
public enum InteractionExportFormat {

    // Note: We don't use the capitalization for aesthetic purposes.
    // The enums are shown to the users in the swagger documentation.
    miJSON("json"),
    miXML25("xml25"),
    miXML30("xml30"),
    miTab25("tab25"),
    miTab26("tab26"),
    miTab27("tab27"),
    featureTab("featureTab");

    final String format;

    public static InteractionExportFormat findByFormat(String format) {

        for (InteractionExportFormat value : values()) {
            if(value.format.equals(format)){
                return value;
            }
        }

        return miJSON;
    }
}
