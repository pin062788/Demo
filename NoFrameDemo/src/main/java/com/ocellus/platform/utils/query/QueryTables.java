package com.ocellus.platform.utils.query;

import java.util.*;

public class QueryTables {

    private final Map tableMap;   // does not contain null values
    private final List tableList; // can contain null values, used for unit testing

    QueryTables() {
        tableMap = new HashMap();
        tableList = new ArrayList();
    }

    /**
     * Need to remove if we are to support alias handling
     *
     * @param qcc
     */
    void add(final QueryColumnComponent qcc) {
        if (qcc != null) {
            final String key = qcc.getQueryTableComponent().toString().toLowerCase();
            List list = (List) tableMap.get(key);
            if (list == null) {
                list = new ArrayList();
                tableMap.put(key, list);
            }
            list.add(qcc);
        }
        tableList.add(qcc);
    }

    void add(final IQueryComponent table) {
        if (table != null) {
            final String key = table.toString().toLowerCase();
            List list = (List) tableMap.get(key);
            if (list == null) {
                list = new ArrayList();
                tableMap.put(key, list);
            }
            list.add(table);
        }
        tableList.add(table);
    }


    public List get(final String table) {
        return (List) tableMap.get(table);
    }

    public List get(final String table, final String column) {
        final String key = table + "." + column;
        return (List) tableMap.get(key.toLowerCase());
    }

    public Iterator iterator() {
        return tableList.iterator();
    }

    public int size() {
        return tableList.size();
    }

    List fetchTables() {
        return Collections.unmodifiableList(tableList);
    }

    int getTableMapSize() {
        return tableMap.size();
    }
}
