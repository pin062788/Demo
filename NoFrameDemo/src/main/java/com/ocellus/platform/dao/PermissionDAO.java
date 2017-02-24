package com.ocellus.platform.dao;

import com.ocellus.platform.model.Permission;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface PermissionDAO extends BaseDAO<Permission, String> {
    void insertBatch(Map map);

    int batchDelete(String[] roleIds);
}
