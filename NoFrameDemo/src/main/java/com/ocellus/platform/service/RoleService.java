package com.ocellus.platform.service;

import com.ocellus.platform.dao.GroupUserRoleMappingDAO;
import com.ocellus.platform.dao.PermissionDAO;
import com.ocellus.platform.dao.ResourceDAO;
import com.ocellus.platform.dao.RoleDAO;
import com.ocellus.platform.model.*;
import com.ocellus.platform.utils.NumberUtil;
import com.ocellus.platform.utils.ParamUtil;
import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.utils.WebUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleService extends AbstractService<Role, String> {
    private RoleDAO roleDAO;
    @Autowired
    private ResourceDAO resourceDAO;

    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private GroupUserRoleMappingDAO gurMappingDAO;
    @Autowired
    private RestrictService restrictService;
    @Autowired
    private RestrictTableService tableService;
    @Autowired
    private RestrictColumnService columnService;
    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    public void setRoleDAO(RoleDAO roleDAO) {
        super.setDao(roleDAO);
        this.roleDAO = roleDAO;
    }

    /**
     * @param userId
     * @return 不包含用户组的role
     */
    public List<Role> getUserRoles(String userId) {
        return roleDAO.getUserRoles(userId);
    }

    /**
     * @param user
     * @return user的所有role及user所在组的role
     */
    public List<Role> getUserRoles(User user) {
        List<Role> roles = user.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = roleDAO.getUserRoles(user.getUserId());
            Set<Role> roleSet = new HashSet<Role>(roles);
            List<UserGroup> userGroups = userGroupService.getUserGroups(user);
            for (UserGroup group : userGroups) {
                roleSet.addAll(roleDAO.getGroupRoles(group.getGroupId()));
            }
            roles = new ArrayList<Role>(roleSet);
            user.setRoles(roles);
        }
        return roles;
    }

    public Role getByCode(String roleCode) {
        return roleDAO.getByCode(roleCode);
    }

    public Role save(Role vo) {
        Role po = null;
        boolean insert = false;
        if (StringUtil.isEmpty(vo.getRoleId())) {
            po = new Role();
            po.setAddDate(new Date());
            po.setAddUser(WebUtil.getLoginUserName());
            insert = true;
        } else {
            po = getById(vo.getRoleId());
        }
        BeanUtils.copyProperties(vo, po, "roleId");
        po.setEditDate(new Date());
        po.setEditUser(WebUtil.getLoginUserName());
        if (insert) {
            insert(po);
        } else {
            update(po);
        }
        return po;
    }

    public List<Role> getGroupRoles(String groupId) {
        return roleDAO.getGroupRoles(groupId);
    }

    public List<Resource> searchRoleResource(String roleId) {
        return resourceDAO.findByRoleId(roleId);
    }

    public void savePermission(String roleId, String resources) {
        permissionDAO.delete(roleId);
        String[] resourceCods = resources.split(",");
        List<Permission> ps = new ArrayList<Permission>();
        Permission permission = null;
        for (int i = 0; i < resourceCods.length; i++) {
            permission = new Permission(roleId, resourceCods[i]);
            permission.setPermissionId(StringUtil.getGUID());
            ps.add(permission);
        }
        Map map = new HashMap();
        map.put("list", ps);
        permissionDAO.insertBatch(map);
    }

    public void saveDataConfig(Map<String, String> params) {
        String tableId = params.get("tableId");
        String roleId = params.get("roleId");
        RestrictTable table = tableService.getById(tableId);
        Set<String> paramNames = params.keySet();
        Map<String, Restrict> restrictMap = new HashMap<String, Restrict>();
        Restrict restrict = null;
        //delete old data
        restrictService.deleteBatch(ParamUtil.setParam("tableName", table.getTableName()).setParam("roleId", roleId));
        //save new data
        for (String paramName : paramNames) {
            String paramValue = params.get(paramName);
            if (isRestrictProp(paramName)) {
                String[] arr = paramName.split("_");
                String propName = arr[0];
                String index = arr[1];
                if (!restrictMap.containsKey(index)) {
                    restrict = new Restrict();
                    restrict.setTableName(table.getTableName());
                    restrict.setRoleId(roleId);
                    restrict.setOrderNumber(NumberUtil.toInteger(index));
                    restrictMap.put(index, restrict);
                } else {
                    restrict = restrictMap.get(index);
                }

                if (paramName.contains("connOpt")) {
                    restrict.setConnOpt(paramValue);
                } else if (propName.contains("columnName")) {
                    restrict.setColumnName(paramValue);
                    List<RestrictColumn> columns = columnService.search(ParamUtil.setParam("tableId", tableId).setParam("columnName", paramValue));
                    if (columns != null && columns.size() > 0) {
                        restrict.setColumnType(columns.iterator().next().getColumnType());
                    }
                } else if (propName.contains("optCode")) {
                    restrict.setOptCode(paramValue);
                    ;
                } else if (propName.contains("restrictValue")) {
                    restrict.setRestrictValue(paramValue);
                    ;
                }
            }

        }
        Collection<Restrict> restricts = restrictMap.values();

        for (Restrict r : restricts) {
            restrictService.insert(r);
        }
    }

    private boolean isRestrictProp(String propName) {
        boolean result = false;
        if (propName.contains("connOpt") || propName.contains("columnName") || propName.contains("optCode") || propName.contains("restrictValue")) {
            result = true;
        }

        return result;
    }


    public void batchDelete(String roleIds) {
        String[] roleIdArray = roleIds.split(",");
        gurMappingDAO.batchDelete(roleIdArray);
        permissionDAO.batchDelete(roleIdArray);
        roleDAO.batchDelete(roleIdArray);
    }

}
