package com.ocellus.platform.service;

import com.ocellus.platform.dao.GroupUserMappingDAO;
import com.ocellus.platform.dao.GroupUserRoleMappingDAO;
import com.ocellus.platform.dao.UserDAO;
import com.ocellus.platform.dao.UserGroupDAO;
import com.ocellus.platform.model.*;
import com.ocellus.platform.utils.Constants;
import com.ocellus.platform.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends AbstractService<User, String> {
    private UserDAO userDAO;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserGroupDAO userGroupDAO;

    @Autowired
    private GroupUserMappingDAO groupUserMappingDAO;
    @Autowired
    private GroupUserRoleMappingDAO groupUserRoleMappingDAO;
    @Autowired
    private OrganizationService orgService;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        super.setDao(userDAO);
        this.userDAO = userDAO;
    }

    public List<User> getUsers(Map params) {
        List<User> users = search(params);
        if (users != null && users.size() > 0) {
            for (User user : users) {
                user.setRoles(roleService.getUserRoles(user.getUserId()));
                user.setGroups(userGroupDAO.getUserGroups(user.getUserId()));
            }
        }
        return users;
    }

    public void saveUserRole(User user) {
        groupUserRoleMappingDAO.deleteByReleated(new Releated(user.getUserId(), User.RELATED_TYPE));
        GroupUserRoleMapping gr = new GroupUserRoleMapping();
        gr.setReleatedId(user.getUserId());
        gr.setReleatedType(User.RELATED_TYPE);
        gr.setId(StringUtil.getGUID());
        for (Role role : user.getRoles()) {
            if (!StringUtil.isEmpty(role.getRoleId())) {
                gr.setRoleId(role.getRoleId());
                groupUserRoleMappingDAO.insert(gr);
            }
        }
    }

    public void saveGroupUser(User user) {
        GroupUserMapping gu = new GroupUserMapping();
        gu.setUserId(user.getUserId());
        gu.setId(StringUtil.getGUID());
        groupUserMappingDAO.deleteByUserId(user.getUserId());
        for (UserGroup userGroup : user.getGroups()) {
            if (!StringUtil.isEmpty(user.getUserId())) {
                gu.setGroupId(userGroup.getGroupId());
                groupUserMappingDAO.insert(gu);
            }

        }
    }

    public User save(User user) throws Exception {
        if (StringUtils.isEmpty(user.getUserId())) {
            insert(user);
        } else {
            update(user);
        }
        if (user.getRoles() != null) {
            saveUserRole(user);
        }
        if (user.getGroups() != null) {
            saveGroupUser(user);
        }
        return user;

    }

    public List<User> getGroupUsers(String groupId) {
        return userDAO.getGroupUsers(groupId);
    }

    public boolean isDuplicated(User user) {
        List<User> users = userDAO.searchDuplicated(user);
        return !users.isEmpty();
    }

    public User getLoginUser() {
        User user = null;
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            if (request != null) {
                HttpSession session = request.getSession();
                user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
                if (user == null) {
                    Subject currentUser = SecurityUtils.getSubject();
                    String userName = currentUser.getPrincipal() + "";
                    user = userDAO.getByUserName(userName);
                    if (user != null) {
                        session.setAttribute(Constants.SESSION_USER_KEY, user);
                    }
                }
            }
        }
        return user;
    }

    public Organization getUserZygs(User user) {
        if (user != null) {
            if (user.getSczygs() == null) {
                if (User.RELATED_TYPE_EMPLOYEE.equals(user.getRelatedType())) {
                    if (user.getRelatedId() != null) {
                        Organization org = orgService.getById(user.getRelatedId());
                        user.setSczygs(orgService.getZYGS(org));
                    }
                }
            }
            return user.getSczygs();
        }
        return null;
    }

    public User getByUserName(String username) {
        return userDAO.getByUserName(username);
    }
}
