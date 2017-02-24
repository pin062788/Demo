package com.ocellus.platform.dao;


import com.ocellus.platform.model.GroupUserRoleMapping;
import com.ocellus.platform.model.Releated;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUserRoleMappingDAO extends BaseDAO<GroupUserRoleMapping, String> {
    int deleteByReleated(Releated releated);

    int batchDelete(String[] roleIds);
}
