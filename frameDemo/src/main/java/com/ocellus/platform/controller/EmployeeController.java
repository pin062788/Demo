package com.ocellus.platform.controller;

import com.ocellus.platform.model.Employee;
import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.model.Reference;
import com.ocellus.platform.service.EmployeeService;
import com.ocellus.platform.service.ReferenceCacheService;
import com.ocellus.platform.utils.DateUtil;
import com.ocellus.platform.web.view.AjaxView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人员信息
 *
 */

@Controller
@RequestMapping("employee")
public class EmployeeController extends BaseController {


    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ReferenceCacheService<?> reference;

    /**
     * 人员信息:展示页面
     *
     * @return
     */
    @RequestMapping("employeeIndexPage")
    public ModelAndView EmployeeIndexPage() {
        ModelAndView mv = new ModelAndView("employee/employeeMain");
        List<Reference> xb = reference.getSubNodesList("YH_XB");//收支类型
        mv.addObject("xbList", xb);
        return mv;
    }

    /**
     * 人员信息:数据读取
     *
     * @param request
     * @return
     */
    @RequestMapping("getEmployeeList")
    @ResponseBody
    public PageResponse getEmployeeList(HttpServletRequest request) {
        Map<String, String> map = getParamMapWithPage(request);
        List<Employee> list = employeeService.search(map);
        return getPageResponse(list);
    }

    /**
     * 人员信息:新增
     *
     * @return
     */
    @RequestMapping("addEmployee")
    public ModelAndView addEmployee() {
        ModelAndView mv = new ModelAndView("employee/employeeDetail");
        Employee employee = new Employee();
        employee.setEmployeeBh("Emp_" + DateUtil.date2Str(new Date(), "yyyyMMddhhmmssSSS"));
        List<Reference> xb = reference.getSubNodesList("YH_XB");
        mv.addObject("employee", employee);
        mv.addObject("xbList", xb);
        return mv;
    }

    /**
     * 人员信息:新增
     *
     * @return
     */
    @RequestMapping("editEmployee")
    public ModelAndView editEmployee(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("employee/employeeDetail");
        Map<String, String> map = getParamMap(request);
        Employee employee = employeeService.search(map).iterator().next();
        List<Reference> xb = reference.getSubNodesList("YH_XB");
        mv.addObject("employee", employee);
        mv.addObject("xbList", xb);
        return mv;
    }

    /**
     * 人员信息:新增保存
     *
     * @param request
     * @return
     */
    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public AjaxView saveOrUpdate(HttpServletRequest request, @ModelAttribute Employee employee) {
        AjaxView rtn = new AjaxView();
        try {

            employeeService.save(employee);
            rtn.setSuccess();
        } catch (Exception e) {
            logger.error("保存失败", e);
            rtn.setFailed().setMessage(e.getMessage());
        }
        return rtn;
    }

    /**
     * 删除
     *
     * @param request
     * @return
     */
    @RequestMapping("deleteEmployee")
    @ResponseBody
    public AjaxView deleteEmployee(HttpServletRequest request) {
        AjaxView rtn = new AjaxView();
        Map<String, String> map = getParamMap(request);
        try {
            employeeService.deleteByIds(map);
            rtn.setMessage("删除成功！");
            rtn.setSuccess();
        } catch (Exception e) {
            logger.error("删除失败", e);
            rtn.setFailed().setMessage(e.getMessage());
        }
        return rtn;
    }

}
