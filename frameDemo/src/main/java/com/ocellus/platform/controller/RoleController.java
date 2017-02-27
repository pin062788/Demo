package com.ocellus.platform.controller;

import com.ocellus.platform.model.*;
import com.ocellus.platform.service.RestrictColumnService;
import com.ocellus.platform.service.RestrictService;
import com.ocellus.platform.service.RestrictTableService;
import com.ocellus.platform.service.RoleService;
import com.ocellus.platform.utils.ParamUtil;
import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.web.view.AjaxView;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    private static Logger logger = Logger.getLogger(RoleController.class);
    @Autowired
    private RoleService roleService;
    @Autowired
    private RestrictTableService tableService;
    @Autowired
    private RestrictColumnService columnService;
    @Autowired
    private RestrictService restrictService;

    @RequestMapping("/list")
    public ModelAndView showList() {
        ModelAndView mv = new ModelAndView("/role/role");
        return mv;
    }

    @RequestMapping("/getList")
    @ResponseBody
    @RequiresPermissions("role:view")
    public PageResponse getRoleList(HttpServletRequest request) {
        List<Role> users = roleService.search(getParamMapWithPage(request));
        return getPageResponse(users);
    }

    @RequestMapping("/add")
    @RequiresPermissions("role:add")
    public ModelAndView add() {
        ModelAndView mv = new ModelAndView("/role/roleDetail");
        Role role = new Role();
        mv.addObject("roleDetail", role);
        return mv;
    }

    @RequestMapping("/edit")
    @RequiresPermissions("role:edit")
    public ModelAndView edit(@RequestParam("roleId") String roleId) {
        ModelAndView mv = new ModelAndView("/role/roleDetail");
        Role role = null;
        if (!StringUtil.isEmpty(roleId)) {
            role = roleService.getById(roleId);
        }
        mv.addObject("roleDetail", role);
        return mv;
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxView save(@ModelAttribute("role") Role vo) {
        AjaxView rtn = new AjaxView();
        try {
            boolean duplicate = false;
            if (!StringUtil.isEmpty(vo.getRoleCode())) {
                Role role = roleService.getByCode(vo.getRoleCode());
                if (role != null && !role.getRoleId().equals(vo.getRoleId())) {
                    duplicate = true;
                    rtn.setFailed().setMessage("角色编码" + vo.getRoleCode() + "已经存在!");
                }
            }
            if (!duplicate) {
                Role po = roleService.save(vo);
                rtn.setSuccess().setData(po);
            }
        } catch (Exception e) {
            logger.error("角色保存失败", e);
            rtn.setFailed().setMessage(e.getMessage());
        }
        return rtn;
    }

    @RequestMapping("/delete/{roleIds}")
    @ResponseBody
    @RequiresPermissions("role:delete")
    public AjaxView delete(HttpServletRequest request, @PathVariable("roleIds") String roleIds) {
        AjaxView rtn = new AjaxView();
        try {
            boolean hasAdminRole = roleService.hasAdminRole(roleIds);
            if(hasAdminRole){
                rtn.setFailed().setMessage("选择数据中包含管理员权限，最高权限不可删除!");
            }else{
                roleService.batchDelete(roleIds);
                rtn.setSuccess();
            }
        } catch (Exception e) {
            logger.error("删除失败", e);
            rtn.setFailed().setMessage(e.getMessage());
        }
        return rtn;
    }

    @RequestMapping("/getPermission")
    @ResponseBody
    @RequiresPermissions("role:addOperateFilter")
    public List<Resource> getPermission(String roleId) {
        return roleService.searchRoleResource(roleId);
    }

    @RequestMapping("/savePermission")
    @ResponseBody
    @RequiresPermissions("role:addOperateFilter")
    public AjaxView savePermission(String roleId, String resources) {
        AjaxView rtn = new AjaxView();
        try {
            roleService.savePermission(roleId, resources);
            rtn.setSuccess();
        } catch (Exception e) {
            String message = "保存失败";
            logger.error("角色保存失败", e);
            rtn.setFailed().setMessage(e.getMessage());
        }
        return rtn;
    }

    /**
     * 数据限制
     **/
    @RequestMapping("/showDataConfig")
    public ModelAndView showDataConfig(@RequestParam("roleId") String roleId) {
        ModelAndView mv = new ModelAndView("/role/dataConfig");
        mv.addObject("tables", tableService.search(new HashMap()));
        mv.addObject("roleId", roleId);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/getTableColumns")
    public AjaxView getTableColumns(@RequestParam("tableId") String tableId, @RequestParam("roleId") String roleId) {
        AjaxView rtn = new AjaxView();
        try {
            List<RestrictColumn> columns = columnService.search(ParamUtil.setParam("tableId", tableId));
            rtn.put("columns", columns);

            RestrictTable table = tableService.getById(tableId);
            List<Restrict> restricts = restrictService.search(ParamUtil.setParam("tableName", table.getTableName()).setParam("roleId", roleId));
            rtn.put("restricts", restricts);

            rtn.setSuccess();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            rtn.setFailed().setMessage("加载数据失败！");
        }
        return rtn;
    }

    @ResponseBody
    @RequestMapping("/saveDataConfig")
    public AjaxView saveDataConfig(HttpServletRequest request) {
        AjaxView rtn = new AjaxView();
        try {
            roleService.saveDataConfig(getParamMap(request));
            rtn.setSuccess();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            rtn.setFailed().setMessage("保存数据失败！");
        }
        return rtn;
    }
}
