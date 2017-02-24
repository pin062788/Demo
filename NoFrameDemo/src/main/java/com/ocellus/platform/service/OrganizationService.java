package com.ocellus.platform.service;

import com.ocellus.platform.dao.OrganizationDAO;
import com.ocellus.platform.model.Organization;
import com.ocellus.platform.model.TreeNode;
import com.ocellus.platform.utils.ParamUtil;
import com.ocellus.platform.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService extends AbstractService<Organization, String> {
    private OrganizationDAO organizationDAO;


    @Autowired
    public void setOrganizationDAO(OrganizationDAO organizationDAO) {
        super.setDao(organizationDAO);
        this.organizationDAO = organizationDAO;
    }


    public Organization save(Organization org) {
        if (org != null) {
            if (StringUtil.isEmpty(org.getOrgId())) {
                insert(org);
            } else {
                update(org);
            }

        }
        return org;
    }

    public void deleteById(String orgId, Boolean deleteChildren) {

        Organization org = new Organization();
        org.setOrgId(orgId);
        org.setIsDelete(true);
        update(org);
        if (deleteChildren) {
            List<Organization> children = searchByParentId(orgId);
            for (Organization child : children) {
                deleteById(child.getOrgId(), deleteChildren);
            }
        }
    }

    public List<Organization> searchByParentId(String parentId) {
        return organizationDAO.searchByParentId(parentId);
    }

    public List<TreeNode> getChildTreeNodes(String parentId) {
        return organizationDAO.getChildTreeNodes(parentId);
    }

    public List<Organization> getAll() {
        return organizationDAO.selectAll();
    }

    public Organization getByCode(String code) {
        return organizationDAO.getByCode(code);
    }

    /**
     * @return 专业公司
     */
    public List<Organization> getZYGS() {
        return organizationDAO.search(ParamUtil.setParam("orgFunctionTypeCode", "1"));
    }

    /**
     * @return 储运公司
     */
    public List<Organization> getCYGS() {
        return organizationDAO.search(ParamUtil.setParam("orgFunctionTypeCode", "2"));
    }

    public Organization getZYGS(Organization org) {
        if (org != null) {
            if ("1".equals(org.getOrgFunctionTypeCode())) {
                return org;
            } else {
                return getZYGS(org.getParentOrg());
            }
        } else {
            return null;
        }

    }
}
