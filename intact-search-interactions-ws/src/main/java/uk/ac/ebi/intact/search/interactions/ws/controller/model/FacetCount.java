package uk.ac.ebi.intact.search.interactions.ws.controller.model;

public class FacetCount<T> {

    private String value;
    private String termId;
    private String color;
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
                setColor(styledValue[2]);
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public T getValueCount() {
        return valueCount;
    }

    public void setValueCount(T valueCount) {
        this.valueCount = valueCount;
    }
}