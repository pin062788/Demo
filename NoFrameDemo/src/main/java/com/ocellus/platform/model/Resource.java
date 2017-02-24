package com.ocellus.platform.model;

import java.util.ArrayList;
import java.util.List;

public class Resource extends TreeNode {
    private String resourceId;
    private String resourceCode;
    private String parentResourceCode;
    private String parentResourceName;
    private String resourceName;
    private String resourceDesc;
    private String resourceUrl;
    private String resourceSn;
    private String resourceImg;
    private String resourceType;
    private String resourceTypeDesc;
    private String orderNumber;
    private List<Resource> children;
    private boolean activeModule;
    private String parentModuleCode;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getParentResourceCode() {
        return parentResourceCode;
    }

    public void setParentResourceCode(String parentResourceCode) {
        this.parentResourceCode = parentResourceCode;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceTypeDesc() {
        return resourceTypeDesc;
    }

    public void setResourceTypeDesc(String resourceTypeDesc) {
        this.resourceTypeDesc = resourceTypeDesc;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<Resource> getChildren() {
        if (children == null) {
            children = new ArrayList<Resource>();
        }
        return children;
    }

    public void setChildren(List<Resource> children) {
        this.children = children;
    }

    @Override
    public String getText() {
        // TODO Auto-generated method stub
        return this.getResourceName();
    }

    @Override
    public String getState() {
        if (this.children != null && this.children.size() > 0) {
            return "closed";
        }
        return "open";
    }

    @Override
    public Object getAttribute() {
        return this.getResourceUrl();
    }

    public String getParentResourceName() {
        return parentResourceName;
    }

    public void setParentResourceName(String parentResourceName) {
        this.parentResourceName = parentResourceName;
    }

    public String getResourceSn() {
        return resourceSn;
    }

    public void setResourceSn(String resourceSn) {
        this.resourceSn = resourceSn;
    }

    public boolean isActiveModule() {
        return activeModule;
    }

    public void setActiveModule(boolean activeModule) {
        this.activeModule = activeModule;
    }

    public String getParentModuleCode() {
        return parentModuleCode;
    }

    public void setParentModuleCode(String parentModuleCode) {
        this.parentModuleCode = parentModuleCode;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Resource) {
            if (this.getResourceCode() == null || ((Resource) obj).getResourceCode() == null) {
                return false;
            } else {
                return this.getResourceCode().equals(((Resource) obj).getResourceCode());
            }
        } else {
            return false;
        }
    }

    public String getResourceImg() {
        return resourceImg;
    }

    public void setResourceImg(String resourceImg) {
        this.resourceImg = resourceImg;
    }

    @Override
    public void setDBId(String id) {
        // TODO Auto-generated method stub
        this.setResourceId(id);
        ;
    }

    @Override
    public String getDBId() {
        // TODO Auto-generated method stub
        return this.getResourceId();
    }
}
