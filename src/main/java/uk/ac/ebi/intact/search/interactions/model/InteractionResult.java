package uk.ac.ebi.intact.search.interactions.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;

/**
 * This class has all the methods/utils of a Page and one more customized method getFacetResultPage()
 */
public class InteractionResult implements Page<Interaction> {

    private final FacetPage<Interaction> page;

    public InteractionResult(FacetPage<Interaction> page) {
        this.page = page;
    }

    @Override
    public int getTotalPages() {
        return page.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return page.getTotalElements();
    }

    @Override
    public int getNumber() {
        return page.getNumber();
    }

    @Override
    public int getSize() {
        return page.getSize();
    }

    @Override
    public int getNumberOfElements() {
        return page.getNumberOfElements();
    }

    @Override
    public List<Interaction> getContent() {
        return page.getContent();
    }

    @Override
    public boolean hasContent() {
        return page.hasContent();
    }

    @Override
    public Sort getSort() {
        return page.getSort();
    }

    @Override
    public boolean isFirst() {
        return page.isFirst();
    }

    @Override
    public boolean isLast() {
        return page.isLast();
    }

    @Override
    public boolean hasNext() {
        return page.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return page.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return page.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return page.previousPageable();
    }

    @Override
    public <U> Page<U> map(Function<? super Interaction, ? extends U> converter) {
        return page.map(converter);
    }

    @Override
    public Iterator<Interaction> iterator() {
        return page.iterator();
    }

    public Collection<Field> getFacetFields() {
        return page.getFacetFields();
    }

    /**
     * Gives map of facet fields and List of FacetCount,
     * where List of FacetCount contains List of facet field values and their respective counts.
     * This was implemented to save client from complexity of calculating facets from page.
     * @return
     */
    public Map<String, List<FacetCount>> getFacetResultPage() {
        Map<String, List<FacetCount>> facetPerFieldMap = new HashMap<>();

        for (Field field : page.getFacetFields()) {
            List<FacetCount> facet = new ArrayList<>();
            if (field.getName().equals(InteractionFields.INTACT_MISCORE)) {
                for (FacetFieldEntry facetFieldEntry : page.getRangeFacetResultPage(field).getContent()) {
                    DecimalFormat df = new DecimalFormat("####0.00");// inherently it is giving long decimal values
                    facet.add(new FacetCount(df.format(Double.parseDouble(facetFieldEntry.getValue())), facetFieldEntry.getValueCount()));
                }
            } else {
                for (FacetFieldEntry facetFieldEntry : page.getFacetResultPage(field).getContent()) {
                    facet.add(new FacetCount(facetFieldEntry.getValue(), facetFieldEntry.getValueCount()));
                }
            }
            facetPerFieldMap.put(field.getName(), facet);
        }

        return facetPerFieldMap;
    }

/*    public Map<String,Map<String,Long>> getFacetResultPage2() {
        Map<String,Map<String,Long>> facetPerFieldMap = new HashMap<>();

        for (Field field : page.getFacetFields()) {
            Map<String,Long> facet = new HashMap<>();
            for (FacetFieldEntry facetFieldEntry : page.getFacetResultPage(field).getContent()) {
                facet.put(facetFieldEntry.getValue(),facetFieldEntry.getValueCount());
            }
            facetPerFieldMap.put(field.getName(), facet);
        }

        return facetPerFieldMap;
    }*/

    public class FacetCount {

        private String value;
        private Long valueCount;

        FacetCount(String value, Long valueCount) {
            this.value = value;
            this.valueCount = valueCount;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Long getValueCount() {
            return valueCount;
        }

        public void setValueCount(Long valueCount) {
            this.valueCount = valueCount;
        }
    }
}
