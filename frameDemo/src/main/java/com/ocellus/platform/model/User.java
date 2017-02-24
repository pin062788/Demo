package com.ocellus.platform.model;

import org.apache.shiro.authz.SimpleAuthorizationInfo;

import java.util.*;

@SuppressWarnings("serial")
public class User extends AbstractModel {
    public static final String RELATED_TYPE = "User";
    public static final String RELATED_TYPE_CUSTOMER = "customer";
    public static final String RELATED_TYPE_SUPPLIER = "supplier";
    public static final String RELATED_TYPE_EMPLOYEE = "employee";
    public static final String SPICAIL_PARAM_ENTERPRISE_CODE = "enterpriseCode";
    public static final String SPICAIL_PARAM_DOMAIN_IDS = "domainIds";
    public static final String SPICAIL_PARAM_REPERTORY_IDS = "repertoryIds";
    public static final String SPICAIL_PARAM_UNIT_ID = "unitId";
    private String userId;
    private String userName;
    private String password;
    private String theme;
    private Date lastLoginTime;
    private String relatedType;
    private String relatedId;
    private String relatedName;
    private String configure;
    private List<Resource> resource;
    private List<Resource> menuResource;
    private List<Role> roles;
    private List<UserGroup> groups;
    private List<Restrict> restricts;
    private SimpleAuthorizationInfo permissions;
    private Map userPeculiarParams;
    private List<String> msg;
    private List<String> sendMsg = new ArrayList<String>();
    //private String userName;
    private String toUser;
    private Organization sczygs;//所属专业公司

    public List<String> getMsg() {
        return msg;
    }

    public void setMsg(List<String> msg) {
        this.msg = msg;
    }


    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public List<String> getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(List<String> sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getRelatedType() {
        return relatedType;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public List<Resource> getMenuResource() {
        return menuResource;
    }

    public void setRelatedType(String relatedType) {
        this.relatedType = relatedType;
    }

    public void setRelatedId(String relatedId) {
        this.relatedId = relatedId;
    }

    public void setMenuResource(List<Resource> menuResource) {
        this.menuResource = menuResource;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRelatedName() {
        return relatedName;
    }

    public void setRelatedName(String relatedName) {
        this.relatedName = relatedName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<UserGroup> groups) {
        this.groups = groups;
    }

    public String getRoleNames() {
        String names = "";
        if (roles != null) {
            names = roles.toString();
            names = names.substring(1, names.length() - 1);
        }
        return names;
    }

    public String getGroupNames() {
        String groupNames = "";
        if (groups != null) {
            groupNames = groups.toString();
            groupNames = groupNames.substring(1, groupNames.length() - 1);
        }
        return groupNames;
    }

    public Map getUserPeculiarParams() {
        if (userPeculiarParams == null) {
            userPeculiarParams = new HashMap();
        }
        return userPeculiarParams;
    }


    public void setUserPeculiarParams(Map userPeculiarParams) {
        this.userPeculiarParams = userPeculiarParams;
    }

    @Override
    public String toString() {
        return this.getUserName();
    }

    public List<Restrict> getRestricts() {
        return restricts;
    }

    public void setRestricts(List<Restrict> restricts) {
        this.restricts = restricts;
    }

    public void addRestricts(List<Restrict> restricts) {
        if (this.restricts == null) {
            this.restricts = new ArrayList<Restrict>();
        }
        this.restricts.addAll(restricts);
    }

    public void addRole(Role role) {
        if (roles == null) {
            roles = new ArrayList<Role>();
        }
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    public SimpleAuthorizationInfo getPermission() {
        return permissions;
    }

    public void setPermission(SimpleAuthorizationInfo permissions) {
        this.permissions = permissions;
    }

    public List<Resource> getResource() {
        return resource;
    }

    public void setResource(List<Resource> resource) {
        this.resource = resource;
    }

    public String getConfigure() {
        return configure;
    }

    public void setConfigure(String configure) {
        this.configure = configure;
    }

    public Organization getSczygs() {
        return sczygs;
    }

    public void setSczygs(Organization sczygs) {
        this.sczygs = sczygs;
    }

    @Override
    public void setDBId(String id) {
        // TODO Auto-generated method stub
        this.setUserId(id);
    }

    @Override
    public String getDBId() {
        // TODO Auto-generated method stub
        return this.getUserId();
    }

}
