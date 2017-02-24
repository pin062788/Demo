package com.ocellus.platform.controller;

import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.model.RestrictTable;
import com.ocellus.platform.service.RestrictTableService;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/restrictTable")
public class RestrictTableController extends BaseController {

    private static Logger logger = Logger.getLogger(RestrictTableController.class);
    @Autowired
    private RestrictTableService restrictTableService;

    @RequestMapping("/listTables")
    @RequiresPermissions("restrictTable:view")
    public ModelAndView listTables() {
        ModelAndView mv = new ModelAndView("/restrict/restrictTableColumn");
        return mv;
    }

    @RequestMapping("/list")
    @RequiresPermissions("restrictTable:view")
    public ModelAndView showList() {
        ModelAndView mv = new ModelAndView("/restrict/restrictTable");
        return mv;
    }

    @RequestMapping("/getList")
    @ResponseBody
    @RequiresPermissions("restrictTable:view")
    public PageResponse getTableList(HttpServletRequest request) {
        List<RestrictTable> restrictTables = restrictTableService.search(getParamMapWithPage(request));
        return getPageResponse(restrictTables);
    }


    @RequestMapping("/add")
    @RequiresPermissions("restrictTable:add")
    public ModelAndView add(@RequestParam("tableId") String tableId) {
        return showDetail(tableId);
    }

    @RequestMapping("/edit")
    @RequiresPermissions("restrictTable:edit")
    public ModelAndView edit(@RequestParam("tableId") String tableId) {
        return showDetail(tableId);
    }

    public ModelAndView showDetail(String tableId) {
        ModelAndView mv = new ModelAndView("/restrict/restrictTableDetail");
        Boolean isNew = "0".equals(tableId);
        RestrictTable restrictTable = isNew ? new RestrictTable() : restrictTableService.getById(tableId);
        mv.addObject("allUserTables", restrictTableService.selectAllUserTable());
        mv.addObject("restrictTableDetail", restrictTable);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions(value = {"restrictTable:add", "restrictTable:edit"}, logical = Logical.OR)
    public AjaxView save(@ModelAttribute("restrictTableDetail") RestrictTable restrict) {
        AjaxView rtn = new AjaxView();
        try {
            if (StringUtil.isEmpty(restrict.getTableDesc())) {
                rtn.setFailed().setMessage("*表描述不能为空！");
            } else {
                boolean duplicate = restrictTableService.isDuplicated(restrict);
                if (duplicate) {
                    rtn.setFailed().setMessage("*该表已经存在");
                } else {
                    restrictTableService.save(restrict);
                    rtn.setSuccess();
                }
            }

        } catch (Exception e) {
            rtn.setFailed();
            rtn.setMessage(e.getMessage());
            logger.error("Error occur when save table.", e);
        }

        return rtn;
    }

    @RequestMapping("/isDuplicated")
    @ResponseBody
    public Boolean isDuplicated(HttpServletRequest request) {
        Map param = getParamMapWithPage(request);
        String tableName = (String) param.get("tableName");
        String tableId = (String) param.get("tableId");
        RestrictTable restrictTable = new RestrictTable();
        restrictTable.setTableName(tableName);
        restrictTable.setTableId(tableId);
        return restrictTableService.isDuplicated(restrictTable);

    }

    @RequestMapping("/delete")
    @ResponseBody
    @RequiresPermissions("restrictTable:delete")
    public AjaxView delete(HttpServletRequest request, @RequestParam("tableIds") String tableIds) {
        AjaxView rtn = new AjaxView();
        try {
            restrictTableService.batchDelete(tableIds);
            rtn.put("status", 1);
        } catch (Exception e) {
            String message = "删除失败";
            logger.error(message, e);
            rtn.setFailed().setMessage(e.getMessage());
        }
        return rtn;
    }
}
