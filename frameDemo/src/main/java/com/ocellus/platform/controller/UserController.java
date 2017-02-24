package com.ocellus.platform.controller;

import com.ocellus.platform.model.*;
import com.ocellus.platform.service.ReferenceService;
import com.ocellus.platform.service.RoleService;
import com.ocellus.platform.service.UserGroupService;
import com.ocellus.platform.service.UserService;
import com.ocellus.platform.utils.ParamUtil;
import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.web.view.AjaxView;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    private static Logger logger = Logger.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ReferenceService refService;
    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping("/list")
    @RequiresPermissions("user:view")
    public ModelAndView showList() {
        ModelAndView mv = new ModelAndView("/user/user");
        List<Reference> relatedTypes = refService.getByGroupName("userRelatedType");
        mv.addObject("relatedTypes", relatedTypes);
        return mv;
    }

    @RequestMapping("/getList")
    @ResponseBody
    @RequiresPermissions("user:view")
    public PageResponse getUserList(HttpServletRequest request) {
        List<User> users = userService.getUsers(getParamMapWithPage(request));
        return getPageResponse(users);
    }

    @RequestMapping("/add")
    @RequiresPermissions("user:add")
    public ModelAndView add(@RequestParam("userId") String userId) {
        return showDetail(userId);
    }

    @RequestMapping("/edit")
    @RequiresPermissions("user:edit")
    public ModelAndView edit(@RequestParam("userId") String userId) {
        return showDetail(userId);
    }

    public ModelAndView showDetail(String userId) {
        ModelAndView mv = new ModelAndView("/user/userDetail");
        Boolean isNew = "0".equals(userId);
        User user = isNew ? new User() : userService.getById(userId);
        List<Role> selectedRoles = isNew ? new ArrayList<Role>() : roleService.getUserRoles(userId);
        List<Role> availableRoles = roleService.search(new HashMap());
        if (availableRoles != null && availableRoles.size() > 0 && selectedRoles != null && selectedRoles.size() > 0) {
            availableRoles.removeAll(selectedRoles);
        }
        List<UserGroup> selectedGroups = isNew ? new ArrayList<UserGroup>() : userGroupService.getUserGroups(userId);
        List<UserGroup> availableGroups = userGroupService.search(new HashMap());
        if (availableGroups != null && !availableGroups.isEmpty() && selectedGroups != null && !selectedGroups.isEmpty()) {
            availableGroups.removeAll(selectedGroups);
        }
        List<Reference> relatedTypes = refService.getByGroupName("userRelatedType");

        mv.addObject("relatedTypes", relatedTypes);
        mv.addObject("userDetail", user);
        mv.addObject("selectedRoles", selectedRoles);
        mv.addObject("availableRoles", availableRoles);
        mv.addObject("selectedUserGroups", selectedGroups);
        mv.addObject("availableUserGroups", availableGroups);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions(value = {"user:add", "user:edit"}, logical = Logical.OR)
    public AjaxView save(@ModelAttribute("userDetail") User vo) {
        AjaxView rtn = new AjaxView();
        try {
            if (!StringUtil.isEmpty(vo.getUserName())) {
                boolean duplicate = userService.isDuplicated(vo);
                if (duplicate) {
                    rtn.setFailed().setMessage("*用户名已被占用！");
                } else {
                    userService.save(vo);
                    rtn.setSuccess();
                }

            }
        } catch (Exception e) {
            rtn.setFailed();
            rtn.setMessage(e.getMessage());
            logger.error("Error occur when save user.", e);
        }

        return rtn;
    }

    @RequestMapping("/showUserRelated")
    public ModelAndView showUserRelated(@RequestParam("relatedType") String relatedType) {
        ModelAndView mv = new ModelAndView("user/userRelated");
        if (User.RELATED_TYPE_EMPLOYEE.equals(relatedType)) {
            //List<Organization> organizations = orgService.search(new HashMap());
            List organizations = new ArrayList();
            mv.addObject("organizations", organizations);
        }
        mv.addObject("relatedType", relatedType);
        return mv;
    }

    @RequestMapping("/getUserRelatedList")
    @ResponseBody
    public PageResponse getUserRelatedList(HttpServletRequest request) throws UnsupportedEncodingException {
        //Map<String, Object> newMap = new HashMap<String, Object>(getParamMap(request));
        String relatedType = request.getParameter("relatedType");
        List vos = new ArrayList();
        if (User.RELATED_TYPE_CUSTOMER.equals(relatedType)) {
            //vos = customerService.search(new HashMap());
        } else if (User.RELATED_TYPE_SUPPLIER.equals(relatedType)) {
            //vos = supplierService.search(new HashMap());
        } else if (User.RELATED_TYPE_EMPLOYEE.equals(relatedType)) {
            String organizationNumber = request.getParameter("organizationNumber");
            if (!StringUtil.isEmpty(organizationNumber)) {
                Map params = ParamUtil.setParam("department", organizationNumber);
                //vos = employeeService.search(params);
            } else {
                vos = new ArrayList();
            }
        }
        return getPageResponse(vos);
    }

}
