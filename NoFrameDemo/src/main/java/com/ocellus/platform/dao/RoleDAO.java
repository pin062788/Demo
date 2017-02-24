package com.ocellus.platform.dao;

import com.ocellus.platform.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDAO extends BaseDAO<Role, String> {
    List<Role> getUserRoles(String userId);

    List<Role> getGroupRoles(String groupId);

    Role getByCode(String roleCode);

    void batchDelete(String[] roleIds);
}
