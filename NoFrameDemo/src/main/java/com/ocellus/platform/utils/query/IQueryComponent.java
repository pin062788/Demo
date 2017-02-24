package com.ocellus.platform.utils.query;

public interface IQueryComponent {

    public static final String TYPE_KEYWORD_SELECT = "select";
    public static final String TYPE_KEYWORD_FROM = "from";
    public static final String TYPE_KEYWORD_WHERE = "where";
    public static final String TYPE_KEYWORD_GROUP = "group";
    public static final String TYPE_KEYWORD_ORDER = "order";
    public static final String TYPE_KEYWORD_BY = "by";
    public static final String TYPE_KEYWORD_AS = "as";
    public static final String TYPE_KEYWORD_AND = "and";
    public static final String TYPE_KEYWORD_OR = "or";
    public static final String TYPE_KEYWORD_INNER = "inner";
    public static final String TYPE_KEYWORD_OUTER = "outer";
    public static final String TYPE_KEYWORD_JOIN = "join";
    public static final String TYPE_KEYWORD_IS = "is";
    public static final String TYPE_KEYWORD_LEFT = "left";
    public static final String TYPE_KEYWORD_RIGHT = "right";
    public static final String TYPE_KEYWORD_NULL = "null";
    public static final String TYPE_KEYWORD_CASE = "case";
    public static final String TYPE_KEYWORD_END = "end";
    public static final String TYPE_KEYWORD_ELSE = "else";
    public static final String TYPE_KEYWORD_ON = "on";
    public static final String TYPE_KEYWORD_UNION = "union";
    public static final String TYPE_KEYWORD_ALL = "all";
    public static final String TYPE_KEYWORD_LIKE = "like";
    public static final String TYPE_KEYWORD_DESC = "desc";
    public static final String TYPE_KEYWORD_NOT = "not";
    public static final String TYPE_OPEN_PARENTHESIS = "(";
    public static final String TYPE_CLOSE_PARENTHESIS = ")";
    public static final String TYPE_COMMA = ",";
    public static final String TYPE_PERIOD = ".";

    public static final String TYPE_OPERATOR_PLUS = "+";
    public static final String TYPE_OPERATOR_MINUS = "-";
    public static final String TYPE_OPERATOR_TIMES = "*";
    public static final String TYPE_OPERATOR_DIVIDE = "/";
    public static final String TYPE_OPERATOR_LESSTHAN = "<";
    public static final String TYPE_OPERATOR_LESSTHAN_EQUAL = "<=";
    public static final String TYPE_OPERATOR_GREATERTHAN = ">";
    public static final String TYPE_OPERATOR_GREATERTHAN_EQUAL = ">=";
    public static final String TYPE_OPERATOR_EQUAL = "=";
    public static final String TYPE_OPERATOR_NOT_EQUAL = "!=";

    public static final String TYPE_UNCATEGORIZED = "[uncategorized]";
    public static final String TYPE_ALIAS = "[alias]";
    public static final String TYPE_QUERY = "[query]";
    public static final String TYPE_UNION_ALL = "[union all]";
    public static final String TYPE_TABLE_ALIAS = "[table alias]";
    public static final String TYPE_TABLE_PERIOD_COLUMN = "[table.column]";
    public static final String TYPE_TABLE_TABLE_COLUMN_DESC = "[table.column desc]";

    public int getStartPos();

    public int getEndPos();

    public String getType();

    public String getValue();

    public String getAlias();

    public String getSql();

    // convinience methods
    public boolean isAlias();

    public boolean isSelect();

    public boolean isOpenParenthesis();

    public boolean isCloseParenthesis();

    public boolean isUnion();

    public boolean isAll();

    public boolean isGroup();

    public boolean isBy();

    public boolean isOrder();

    public boolean isWhere();

    public boolean isFrom();

    public boolean isComma();

    public boolean isUncategorized();

    public boolean isAs();

    public boolean isPeriod();

    public boolean isDesc();

    public boolean isTableColumnDesc();

    public boolean isJoin();

    public boolean isQuery();

    public boolean isOperatorTimes();
}
