package com.ocellus.platform.utils.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueryAnalyzer {

    public IQueryComponent findWhere(final Query query) {
        IQueryComponent where = null;

        final Iterator it = query.getComponentList().iterator();
        while (it.hasNext()) {
            IQueryComponent component = (IQueryComponent) it.next();
            if (component.isWhere()) {
                where = component;
                break;
            }
        }
        return where;
    }

    public IQueryComponent findGroupBy(final Query query) {
        IQueryComponent groupBy = null;

        final Iterator it = query.getComponentList().iterator();
        while (it.hasNext()) {
            IQueryComponent component = (IQueryComponent) it.next();
            if (component.isGroup()) {
                final int beg = component.getStartPos();
                if (it.hasNext()) {
                    component = (IQueryComponent) it.next();
                    if (component.isBy()) {
                        groupBy = new QueryComponent(query.getSql(), "group by", beg, component.getEndPos());
                        break;
                    }
                }
            }
        }
        return groupBy;
    }

    public IQueryComponent findOrderBy(final Query query) {
        IQueryComponent orderBy = null;

        final Iterator it = query.getComponentList().iterator();
        while (it.hasNext()) {
            IQueryComponent component = (IQueryComponent) it.next();
            if (component.isOrder()) {
                final int beg = component.getStartPos();
                if (it.hasNext()) {
                    component = (IQueryComponent) it.next();
                    if (component.isBy()) {
                        orderBy = new QueryComponent(query.getSql(), "order by", beg, component.getEndPos());
                        break;
                    }
                }
            }
        }
        return orderBy;
    }


    public QueryColumns fetchOrderByColumnNames(final Query query) {

        IQueryComponent by = null;

        int counter = 0;

        final Iterator it = query.getComponentList().iterator();
        while (it.hasNext()) {
            IQueryComponent component = (IQueryComponent) it.next();
            if (component.isOrder()) {
                if (it.hasNext()) {
                    component = (IQueryComponent) it.next();
                    if (component.isBy()) {
                        by = component;
                        break;
                    }
                }
            }
        }

        final QueryColumns queryComponents = new QueryColumns();

        if (by != null) {

            IQueryComponent last1 = by;
            IQueryComponent last2 = null;
            IQueryComponent last3 = null;
            IQueryComponent last4 = null;

            while (it.hasNext()) {
                final IQueryComponent component = (IQueryComponent) it.next();
                if (component.isOpenParenthesis()) {
                    counter++;
                } else if (component.isCloseParenthesis()) {
                    counter--;
                } else if (counter == 0) {
                    if (component.isComma()) {
                        if (last1.isUncategorized()) {
                            if (last2.isComma() || last2.isUncategorized() || last2.isBy()) {
                                queryComponents.add(last1);
                            } else if (last2.isPeriod()) {
                                queryComponents.add(new QueryComponent(query.getSql(), IQueryComponent.TYPE_TABLE_PERIOD_COLUMN, last3.getStartPos(), last1.getEndPos()));
                            } else {
                                new IllegalStateException("Malformed query?\n" + query.getSql());
                            }
                        } else if (last1.isDesc()) {
                            if (last3.isComma() || last3.isUncategorized() || last3.isBy()) {
                                queryComponents.add(new QueryComponent(query.getSql(), IQueryComponent.TYPE_TABLE_TABLE_COLUMN_DESC, last2.getStartPos(), last1.getEndPos()));
                            } else if (last3.isPeriod()) {
                                queryComponents.add(new QueryComponent(query.getSql(), IQueryComponent.TYPE_TABLE_TABLE_COLUMN_DESC, last4.getStartPos(), last2.getEndPos()));
                            } else {
                                new IllegalStateException("Malformed query?\n" + query.getSql());
                            }
                        } else {
                            queryComponents.add(null);
                        }
                        counter = 0;
                        if (component.isFrom()) {
                            break;
                        }
                    } else if (component.isUncategorized() && !it.hasNext()) {
                        if (last1 != null && last1 != null && last1.isPeriod() && last2.isUncategorized()) {
                            queryComponents.add(new QueryComponent(query.getSql(), IQueryComponent.TYPE_TABLE_PERIOD_COLUMN, last2.getStartPos(), component.getEndPos()));
                        } else {
                            queryComponents.add(component);
                        }
                    }
                }
                last4 = last3;
                last3 = last2;
                last2 = last1;
                last1 = component;
            }
        }

        return queryComponents;
    }

    /**
     * List can contain null values in cases of:
     * SELECT sum(payment.amount), ....
     * where no column name exist and no alias provided
     */
    public QueryColumns fetchSelectColumnNames(final Query query) {

        final QueryColumns queryComponents = new QueryColumns();

        IQueryComponent last1 = null;
        IQueryComponent last2 = null;
        IQueryComponent last3 = null;

        int counter = 0;

        final Iterator it = query.getComponentList().iterator();
        while (it.hasNext()) {
            final IQueryComponent component = (IQueryComponent) it.next();
            if (component.isOpenParenthesis()) {
                counter++;
            } else if (component.isCloseParenthesis()) {
                counter--;
            } else if (counter == 0) {
                if (component.isFrom() || component.isComma()) {
                    if (last1.isUncategorized()) {
                        if (last2.isAs()) {
                            queryComponents.add(new QueryComponent(query.getSql(), IQueryComponent.TYPE_ALIAS, last1.getStartPos(), last1.getEndPos()));

                        } else if (last2.isComma() || last2.isUncategorized() || last2.isSelect()) {
                            queryComponents.add(last1);
                        } else if (last2.isPeriod() && last3.isUncategorized()) {
                            queryComponents.add(new QueryComponent(query.getSql(), IQueryComponent.TYPE_TABLE_PERIOD_COLUMN, last3.getStartPos(), last1.getEndPos()));
                        } else {
                            new IllegalStateException("Malformed query?\n" + query.getSql());
                        }
                    } else {
                        queryComponents.add(null);
                    }
                    counter = 0;
                    if (component.isFrom()) {
                        break;
                    }
                }
            }
            last3 = last2;
            last2 = last1;
            last1 = component;
        }

        return queryComponents;
    }

    public String formatFullyQualifiedName(final Query query, final String column) {

        final QueryColumns queryComponents = fetchSelectColumnNames(query);

        final IQueryComponent existingComponent = queryComponents.get(column);
        if (existingComponent != null) {
            if (existingComponent.getValue().indexOf('.') != -1) {
                return existingComponent.getValue();
            }
        }

        IQueryComponent last1 = null;
        IQueryComponent last2 = null;
        IQueryComponent last3 = null;
        IQueryComponent last4 = null;
        IQueryComponent last5 = null;

        boolean fromPast = false;

        final Iterator it = query.getComponentList().iterator();
        while (it.hasNext()) {
            final IQueryComponent component = (IQueryComponent) it.next();

            if (component.isFrom()) {
                fromPast = true;
            }

            if (!fromPast && component.isComma() || component.isFrom()) {
                if (last2.isAs() && last1.getValue().equals(column)) {
                    // we have an alias match
                    if (last3 != null && last4 != null && last5 != null && last4.isPeriod()) {
                        // case table.column AS alias_name
                        return last5.getValue() + "." + last3.getValue();
                    } else if (last3 != null && last4 != null && last3.isCloseParenthesis()) {
                        // case FUNCTION(table.column) AS alias_name
                        return column;
                    } else if (last3 != null && last3.isUncategorized() && !last3.isQuery()) {
                        // case column AS alias_name
                        // only recurse in the case where the column != alias name - CR120763 - GAB
                        if (column.equalsIgnoreCase(last3.getValue())) {
                            return column;
                        } else {
                            return formatFullyQualifiedName(query, last3.getValue());
                        }
                    }
                }
            }

            if (fromPast && last1 != null && last1.isPeriod()) {
                if (component.getValue().equals(column)) {
                    return last2.getValue() + "." + column;
                }
            }

            last5 = last4;
            last4 = last3;
            last3 = last2;
            last2 = last1;
            last1 = component;
        }

        return column;
    }

    public QueryTables fetchTables(final Query query) {

        final QueryTables queryTables = new QueryTables();

        int counter = 0;
        int state = 0;
        IQueryComponent last1 = null;
        IQueryComponent last2 = null;
        for (int i = 0; i < query.getComponentList().size(); i++) {
            final IQueryComponent component = (IQueryComponent) query.getComponentList().get(i);
            if (component.isOpenParenthesis()) {
                counter++;
            } else if (component.isCloseParenthesis()) {
                counter--;
            } else if (counter == 0) {
                if (component.isWhere() || component.isGroup()) {
                    break; // we are done
                } else if (state == 0) {
                    if (component.isFrom()) {
                        state = 1;
                    }
                } else if (state == 1) {
                    if (last1.isFrom() || last1.isComma() || last1.isJoin()) {
                        if (component.isUncategorized()) { // must be a table
                            if (i + 1 < query.getComponentList().size()) {
                                final IQueryComponent nextComponent = (IQueryComponent) query.getComponentList().get(i + 1);
                                if (nextComponent.isUncategorized()) {
                                    queryTables.add(new QueryColumnComponent(component, nextComponent));
                                    i++;
                                } else {
                                    queryTables.add(component);
                                }
                            } else {
                                queryTables.add(component);
                            }
                        } else { // probably a sub-query
                            queryTables.add(null);
                        }
                    } else if (last2.isQuery()) {
                        final Query subQuery = (Query) last2;
                        final List subQueryComponents = subQuery.getComponentList();
                        final int size = subQueryComponents.size();
                        boolean isSimpleSubQuery = false;
                        if (size > 3) {
                            final IQueryComponent subFirst = (IQueryComponent) subQueryComponents.get(0);
                            final IQueryComponent subLast1 = (IQueryComponent) subQueryComponents.get(size - 1);
                            final IQueryComponent subLast2 = (IQueryComponent) subQueryComponents.get(size - 2);
                            if (subFirst.isSelect() && subLast1.isUncategorized() && subLast2.isFrom()) {
                                final List columns = new ArrayList();
                                for (int j = 1; j < size - 1; j++) {
                                    final IQueryComponent subComponent = (IQueryComponent) subQueryComponents.get(j);
                                    if (subComponent.isComma() || subComponent.isFrom()) {
                                        columns.add((IQueryComponent) subQueryComponents.get(j - 1));
                                    }
                                }

                                if (!columns.isEmpty()) {
                                    isSimpleSubQuery = true;
                                    final Iterator columnsIt = columns.iterator();
                                    while (columnsIt.hasNext()) {
                                        final IQueryComponent subComponent = (IQueryComponent) columnsIt.next();
                                        queryTables.add(new QueryColumnComponent(subLast1, component, subComponent));
                                    }
                                }
                            }
                        }

                        if (!isSimpleSubQuery) {
                            final IQueryComponent subFirst = (IQueryComponent) subQueryComponents.get(0);
                            int fromIndex = -1;
                            int fromSign = 0;
                            int timesSign = 0;
                            if (subFirst.isSelect()) {
                                for (int j = 1; j < size - 1; j++) {
                                    final IQueryComponent subComponent = (IQueryComponent) subQueryComponents.get(j);
                                    if (subComponent.isFrom() && fromSign == 0) {
                                        fromIndex = j;
                                        fromSign = 1;
                                        break;
                                    }
                                }

                                if (fromIndex > 0) {
                                    IQueryComponent subLast1 = null;
                                    IQueryComponent subLast2 = null;
                                    for (int k = 1; k < fromIndex; k++) {
                                        final IQueryComponent subcomponent = (IQueryComponent) subQueryComponents.get(k);
                                        boolean subLastsNull = (null == subLast1 || null == subLast2);
                                        if (!subLastsNull && subcomponent.isOperatorTimes() && timesSign == 0) {
                                            if (subLast1.isPeriod() && subLast2.getValue().equals(component.getValue())) {
                                                queryTables.add(component);
                                                timesSign = 1;
                                                break;
                                            } else if (!subLast1.isPeriod()) {
                                                queryTables.add(component);
                                                timesSign = 1;
                                                break;
                                            }
                                        }
                                        subLast2 = subLast1;
                                        subLast1 = subcomponent;
                                    }
                                }
                                if (fromSign == 0 || timesSign == 0) {
                                    queryTables.add(null);
                                }
                            } else {
                                queryTables.add(null);
                            }
                        }
                    }
                }
            }
            last2 = last1;
            last1 = component;
        }
        return queryTables;
    }

    public List fetchConstraintColumns(final Query query, final String table) {
        final List columns = new ArrayList();

        final Iterator it = query.getComponentList().iterator();
        IQueryComponent last1 = null;
        IQueryComponent last2 = null;
        while (it.hasNext()) {
            final IQueryComponent component = (IQueryComponent) it.next();
            final boolean bLast1 = last1 != null && last1.isPeriod();
            final boolean bLast2 = last2 != null && last2.isUncategorized();
            if (component.isUncategorized() && bLast1 && bLast2 && last2.getValue().equalsIgnoreCase(table)) {
                columns.add(component);
            }
            last2 = last1;
            last1 = component;
        }

        return columns;
    }
}

