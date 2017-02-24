package com.ocellus.platform.dao;

import com.ocellus.platform.model.Resource;
import com.ocellus.platform.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ResourceDAO extends BaseDAO<Resource, String> {
    List<Resource> findByRoleId(String roleId);

    public List<Resource> searchByParentCode(String parentcode);

    public Resource getByCode(String code);

    public int activeModule(String[] resourceNames);

    public void inactiveModule();

    public List<Resource> selectAll();

    public List<Resource> findMenuResourceByUserName(String userName);

    List<Resource> findUserResources(Role[] userRoles);

    public void batchDelete(String[] resourceIds);

    public void deleteByCode(String resourceCode);

    public List<Resource> selectShortcutMenu(Map params);//查询快捷菜单
}
