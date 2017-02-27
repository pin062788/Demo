package com.ocellus.platform.service;


import com.ocellus.platform.dao.GroupUserMappingDAO;
import com.ocellus.platform.dao.GroupUserRoleMappingDAO;
import com.ocellus.platform.dao.UserGroupDAO;
import com.ocellus.platform.model.*;
import com.ocellus.platform.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserGroupService extends AbstractService<UserGroup, String> {
    private UserGroupDAO userGroupDAO;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupUserMappingDAO groupUserMappingDAO;
    @Autowired
    private GroupUserRoleMappingDAO groupUserRoleMappingDAO;

    @Autowired
    public void setUserDAO(UserGroupDAO userGroupDAO) {
        super.setDao(userGroupDAO);
        this.userGroupDAO = userGroupDAO;
    }

    @RequiresPermissions("userGroup:create")
    public void createUser() {
        System.out.print("invoke");
    }

    public List<UserGroup> getUserGroups(String userId) {
        return userGroupDAO.getUserGroups(userId);
    }

    public List<UserGroup> getUserGroups(User user) {
        List<UserGroup> groups = user.getGroups();
        if (groups == null || groups.isEmpty()) {
            groups = userGroupDAO.getUserGroups(user.getUserId());
            user.setGroups(groups);
        }
        return groups;
    }

    public List<UserGroup> search(Map params) {
        List<UserGroup> userGroups = userGroupDAO.search(params);
        for (UserGroup group : userGroups) {
            group.setRoles(roleService.getGroupRoles(group.getGroupId()));
            group.setUsers(userService.getGroupUsers(group.getGroupId()));
        }
        return userGroups;
    }

    public boolean isDuplicated(UserGroup userGroup) {
        List<UserGroup> groups = userGroupDAO.searchDuplicated(userGroup);
        return !groups.isEmpty();
    }

    public UserGroup save(UserGroup userGroup) throws Exception {
        if (StringUtils.isEmpty(userGroup.getGroupId())) {
            insert(userGroup);
        } else {
            update(userGroup);
        }
        if (userGroup.getUsers() != null) {
            saveGroupUser(userGroup);
        }
        if (userGroup.getRoles() != null) {
            saveGroupRole(userGroup);
        }
        return userGroup;
    }

    public void saveGroupRole(UserGroup userGroup) {
        groupUserRoleMappingDAO.deleteByReleated(new Releated(userGroup.getGroupId(), UserGroup.RELATED_TYPE));
        GroupUserRoleMapping gr = new GroupUserRoleMapping();
        gr.setReleatedId(userGroup.getGroupId());
        gr.setReleatedType(UserGroup.RELATED_TYPE);
        gr.setId(StringUtil.getGUID());
        for (Role role : userGroup.getRoles()) {
            if (!StringUtil.isEmpty(role.getRoleId())) {
                gr.setRoleId(role.getRoleId());
                groupUserRoleMappingDAO.insert(gr);
            }
        }
    }

    public void saveGroupUser(UserGroup userGroup) {
        GroupUserMapping gu = new GroupUserMapping();
        gu.setGroupId(userGroup.getGroupId());
        gu.setId(StringUtil.getGUID());
        groupUserMappingDAO.deleteByGroupId(userGroup.getGroupId());
        for (User user : userGroup.getUsers()) {
            if (!StringUtil.isEmpty(user.getUserId())) {
                gu.setUserId(user.getUserId());
                groupUserMappingDAO.insert(gu);
            }

        }
    }

    public void deleteByIds(String[] groupIds) {
        for (String id : groupIds) {
            userGroupDAO.deleteById(id);
            groupUserMappingDAO.deleteByGroupId(id);
            groupUserRoleMappingDAO.deleteByReleated(new Releated(id, UserGroup.RELATED_TYPE));
        }

    }
}
