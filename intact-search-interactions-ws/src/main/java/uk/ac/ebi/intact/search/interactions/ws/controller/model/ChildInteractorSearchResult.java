package uk.ac.ebi.intact.search.interactions.ws.controller.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import uk.ac.ebi.intact.search.interactions.model.SearchChildInteractor;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by anjali on 17/02/20.
 */
public class ChildInteractorSearchResult implements GroupPage<SearchChildInteractor> {

    private final GroupPage<SearchChildInteractor> groupPage;

    public ChildInteractorSearchResult(GroupPage<SearchChildInteractor> page) {
        this.groupPage = page;
    }

    @Override
    public int getTotalPages() {
        return groupPage.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return groupPage.getTotalElements();
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


    @Override
    public GroupResult<SearchChildInteractor> getGroupResult(Field field) {
        return groupPage.getGroupResult(field);
    }

    @Override
    public GroupResult<SearchChildInteractor> getGroupResult(org.springframework.data.solr.core.query.Function function) {
        return groupPage.getGroupResult(function);
    }

    @Override
    public GroupResult<SearchChildInteractor> getGroupResult(Query query) {
        return groupPage.getGroupResult(query);
    }

    @Override
    public GroupResult<SearchChildInteractor> getGroupResult(String s) {
        return groupPage.getGroupResult(s);
    }
}
