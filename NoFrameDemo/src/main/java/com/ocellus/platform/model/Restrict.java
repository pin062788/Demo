package com.ocellus.platform.model;

public class Restrict extends AbstractModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String restrictId;
    private String roleId;
    private String tableName;
    private String columnName;
    private String columnType;
    private String optCode;
    private String connOpt;
    private String restrictValue;
    private Integer orderNumber;

    public String getRestrictId() {
        return restrictId;
    }

    public void setRestrictId(String restrictId) {
        this.restrictId = restrictId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    public String getConnOpt() {
        return connOpt;
    }

    public void setConnOpt(String connOpt) {
        this.connOpt = connOpt;
    }

    public String getRestrictValue() {
        return restrictValue;
    }

    public void setRestrictValue(String restrictValue) {
        this.restrictValue = restrictValue;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof Restrict)) {
            return false;
        } else {
            return this.getRestrictId().equals(((Restrict) obj).getRestrictId());
        }
    }

    @Override
    public void setDBId(String id) {
        // TODO Auto-generated method stub
        this.setRestrictId(id);
    }

    @Override
    public String getDBId() {
        // TODO Auto-generated method stub
        return this.getRestrictId();
    }

}
