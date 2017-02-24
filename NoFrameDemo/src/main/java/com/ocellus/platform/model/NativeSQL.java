package com.ocellus.platform.model;

public class NativeSQL {
    private String sql;

    public NativeSQL() {
    }

    public NativeSQL(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
