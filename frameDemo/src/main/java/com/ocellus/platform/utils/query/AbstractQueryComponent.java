package com.ocellus.platform.utils.query;

public abstract class AbstractQueryComponent implements IQueryComponent {

    private final String type;

    private final String sql;

    public AbstractQueryComponent(final String sql, final String type) {
        this.sql = sql;
        this.type = type;
    }

    public String getValue() {
        return toString();
    }

    public String getAlias() {
        return getValue();
    }

    public String getType() {
        return type;
    }

    public String getSql() {
        return sql;
    }

    public boolean isAlias() {
        return getType().equals(IQueryComponent.TYPE_ALIAS);
    }

    public boolean isSelect() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_SELECT);
    }

    public boolean isOpenParenthesis() {
        return getType().equals(IQueryComponent.TYPE_OPEN_PARENTHESIS);
    }

    public boolean isCloseParenthesis() {
        return getType().equals(IQueryComponent.TYPE_CLOSE_PARENTHESIS);
    }

    public boolean isAll() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_ALL);
    }

    public boolean isUnion() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_UNION);
    }

    public boolean isBy() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_BY);
    }

    public boolean isGroup() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_GROUP);
    }

    public boolean isOrder() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_ORDER);
    }

    public boolean isWhere() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_WHERE);
    }

    public boolean isFrom() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_FROM);
    }

    public boolean isComma() {
        return getType().equals(IQueryComponent.TYPE_COMMA);
    }

    public boolean isUncategorized() {
        return getType().equals(IQueryComponent.TYPE_UNCATEGORIZED);
    }

    public boolean isAs() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_AS);
    }

    public boolean isPeriod() {
        return getType().equals(IQueryComponent.TYPE_PERIOD);
    }

    public boolean isDesc() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_DESC);
    }

    public boolean isTableColumnDesc() {
        return getType().equals(IQueryComponent.TYPE_TABLE_TABLE_COLUMN_DESC);
    }

    public boolean isJoin() {
        return getType().equals(IQueryComponent.TYPE_KEYWORD_JOIN);
    }

    public boolean isQuery() {
        return getType().equals(IQueryComponent.TYPE_QUERY);
    }

    public boolean isOperatorTimes() {
        return getType().equals(IQueryComponent.TYPE_OPERATOR_TIMES);
    }
}
