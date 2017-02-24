package com.ocellus.platform.model;

public class RestrictColumn extends AbstractModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String columnId;
    private String tableId;
    private String tableName;
    private String columnName;
    private String columnDesc;
    private String columnType;
    private String dataSource;

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        this.columnDesc = columnDesc;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void setDBId(String id) {
        // TODO Auto-generated method stub
        this.setColumnId(id);
    }

    @Override
    public String getDBId() {
        // TODO Auto-generated method stub
        return this.getColumnId();
    }


}
