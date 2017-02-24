package com.ocellus.platform.utils.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Query extends AbstractQueryComponent {

    private final List componentList;

    private IQueryComponent union;

    Query(final String sql) {
        super(sql, TYPE_QUERY);
        componentList = new ArrayList();
    }

    Query(final IQueryComponent component) {
        this(component.getSql());
    }

    void addComponent(final IQueryComponent token) {
        componentList.add(token);
    }

    public int getEndPos() {
        return fetchLastQueryComponent().getEndPos();
    }

    public int getStartPos() {
        return fetchQueryComponent(0).getStartPos();
    }

    public IQueryComponent fetchLastQueryComponent() {
        return fetchQueryComponent(componentList.size() - 1);
    }

    public IQueryComponent fetchQueryComponent(final int index) {
        return (IQueryComponent) componentList.get(index);
    }

    public List getComponentList() {
        return Collections.unmodifiableList(componentList);
    }

    public String getType() {
        return TYPE_QUERY;
    }

    public String getValue() {
        return getSql().substring(getStartPos(), getEndPos() + 1);
    }

    public String toString() {
        return getValue();
    }

    public String getAllValue() {
        return union != null ? getSql().substring(getStartPos(), union.getEndPos() + 1) : getValue();
    }

    public void addUnion(final IQueryComponent component) {
        if (component.isUnion()) {
            union = component;
        } else if (component.isAll() && union != null) {
            union = new QueryComponent(getSql(), TYPE_UNION_ALL, union.getStartPos(), component.getEndPos());
        }
    }
}
