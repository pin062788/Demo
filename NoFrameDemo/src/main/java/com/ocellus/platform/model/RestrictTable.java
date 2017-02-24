package com.ocellus.platform.model;

public class RestrictTable extends AbstractModel {
    private String tableId;
    private String tableName;
    private String tableDesc;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableDesc() {
        return tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }

    @Override
    public void setDBId(String id) {
        // TODO Auto-generated method stub
        this.setTableId(tableId);
    }

    @Override
    public String getDBId() {
        // TODO Auto-generated method stub
        return this.getTableId();
    }

}
