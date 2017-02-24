package com.ocellus.platform.utils.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueryBuilder {

    private final String sql;
    private final List queryList;
    private final List componentList;

    public QueryBuilder(final String sql, final List tokenList) {
        this.sql = sql;
        queryList = new ArrayList();
        this.componentList = tokenList;
    }

    public void build() {

        IQueryComponent last = null;
        Query query = new Query(sql);
        final Iterator it = componentList.iterator();
        while (it.hasNext()) {
            final IQueryComponent component = (IQueryComponent) it.next();

            if (last != null && last.isUnion() && component.isAll()) {
                final Query lastQuery = (Query) queryList.get(queryList.size() - 1);
                lastQuery.addUnion(component);
                continue;
            }

            if (component.isSelect() && last != null && last.isOpenParenthesis()) { // indicates a sub query, we are looking for "... ( select ..." pattern
                int counter = 1;
                final Query subQuery = new Query(query);
                subQuery.addComponent(component);
                while (it.hasNext()) {
                    final IQueryComponent sub = (IQueryComponent) it.next();
                    if (sub.isOpenParenthesis()) {
                        subQuery.addComponent(sub);
                        counter++;
                    } else if (sub.isCloseParenthesis()) {
                        counter--;
                        if (counter == 0) {
                            query.addComponent(subQuery);
                            query.addComponent(sub);
                            break;
                        }
                    } else {
                        subQuery.addComponent(sub);
                    }
                }
            } else if (component.isUnion()) {
                query.addUnion(component);
                queryList.add(query);
                query = new Query(sql);
            } else {
                query.addComponent(component);
            }
            last = component;
        }
        queryList.add(query);
    }

    private void buildSubQuery(Query query, IQueryComponent component, Iterator it) {

        IQueryComponent last = null;
        int counter = 1;
        final Query subQuery = new Query(query);
        subQuery.addComponent(component);
        while (it.hasNext()) {
            final IQueryComponent sub = (IQueryComponent) it.next();
            if (sub.isSelect() && last != null && last.isOpenParenthesis()) {
                buildSubQuery(subQuery, sub, it);
            } else if (sub.isOpenParenthesis()) {
                subQuery.addComponent(sub);
                counter++;
            } else if (sub.isCloseParenthesis()) {
                counter--;
                if (counter == 0) {
                    query.addComponent(subQuery);
                    query.addComponent(sub);
                    break;
                }
            } else {
                subQuery.addComponent(sub);
            }
            last = sub;
        }
    }

    public List getQueryList() {
        return queryList;
    }
}
