package com.ocellus.platform.utils.query;

import java.util.*;

public class QueryColumns {

    private final Map columnMap;   // does not contain null values
    private final List columnList; // can contain null values

    QueryColumns() {
        columnMap = new HashMap();
        columnList = new ArrayList();
    }

    void add(final IQueryComponent column) {
        if (column != null) {
            columnMap.put(column.toString(), column);
        }
        columnList.add(column);
    }

    public boolean containsColumn(final String column) {
        return columnMap.containsKey(column);
    }

    public IQueryComponent get(final String column) {
        return (IQueryComponent) columnMap.get(column);
    }

    public Iterator iterator() {
        return columnList.iterator();
    }

    public int size() {
        return columnList.size();
    }

    public List fetchColumns() {
        return Collections.unmodifiableList(columnList);
    }

    public List fetchActiveColumns() {
        return new ArrayList(columnMap.values());
    }
}
