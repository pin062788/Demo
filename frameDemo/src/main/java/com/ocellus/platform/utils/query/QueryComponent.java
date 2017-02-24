package com.ocellus.platform.utils.query;


public class QueryComponent extends AbstractQueryComponent {

    private final int startPos;

    private final int endPos;

    public QueryComponent(final String sql, final String type, final int aStartPos, final int aEndPos) {
        super(sql, type);
        this.startPos = aStartPos;
        this.endPos = aEndPos;
    }

    public String toString() {
        return getSql().substring(getStartPos(), getEndPos() + 1);
    }

    public String getValue() {
        return toString();
    }

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return endPos;
    }
}
