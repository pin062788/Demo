package com.ocellus.platform.controller;

import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.model.RestrictColumn;
import com.ocellus.platform.service.RestrictColumnService;
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

@Controller
@RequestMapping("/restrictColumn")
public class RestrictColumnController extends BaseController {

    private static Logger logger = Logger.getLogger(RestrictColumnController.class);
    @Autowired
    private RestrictColumnService restrictColumnService;

    @RequestMapping("/list")
    @RequiresPermissions("restrictColumn:view")
    public ModelAndView showList(@RequestParam("tableId") String tableId) {
        ModelAndView mv = new ModelAndView("/restrict/restrictColumn");
        mv.addObject("tableId", tableId);
        return mv;
    }

    @RequestMapping("/getList")
    @ResponseBody
    @RequiresPermissions("restrictColumn:view")
    public PageResponse getColumnsList(HttpServletRequest request) {
        List<RestrictColumn> restrictColumns = restrictColumnService.search(getParamMapWithPage(request));
        return getPageResponse(restrictColumns);
    }


    @RequestMapping("/add")
    @RequiresPermissions("restrictColumn:add")
    public ModelAndView add(@RequestParam("tableId") String tableId) {
        RestrictColumn column = new RestrictColumn();
        column.setTableId(tableId);
        return showDetail(column);
    }

    @RequestMapping("/edit")
    @RequiresPermissions("restrictColumn:edit")
    public ModelAndView edit(@RequestParam("columnId") String columnId) {
        RestrictColumn column = restrictColumnService.getById(columnId);
        return showDetail(column);
    }

    public ModelAndView showDetail(RestrictColumn column) {
        ModelAndView mv = new ModelAndView("/restrict/restrictColumnDetail");
        List<String> columns = restrictColumnService.selectTableColumns(column.getTableId());
        mv.addObject("columns", columns);
        mv.addObject("columnTypes", new String[]{"text", "list", "date"});
        mv.addObject("restrictColumn", column);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/save")
    @RequiresPermissions(value = {"restrictColumn:add", "restrictColumn:edit"}, logical = Logical.OR)
    public AjaxView save(@ModelAttribute("restrictColumnDetail") RestrictColumn column) {
        AjaxView rtn = new AjaxView();
        try {
            if (StringUtil.isEmpty(column.getColumnDesc())) {
                rtn.setFailed().setMessage("*字段描述不能为空！");
            } else if (StringUtil.isEmpty(column.getColumnType())) {
                rtn.setFailed().setMessage("*字段类型不能为空！");
            } else {
                boolean duplicate = restrictColumnService.isDuplicated(column);
                if (duplicate) {
                    rtn.setFailed().setMessage("*该字段已经存在");
                } else {
                    restrictColumnService.save(column);
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
    public Boolean isDuplicated(@RequestParam("tableId") String tableId, @RequestParam("columnId") String columnId, @RequestParam("columnName") String columnName) {
        RestrictColumn column = new RestrictColumn();
        column.setColumnId(columnId);
        column.setTableId(tableId);
        column.setColumnName(columnName);
        return restrictColumnService.isDuplicated(column);
    }

    @RequestMapping("/delete")
    @ResponseBody
    @RequiresPermissions("restrictColumn:delete")
    public AjaxView delete(HttpServletRequest request, @RequestParam("columnIds") String columnIds) {
        AjaxView rtn = new AjaxView();
        try {
            restrictColumnService.batchDelete(columnIds);
            rtn.put("status", 1);
        } catch (Exception e) {
            String message = "删除失败";
            logger.error(message, e);
            rtn.setFailed().setMessage(e.getMessage());
        }
        return rtn;
    }

}
