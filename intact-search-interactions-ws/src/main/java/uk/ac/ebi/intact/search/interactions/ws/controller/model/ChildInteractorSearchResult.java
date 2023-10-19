package uk.ac.ebi.intact.search.interactions.ws.controller.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.result.GroupPage;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by anjali on 17/02/20.
 */
public class ChildInteractorSearchResult implements Page<SearchChildInteractor> {

    private final GroupPage<SearchChildInteractor> groupPage;
    private final long numGroups;

    public ChildInteractorSearchResult(GroupPage<SearchChildInteractor> page, long numGroups) {
        this.groupPage = page;
        this.numGroups = numGroups;
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) numGroups / (double) getSize());
    }

    @Override
    public long getTotalElements() {
        return numGroups;
    }

    @Override
    public int getNumber() {
        return groupPage.getNumber();
    }

    @Override
    public int getSize() {
        return groupPage.getSize();
    }

    @Override
    public int getNumberOfElements() {
        return groupPage.getNumberOfElements();
    }

    @Override
    public List<SearchChildInteractor> getContent() {
        return groupPage.getContent();
    }

    @Override
    public boolean hasContent() {
        return groupPage.hasContent();
    }

    @Override
    public Sort getSort() {
        return groupPage.getSort();
    }

    @Override
    public boolean isFirst() {
        return groupPage.isFirst();
    }

    @Override
    public boolean isLast() {
        return groupPage.isLast();
    }

    @Override
    public boolean hasNext() {
        return groupPage.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return groupPage.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return groupPage.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return groupPage.previousPageable();
    }

    @Override
    public <U> Page<U> map(Function<? super SearchChildInteractor, ? extends U> converter) {
        return groupPage.map(converter);
    }

    @Override
    public Iterator<SearchChildInteractor> iterator() {
        return groupPage.iterator();
    }

}
