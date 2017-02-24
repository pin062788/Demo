package com.ocellus.platform.model;


public class GroupUserMapping extends AbstractModel {
    private static final long serialVersionUID = 1L;
    private String id;
    private String groupId;
    private String userId;

    public String getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
