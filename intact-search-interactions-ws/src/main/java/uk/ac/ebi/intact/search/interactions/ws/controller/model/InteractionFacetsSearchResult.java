package uk.ac.ebi.intact.search.interactions.ws.controller.model;

import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.FacetQueryEntry;
import uk.ac.ebi.intact.search.interactions.model.SearchInteraction;

import java.util.*;

import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.INTRA_TAX_ID_STYLED;
import static uk.ac.ebi.intact.search.interactions.model.SearchInteractionFields.TAX_ID_A_B_STYLED;

public class InteractionFacetsSearchResult {

    private final FacetPage<SearchInteraction> page;

    public InteractionFacetsSearchResult(FacetPage<SearchInteraction> page) {
        this.page = page;
    }

    public long getTotalElements() {
        return page.getTotalElements();
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
            List<FacetCount> facets = new ArrayList<>();
            for (FacetFieldEntry facetFieldEntry : page.getFacetResultPage(field).getContent()) {
                facets.add(new FacetCount<>(facetFieldEntry.getValue(), facetFieldEntry.getValueCount()));
            }
            facetPerFieldMap.put(field.getName(), facets);
        }

        //TODO Add testing
        List<FacetCount> combinedFacets = new ArrayList<>();
        for (FacetFieldEntry interSpeciesEntry : page.getFacetResultPage(TAX_ID_A_B_STYLED).getContent()) {
            String interSpeciesEntryValue = interSpeciesEntry.getValue();

            SpeciesCount speciesCount = new SpeciesCount(interSpeciesEntry.getValueCount(), 0L);

            for (FacetFieldEntry facetFieldEntry : page.getFacetResultPage(INTRA_TAX_ID_STYLED).getContent()) {
                if(facetFieldEntry.getValue().equalsIgnoreCase(interSpeciesEntryValue)){
                    speciesCount.setIntra(facetFieldEntry.getValueCount());
                    break;
                }
            }

            combinedFacets.add(new FacetCount<>(interSpeciesEntry.getValue(), speciesCount));
        }

        facetPerFieldMap.put("combined_species", combinedFacets);

        // Expansion method query facet
        //TODO Add testing
        List<FacetCount> facets = new ArrayList<>();

        for (FacetQueryEntry facetQueryEntry : page.getFacetQueryResult().getContent()) {

            if (facetQueryEntry.getKey().equalsIgnoreCase("expansion_true")) {
                facets.add(new FacetCount<>("true", facetQueryEntry.getValueCount())); //801
            }
            if (facetQueryEntry.getKey().equalsIgnoreCase("expansion_false")) {
                facets.add(new FacetCount<>("false", facetQueryEntry.getValueCount())); //733
            }
        }
        facetPerFieldMap.put("expansion_method_s", facets);


        return facetPerFieldMap;
    }

}