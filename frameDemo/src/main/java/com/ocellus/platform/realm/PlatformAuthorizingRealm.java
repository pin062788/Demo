package com.ocellus.platform.realm;


import com.ocellus.platform.model.User;
import com.ocellus.platform.service.*;
import com.ocellus.platform.utils.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

public class PlatformAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RestrictService restrictService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserGroupService userGroupservice;
    @Autowired
    private UserService userService;

    // 获取授权信息
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Subject currentUser = SecurityUtils.getSubject();
        String username = (String) principals.fromRealm(getName()).iterator().next();
        User user = null;
        if (currentUser != null) {
            user = (User) currentUser.getSession().getAttribute(Constants.SESSION_USER_KEY);
            if (user == null) {
                user = userService.getByUserName(username);
            }
        } else if (username != null) {
            user = userService.getByUserName(username);
        }
        if (user != null) {//所属公司
            userService.getUserZygs(user);
        }
        currentUser.getSession().setAttribute(Constants.SESSION_USER_KEY, user);
        return resourceService.getUserPermission(user);
    }

    //  获取认证信息
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        // 通过表单接收的用户名
        String username = token.getUsername();

        if (username != null && !"".equals(username)) {
            User user = userService.getByUserName(username);
            if (user != null) {
                //所属公司
                userService.getUserZygs(user);
                userGroupservice.getUserGroups(user);
                roleService.getUserRoles(user);
                restrictService.searchUserRestrict(user);
                setSession(Constants.SESSION_USER_KEY, user);
                setSession("resourceList", resourceService.getMenu(user));
                return new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), getName());
            }
        }
        return null;
    }

    private void setSession(Object key, Object value) {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser != null) {
            Session session = currentUser.getSession();
            if (null != session) {
                session.setAttribute(key, value);
            }
        }
    }
}