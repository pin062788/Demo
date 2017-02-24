package com.ocellus.platform.controller;

import com.ocellus.platform.model.Organization;
import com.ocellus.platform.model.TreeNode;
import com.ocellus.platform.service.OrganizationService;
import com.ocellus.platform.service.ReferenceCacheService;
import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.web.view.AjaxView;
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
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService orgService;
    @Autowired
    private ReferenceCacheService reference;

    @RequestMapping("/showOrgMainPage")
    public String showOrgMainPage() {
        return "organization/orgMain";
    }

    @RequestMapping("/getOrgTree")
    @ResponseBody
    public List<TreeNode> getOrgTree(HttpServletRequest request) {
        String orgId = StringUtil.isEmpty(request.getParameter("id")) ? "0" : request.getParameter("id");
        /*if(StringUtil.isEmpty(request.getParameter("id")))
		{
			List<TreeNode> orgs = new ArrayList<TreeNode>();
			Organization root = new Organization();
			root.setId("0");
			root.setText("组织机构");
			orgs.add(root);
			
			return orgs;
		}*/

        return orgService.getChildTreeNodes(orgId);

    }

    @RequestMapping("/add")
    public ModelAndView add(@RequestParam("parentOrgId") String parentOrgId, @RequestParam("parentOrgName") String parentOrgName) {
        ModelAndView mv = new ModelAndView("/organization/organizationDetail");
        mv.addObject("orgTypeList", reference.getSubNodesList("org_type"));
        mv.addObject("orgFunctionList", reference.getSubNodesList("org_function_type"));
        Organization parent = new Organization();
        parent.setOrgId(parentOrgId);
        parent.setOrgName(parentOrgName);
        Organization org = new Organization();
        org.setParentOrg(parent);
        mv.addObject("organizationDetail", org);
        return mv;
    }

    @RequestMapping("/edit")
    public ModelAndView edit(@RequestParam("orgId") String orgId) {
        ModelAndView mv = new ModelAndView("organization/organizationDetail");
        mv.addObject("orgTypeList", reference.getSubNodesList("org_type"));
        mv.addObject("orgFunctionList", reference.getSubNodesList("org_function_type"));
        Organization org = orgService.getById(orgId);
        mv.addObject("organizationDetail", org);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/save")
    public AjaxView save(@ModelAttribute("organizationDetail") Organization org) {
        AjaxView rtn = new AjaxView();
        try {
            Organization localOrg = orgService.getByCode(org.getOrgCode());
            if (localOrg != null && !localOrg.equals(org)) {
                rtn.setMessage("组织编码已存在,请重新输入...");
                return rtn;
            }
            Organization po = orgService.save(org);
            rtn.setData(po).setSuccess();
        } catch (Exception e) {
            rtn.setFailed().setMessage(e.getMessage());
        }

        return rtn;
    }


    @RequestMapping("/delete")
    @ResponseBody
    public AjaxView delete(@RequestParam("orgId") String orgId) {
        AjaxView rtn = new AjaxView();
        try {
            orgService.deleteById(orgId, true);
            rtn.setSuccess();
        } catch (Exception e) {
            rtn.setFailed().setMessage(e.getMessage());
        }

        return rtn;
    }

}
