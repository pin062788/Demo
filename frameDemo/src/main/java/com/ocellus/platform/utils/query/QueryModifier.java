package com.ocellus.platform.utils.query;

import java.util.Iterator;

public class QueryModifier {

    public String insertConstraints(final Query query, final String constraints) {

        final String sql = query.getSql();
        final Iterator it = query.getComponentList().iterator();
        while (it.hasNext()) {
            final IQueryComponent component = (IQueryComponent) it.next();
            if (component.isWhere()) {
                final String result = sql.substring(query.getStartPos(), component.getEndPos() + 1) + " " + constraints + " AND " + sql.substring(component.getEndPos() + 2, query.getEndPos() + 1);
                return result;
            } else if (component.isGroup() || component.isOrder()) {
                return sql.substring(query.getStartPos(), component.getStartPos() - 1) + " WHERE " + constraints + " " + sql.substring(component.getStartPos(), query.getEndPos() + 1);
            }
        }

        return sql + " WHERE " + constraints;
    }

    public String insertJoin(final Query query, final String join) {
        final String sql = query.getSql();
        int selectStartPos = sql.indexOf(query.getValue());
        if (selectStartPos == -1)
            selectStartPos = 0;

        final Iterator it = query.getComponentList().iterator();
        while (it.hasNext()) {
            final IQueryComponent component = (IQueryComponent) it.next();
            if (component.isWhere()) {
                final String select = sql.substring(selectStartPos, component.getStartPos() - 1);
                final String where = sql.substring(component.getStartPos(), query.getEndPos() + 1);
                return select + " " + join + " " + where;
            }
        }

        return sql + " " + join;
    }

    public String replaceWithAlias(final Query query, final IQueryComponent table) {
        return replaceWithAlias(query, table.getValue(), table.getAlias());
    }

    public String replaceWithAlias(final Query query, final String table, final String alias) {

        final StringBuffer sb = new StringBuffer();

        IQueryComponent last1 = null;

        final Iterator it = query.getComponentList().iterator();
        final String sql = query.getSql();
        while (it.hasNext()) {
            final IQueryComponent component = (IQueryComponent) it.next();

            if (last1 != null) {
                sb.append(sql.substring(last1.getEndPos() + 1, component.getStartPos()));
            }

            if (component.isUncategorized() && component.getValue().equalsIgnoreCase(table)) {
                sb.append(alias);
            } else {
                sb.append(component.getValue());
            }

            last1 = component;
        }

        return sb.toString();
    }
}
