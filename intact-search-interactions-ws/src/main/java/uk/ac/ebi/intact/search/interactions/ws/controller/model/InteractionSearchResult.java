package uk.ac.ebi.intact.search.interactions.ws.controller.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;

import java.util.*;
import java.util.function.Function;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.*;

/**
 * This class has all the methods/utils of a Page and one more customized method getFacetResultPage()
 */
public class InteractionSearchResult implements Page<SearchInteraction> {

    private final FacetPage<SearchInteraction> page;

    public InteractionSearchResult(FacetPage<SearchInteraction> page) {
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
    public List<SearchInteraction> getContent() {
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
    public <U> Page<U> map(Function<? super SearchInteraction, ? extends U> converter) {
        return page.map(converter);
    }

    @Override
    public Iterator<SearchInteraction> iterator() {
        return page.iterator();
    }

    public Collection<Field> getFacetFields() {
        return page.getFacetFields();
    }

    /**
     * Gives map of facet fields and List of FacetCount,
     * where List of FacetCount contains List of facet field values and their respective counts.
     * This was implemented to save client from complexity of calculating facets from page.
     *
     * @return
     */
    public Map<String, List<FacetCount>> getFacetResultPage() {
        Map<String, List<FacetCount>> facetPerFieldMap = new HashMap<>();

        for (Field field : page.getFacetFields()) {
            List<FacetCount> facet = new ArrayList<>();
            for (FacetFieldEntry facetFieldEntry : page.getFacetResultPage(field).getContent()) {
                facet.add(new FacetCount<>(facetFieldEntry.getValue(), facetFieldEntry.getValueCount()));
            }
            facetPerFieldMap.put(field.getName(), facet);
        }

        //TODO Add testing
        List<FacetCount> combinedFacet = new ArrayList<>();
        for (FacetFieldEntry interSpeciesEntry : page.getFacetResultPage(TAX_ID_A_B_STYLED).getContent()) {
            String interSpeciesEntryValue = interSpeciesEntry.getValue();

            SpeciesCount speciesCount = new SpeciesCount(interSpeciesEntry.getValueCount(), 0L);

            for (FacetFieldEntry facetFieldEntry : page.getFacetResultPage(INTRA_TAX_ID_STYLED).getContent()) {
                if(facetFieldEntry.getValue().equalsIgnoreCase(interSpeciesEntryValue)){
                    speciesCount.setIntra(facetFieldEntry.getValueCount());
                    break;
                }
            }

            combinedFacet.add(new FacetCount<>(interSpeciesEntry.getValue(), speciesCount));
        }

        facetPerFieldMap.put("combined_species", combinedFacet);

        return facetPerFieldMap;
    }
}