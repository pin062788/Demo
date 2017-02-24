package com.ocellus.platform.utils.query;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;


public class QueryScanner {

    private static final Map keyworkMap = initKeyworkMap();

    public QueryScanner(final String sql) {
        this.sql = sql;
    }

    private final StringBuffer currentBuffer = new StringBuffer();

    private final List componentList = new ArrayList();

    private String sql;

    private int index = -1;

    private char currentChar;

    public void scan() {

        while (ready()) {
            read();

            process(currentChar);
        }
        index++;
        flushBuffer();
    }

    public List getComponentList() {
        return componentList;
    }

    public String getSql() {
        return sql;
    }

    protected void process(final char c) {
        switch (c) {
            case '(':
            case ')':
            case '=':
            case '.':
            case ',':
            case '+':
            case '-':
            case '*':
            case '/':
                flushBuffer();
                add(c, index);
                break;
            case '<':
            case '>':
            case '!':
                read();
                final String type = (String) keyworkMap.get(String.valueOf(c) + currentChar);
                if (type != null) {
                    flushBuffer();
                    add(type, index - type.length(), index - 1);
                } else {
                    flushBuffer();
                    add(c, index - 1);
                    process(currentChar); // we have not processed this yet
                }
                break;
            default:
                if (Character.isWhitespace(c)) {
                    flushBuffer();
                } else {
                    currentBuffer.append(c);
                }
        }
    }

    protected void flushBuffer() {
        if (currentBuffer.toString().trim().length() > 0) {
            final String type = (String) keyworkMap.get(currentBuffer.toString().toLowerCase());
            if (type != null) {
                add(type, index - type.length(), index - 1);
            } else {
                add(IQueryComponent.TYPE_UNCATEGORIZED, index - currentBuffer.length(), index - 1);
            }
        }
        currentBuffer.setLength(0);
    }

    protected void add(final String aKind, final int beg, final int end) {
        componentList.add(new QueryComponent(sql, aKind, beg, end));
    }

    protected void add(final char aKind, final int beg) {
        componentList.add(new QueryComponent(sql, String.valueOf(aKind), beg, beg));
    }

    protected void read() {
        currentChar = getSql().charAt(++index);
    }

    protected boolean ready() {
        if (getSql() == null)
            return false;
        else
            return index < getSql().length() - 1;
    }

    protected static Map initKeyworkMap() {
        final Field[] fields = QueryComponent.class.getFields();
        final List list = new ArrayList();
        for (int i = 0; i < fields.length; i++) {
            final Field field = fields[i];
            final String name = field.getName();
            if (Modifier.isStatic(field.getModifiers()) && name.startsWith("TYPE_")) {
                try {
                    final String value = (String) field.get(QueryComponent.class);
                    if (!value.startsWith("[")) { // brakets indicate composite type (like Query), not needed for parsing so we can exclude
                        list.add(value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

        final Map map = new HashMap();
        for (final Iterator it = list.iterator(); it.hasNext(); ) {
            final String type = (String) it.next();
            map.put(type, type);
        }
        return map;
    }
}
