package com.ocellus.platform.model;


public class UserLinkRole extends AbstractModel {
    private static final long serialVersionUID = 1L;
    private String id;
    private User user;
    private Role role;

    public String getDBId() {
        return id;
    }

    public void setDBId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
