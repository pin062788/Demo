package com.ocellus.platform.model;

import java.util.Date;

public class TodoTask extends AbstractModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String tdlId;
    private String eventCode;
    private String taskStatus;
    private String toDoTaskTitle;
    private String toDoTaskUrl;
    private Date createTime;
    private String remark;
    private String receiverName;
    private String departmentName;
    private User user;

    public String getTdlId() {
        return tdlId;
    }

    public void setTdlId(String tdlId) {
        this.tdlId = tdlId;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToDoTaskTitle() {
        return toDoTaskTitle;
    }

    public void setToDoTaskTitle(String toDoTaskTitle) {
        this.toDoTaskTitle = toDoTaskTitle;
    }

    public String getToDoTaskUrl() {
        return toDoTaskUrl;
    }

    public void setToDoTaskUrl(String toDoTaskUrl) {
        this.toDoTaskUrl = toDoTaskUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public void setDBId(String id) {
        // TODO Auto-generated method stub
        this.setTdlId(id);
    }

    @Override
    public String getDBId() {
        // TODO Auto-generated method stub
        return this.getTdlId();
    }
}
