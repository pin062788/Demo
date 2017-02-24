package com.ocellus.platform.controller;

import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.model.Role;
import com.ocellus.platform.model.User;
import com.ocellus.platform.model.UserGroup;
import com.ocellus.platform.service.RoleService;
import com.ocellus.platform.service.UserGroupService;
import com.ocellus.platform.service.UserService;
import com.ocellus.platform.utils.WebUtil;
import com.ocellus.platform.web.view.AjaxView;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/userGroup")
public class UserGroupController extends BaseController {
    private static Logger logger = Logger.getLogger(UserGroupController.class);
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public ModelAndView showList() {
        ModelAndView mv = new ModelAndView("/userGroup/userGroup");
        return mv;
    }

    @RequestMapping("/getList")
    @ResponseBody
    @RequiresPermissions("usergroup:view")
    public PageResponse getUserList(HttpServletRequest request) {
        List<UserGroup> userGroups = userGroupService.search(this.getParamMapWithPage(request));
        return getPageResponse(userGroups);
    }

    @RequestMapping("/add")
    @RequiresPermissions("usergroup:add")
    public ModelAndView add(@RequestParam("userGroupId") String userId) {
        return showDetail(userId);
    }

    @RequestMapping("/edit")
    @RequiresPermissions("usergroup:edit")
    public ModelAndView edit(@RequestParam("userGroupId") String userId) {
        return showDetail(userId);
    }

    @RequestMapping("/showDetail")
    public ModelAndView showDetail(@RequestParam("userGroupId") String userGroupId) {
        Boolean isNew = userGroupId.equals("0");
        ModelAndView mv = new ModelAndView("/userGroup/userGroupDetail");
        UserGroup userGroup = !isNew ? userGroupService.getById(userGroupId) : new UserGroup();
        List<Role> selectedRoles = !isNew ? roleService.getGroupRoles(userGroupId) : new ArrayList<Role>();
        List<Role> availableRoles = roleService.search(new HashMap());
        if (availableRoles != null && availableRoles.size() > 0 && selectedRoles != null && selectedRoles.size() > 0) {
            availableRoles.removeAll(selectedRoles);
        }
        List<User> selectedUsers = userGroupId != "0" ? userService.getGroupUsers(userGroupId) : new ArrayList<User>();
        List<User> availableUsers = userService.search(new HashMap());
        if (availableRoles != null && availableRoles.size() > 0 && selectedRoles != null && selectedRoles.size() > 0) {
            availableUsers.removeAll(selectedUsers);
        }
        mv.addObject("userGroupDetail", userGroup);
        mv.addObject("selectedRoles", selectedRoles);
        mv.addObject("availableRoles", availableRoles);
        mv.addObject("selectedUsers", selectedUsers);
        mv.addObject("availableUsers", availableUsers);
        return mv;
    }

    @RequestMapping("/isDuplicated")
    @ResponseBody
    public Boolean isDuplicated(HttpServletRequest request) {
        Map param = getParamMapWithPage(request);
        String groupId = (String) param.get("groupId");
        String groupName = (String) param.get("groupName");
        UserGroup userGroup = new UserGroup();
        userGroup.setGroupId(groupId);
        userGroup.setGroupName(groupName);
        return userGroupService.isDuplicated(userGroup);

    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxView save(@ModelAttribute("userGroup") UserGroup userGroup, HttpSession session) {
        AjaxView rtn = new AjaxView();
        try {
            boolean duplicate = userGroupService.isDuplicated(userGroup);
            if (duplicate) {
                rtn.setFailed().setMessage("*用户组名已被占用！");
            } else {
                userGroup.setAddUser(WebUtil.getLoginUserName());
                userGroup.setAddDate(new Date());
                userGroup.setEditUser(WebUtil.getLoginUserName());
                userGroup.setEditDate(new Date());
                userGroupService.save(userGroup);
                rtn.setSuccess();
            }

        } catch (Exception e) {
            rtn.setFailed();
            rtn.setMessage(e.getMessage());
            logger.error("Error occur when save userGroup.", e);
        }
        return rtn;
    }

    @RequestMapping("/deleteGroups")
    @ResponseBody
    @RequiresPermissions("usergroup:delete")
    public AjaxView deleteGroups(@RequestParam("groupIds") String groupIds) {
        AjaxView rtn = new AjaxView();
        try {
            userGroupService.deleteByIds(groupIds.split(","));
            rtn.setSuccess();
        } catch (Exception e) {
            rtn.setFailed();
            rtn.setMessage(e.getMessage());
            logger.error("Error occur when delete userGroup.", e);
        }
        return rtn;
    }
}
