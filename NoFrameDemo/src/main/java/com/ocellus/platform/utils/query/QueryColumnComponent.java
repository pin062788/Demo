package com.ocellus.platform.utils.query;

public class QueryColumnComponent extends QueryComponent {

    private final IQueryComponent table;
    private final IQueryComponent alias;

    public QueryColumnComponent(final IQueryComponent table, final IQueryComponent alias) {
        super(table.getSql(), TYPE_TABLE_ALIAS, table.getStartPos(), alias.getEndPos());
        this.table = table;
        this.alias = alias;
    }

    public QueryColumnComponent(final IQueryComponent table, final IQueryComponent alias, final IQueryComponent column) {
        super(column.getSql(), TYPE_TABLE_PERIOD_COLUMN, column.getStartPos(), column.getEndPos());
        this.table = table;
        this.alias = alias;
    }

//	public String toString() {
//		return getType().equals(TYPE_TABLE_PERIOD_COLUMN) ? table + "." + super.toString() : table.toString();
//	}

    public IQueryComponent getQueryTableComponent() {
        return table;
    }

    public String getAlias() {
        return getType().equals(TYPE_TABLE_PERIOD_COLUMN) ? alias + "." + super.toString() : alias.toString();
    }

    public IQueryComponent getQueryAliasComponent() {
        return alias;
    }
}
