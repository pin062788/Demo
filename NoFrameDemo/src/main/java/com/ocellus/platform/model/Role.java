package com.ocellus.platform.model;

import com.ocellus.platform.utils.StringUtil;

import java.util.List;

public class Role extends AbstractModel {
    private String roleId;
    private String roleCode;
    private String roleName;
    private String remark;
    private List<Resource> resource;
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Resource> getResource() {
        return resource;
    }

    public void setResource(List<Resource> resource) {
        this.resource = resource;
    }

    @Override
    public int hashCode() {
        return roleId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Role role = (Role) obj;
        if (!StringUtil.isEmpty(role.getRoleId()) && !StringUtil.isEmpty(this.getRoleId())) {
            return this.getRoleId().equals(role.getRoleId());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return this.getRoleName();
    }

    @Override
    public void setDBId(String id) {
        // TODO Auto-generated method stub
        this.setRoleId(id);
    }

    @Override
    public String getDBId() {
        // TODO Auto-generated method stub
        return this.getRoleId();
    }

}
