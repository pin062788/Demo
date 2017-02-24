package com.ocellus.platform.model;

import java.util.Date;

public class Organization extends TreeNode {
    private static final long serialVersionUID = 1L;
    private String orgId;//组织机构Id
    private String parentOrgId;//上级组织机构Id
    private String orgCode;//组织行业统一编码
    private String orgName;//组织机构名称
    private String orgDesc;//组织机构描述
    private String orgAddress;//组织机构地址
    private String orgType;//组织机构类型编码
    private String orgFunctionTypeCode;//机构职能类别编码
    private String expire;//是否可用
    private Date expireDate;//停用时间
    private String orgOrder;//组织机构排列顺序
    private Boolean isDelete;
    private Organization parentOrg;

    public String getOrgId() {
        return orgId;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public String getExpire() {
        return expire;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public String getOrgOrder() {
        return orgOrder;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public void setOrgOrder(String orgOrder) {
        this.orgOrder = orgOrder;
    }

    public String getOrgDesc() {
        return orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    public Boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Organization) {
            if (this.getOrgId() == null || ((Organization) obj).getOrgId() == null) {
                return false;
            } else {
                return this.getOrgId().equals(((Organization) obj).getOrgId());
            }
        } else {
            return false;
        }
    }

    public Organization getParentOrg() {
        return parentOrg;
    }

    public void setParentOrg(Organization parentOrg) {
        if (parentOrg != null) {
            this.parentOrgId = parentOrg.getOrgId();
            this.parentOrg = parentOrg;
            this.parentOrg.setIsParent("true");
        }
    }

    public String getOrgFunctionTypeCode() {
        return orgFunctionTypeCode;
    }

    public void setOrgFunctionTypeCode(String orgFunctionTypeCode) {
        this.orgFunctionTypeCode = orgFunctionTypeCode;
    }

    @Override
    public void setDBId(String id) {
        this.orgId = id;

    }

    @Override
    public String getDBId() {
        return orgId;
    }
}