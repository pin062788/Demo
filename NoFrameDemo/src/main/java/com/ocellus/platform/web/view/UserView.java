package com.ocellus.platform.web.view;

import com.ocellus.platform.model.Role;
import com.ocellus.platform.model.User;

import java.util.Date;
import java.util.List;

public class UserView extends User {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getUserId() {
        // TODO Auto-generated method stub
        return user.getUserId();
    }

    @Override
    public String getUserName() {
        // TODO Auto-generated method stub
        return user.getUserName();
    }

    public String getRelatedType() {

        return user.getRelatedType();
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return user.getPassword();
    }


    @Override
    public Date getAddDate() {
        return user.getAddDate();
    }

    @Override
    public String getAddUser() {
        return user.getAddUser();
    }


    public String getRoleNames() {
        String roleNames = "";
        List<Role> roles = getRoles();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                roleNames += "[" + role.getRoleName() + "]";
            }
        }
        return roleNames;
    }

    @Override
    public Date getEditDate() {
        // TODO Auto-generated method stub
        return user.getEditDate();
    }

    @Override
    public String getEditUser() {
        // TODO Auto-generated method stub
        return user.getEditUser();
    }

    public String getRelatedName() {
        return user.getRelatedName();
    }


}
