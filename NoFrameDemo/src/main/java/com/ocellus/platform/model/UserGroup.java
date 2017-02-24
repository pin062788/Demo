package com.ocellus.platform.model;

import java.util.List;

public class UserGroup extends AbstractModel {

    public static final String RELATED_TYPE = "Group";
    private String groupId;
    private String groupName;
    private String groupDesc;
    private List<User> users;
    private List<Role> roles;

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getRoleNames() {
        String names = "";
        if (roles != null) {
            names = roles.toString();
            names = names.substring(1, names.length() - 1);
        }
        return names;
    }

    public String getUserNames() {
        String usersNames = "";
        if (users != null) {
            usersNames = users.toString();
            usersNames = usersNames.substring(1, usersNames.length() - 1);
        }
        return usersNames;
    }

    @Override
    public String toString() {
        return this.getGroupName();
    }

    @Override
    public void setDBId(String id) {
        // TODO Auto-generated method stub
        this.setGroupId(id);
    }

    @Override
    public String getDBId() {
        // TODO Auto-generated method stub
        return this.getGroupId();
    }
}
