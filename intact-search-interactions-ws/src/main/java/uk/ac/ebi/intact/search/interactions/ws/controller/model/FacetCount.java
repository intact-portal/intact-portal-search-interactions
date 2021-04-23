package uk.ac.ebi.intact.search.interactions.ws.controller.model;

public class FacetCount<T> {

    private String value;
    private String termId;
    private String visualProperty;
    private T valueCount;

    FacetCount(String value, T valueCount) {
        setValue(value);
        setValueCount(valueCount);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        if (value != null) {
            String[] styledValue = value.split("__");
            if (styledValue.length == 3) {
                setTermId(styledValue[0]);
                this.value = styledValue[1];
                setVisualProperty(styledValue[2]);
            }
            else {
                this.value = value;
            }
        }
    }

    public String getTermId() {
        return termId;
    }

    public void setTermId(String termId) {
        this.termId = termId;
    }

    public String getVisualProperty() {
        return visualProperty;
    }

    public void setVisualProperty(String visualProperty) {
        this.visualProperty = visualProperty;
    }

    public T getValueCount() {
        return valueCount;
    }

    public void setValueCount(T valueCount) {
        this.valueCount = valueCount;
    }
}