package com.ocellus.platform.model;


public class Permission extends AbstractModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String permissionId;
    private String roleCode;
    private String resourceCode;
    private String permissionType;

    public Permission() {
    }

    public Permission(String roleCode, String resourceCode) {
        this.roleCode = roleCode;
        this.resourceCode = resourceCode;

    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

    @Override
    public void setDBId(String id) {
        // TODO Auto-generated method stub
        this.setPermissionId(id);
    }

    @Override
    public String getDBId() {
        // TODO Auto-generated method stub
        return this.getPermissionId();
    }

}
