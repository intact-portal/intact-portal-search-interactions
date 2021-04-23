package uk.ac.ebi.intact.search.interactions.ws.controller.model;

public class SpeciesCount {

    private Long all;
    private Long intra;

    public SpeciesCount(Long all, Long intra) {
        this.all = all;
        this.intra = intra;
    }

    public Long getAll() {
        return all;
    }

    public void setAll(Long all) {
        this.all = all;
    }

    public Long getIntra() {
        return intra;
    }

    public void setIntra(Long intra) {
        this.intra = intra;
    }

}