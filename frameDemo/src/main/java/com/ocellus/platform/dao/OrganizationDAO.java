package com.ocellus.platform.dao;

import com.ocellus.platform.model.Organization;
import com.ocellus.platform.model.TreeNode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationDAO extends BaseDAO<Organization, String> {
    public List<Organization> searchByParentId(String parentId);

    public Organization getById(String id);

    public List<Organization> selectAll();

    public Organization getByCode(String code);

    public List<TreeNode> getChildTreeNodes(String parentId);
}
