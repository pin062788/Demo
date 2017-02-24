package com.ocellus.platform.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.utils.WebUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractModel implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    protected String activate;	//可用标识
    protected String addUser;
    protected Date addDate;
    protected String editUser;
    protected Date editDate;

    public String getAddUser() {
        if (!StringUtil.isEmpty(addUser)) {
            return addUser;
        } else {
            return WebUtil.getLoginUserName();
        }
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }

    public Date getAddDate() {
        if (addDate != null) {
            return addDate;
        } else {
            return new Date();
        }
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getEditDate() {
        if (editDate != null) {
            return editDate;
        } else {
            return new Date();
        }
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public String getEditUser() {
        if (!StringUtil.isEmpty(editUser)) {
            return editUser;
        } else {
            return WebUtil.getLoginUserName();
        }
    }

    public void setEditUser(String editUser) {
        this.editUser = editUser;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        try {
            String str = mapper.writeValueAsString(this);
            if (str.indexOf("'") > 0) {
                str = str.replaceAll("'", "&#180;");
            }
            return str;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return this.getClass().getName() + "转成字符串时失败";
        }
    }

    public AbstractModel clone() {
        AbstractModel o = null;
        try {
            o = (AbstractModel) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public abstract void setDBId(String id);

    public abstract String getDBId();

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }
}
