package com.ocellus.platform.model;


public class GroupUserRoleMapping extends AbstractModel {
    private static final long serialVersionUID = 1L;
    private String id;
    private String releatedType;
    private String releatedId;
    private String roleId;

    public String getId() {
        return id;
    }

    public String getReleatedType() {
        return releatedType;
    }

    public String getReleatedId() {
        return releatedId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setReleatedType(String releatedType) {
        this.releatedType = releatedType;
    }

    public void setReleatedId(String releatedId) {
        this.releatedId = releatedId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public void setDBId(String id) {
        // TODO Auto-generated method stub
        this.setId(id);
    }

    @Override
    public String getDBId() {
        // TODO Auto-generated method stub
        return this.getId();
    }

}
