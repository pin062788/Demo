package com.ocellus.platform.controller;

import com.ocellus.platform.model.Reference;
import com.ocellus.platform.model.Resource;
import com.ocellus.platform.service.ReferenceService;
import com.ocellus.platform.service.ResourceService;
import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.web.view.AjaxView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ReferenceService refService;

    @RequestMapping("/list")
    public String showResource() {
        return "resource/resource";
    }

    @ResponseBody
    @RequestMapping("/getMenu")
    public List<Resource> getMenu(HttpServletRequest request) {
        /*String resourceCode = "-1";
		{
			String code  = request.getParameter("resourceCode");
			if(!StringUtil.isEmpty(code))
			{
				resourceCode = code;
			}
		}
		return resourceService.getChildren(resourceCode);*/

        List<Resource> list = null;
        String code = request.getParameter("resourceCode");
        if (StringUtil.isEmpty(code)) {
            list = new ArrayList<Resource>();
            Resource res = resourceService.getByCode("0");
            resourceService.setIsParent(res);
            //res.setChildren(resourceService.searchByParentCode("0"));
            list.add(res);
        } else {
            list = resourceService.searchByParentCode(code);
            if (list != null && list.size() > 0) {
                for (Resource r : list) {
                    resourceService.setIsParent(r);
                }
            }
        }
        return list;
    }

    @RequestMapping("/add")
    public ModelAndView add(@RequestParam("parentCode") String parentCode) {
        ModelAndView mv = new ModelAndView("/resource/resourceDetail");
        List<Reference> resourceTypes = refService.getByGroupName("resourceType");
        Resource res = new Resource();
        res.setParentResourceCode(parentCode);
        if (!StringUtil.isEmpty(parentCode)) {
            Resource parent = resourceService.getByCode(parentCode);
            if (parent != null) {
                res.setParentResourceName(parent.getResourceName());
                res.setParentModuleCode(parent.getParentModuleCode());
            }
        }
        mv.addObject("resourceDetail", res);
        mv.addObject("resourceTypes", resourceTypes);
        return mv;
    }

    @RequestMapping("/edit")
    public ModelAndView edit(@RequestParam("resourceId") String resourceId) {
        ModelAndView mv = new ModelAndView("resource/resourceDetail");
        List<Reference> resourceTypes = refService.getByGroupName("resourceType");
        Resource res = resourceService.getById(resourceId);
        if (res != null && !StringUtil.isEmpty(res.getParentResourceCode())) {
            Resource parent = resourceService.getByCode(res.getParentResourceCode());
            if (parent != null) {
                res.setParentResourceName(parent.getResourceName());
            }
        }
        mv.addObject("resourceDetail", res);
        mv.addObject("resourceTypes", resourceTypes);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/save")
    public AjaxView save(@ModelAttribute("resourceDetail") Resource vo) {
        AjaxView rtn = new AjaxView();
        try {
            if (StringUtil.isEmpty(vo.getResourceId())) {
                Resource resource = resourceService.getByCode(vo.getResourceCode());
                if (resource != null) {
                    rtn.setMessage("资源编码已存在,请重新输入...");
                    return rtn;
                }
            }
            Resource po = resourceService.save(vo);
            rtn.setData(po).setSuccess();
        } catch (Exception e) {
            rtn.setFailed().setMessage(e.getMessage());
        }

        return rtn;
    }

    @RequestMapping("/openResourceTree/{roleId}")
    public ModelAndView openResourceTree(@PathVariable("roleId") String roleId, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/resource/resourceTree");
        mv.addObject("roleId", roleId);
        return mv;
    }

    @RequestMapping("/getResourceTree")
    @ResponseBody
    public List<Resource> getResourceTree(HttpServletRequest request) {
        return resourceService.getChildren("0");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public AjaxView delete(@RequestParam("resourceCode") String resourceCode) {
        AjaxView rtn = new AjaxView();
        try {
            resourceService.delete(resourceCode);
            rtn.setSuccess();
        } catch (Exception e) {
            rtn.setFailed().setMessage(e.getMessage());
        }

        return rtn;
    }

}
