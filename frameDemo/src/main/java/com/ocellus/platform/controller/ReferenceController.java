package com.ocellus.platform.controller;


import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.model.Reference;
import com.ocellus.platform.model.ResponseStatus;
import com.ocellus.platform.model.User;
import com.ocellus.platform.service.ReferenceCacheService;
import com.ocellus.platform.service.ReferenceService;
import com.ocellus.platform.utils.Constants;
import com.ocellus.platform.utils.StringUtil;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yu.sheng on 14-6-20.
 */
@Controller
@RequestMapping("/reference")
public class ReferenceController extends BaseController {


    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private ReferenceCacheService referenceCacheService;

    @RequestMapping("/show")
    public String showMain() {
        return "reference/dataDictionary";
    }

    @RequestMapping("/showDetail")
    public ModelAndView detailPage(HttpServletRequest request) {
        Map<?, ?> param = getParamMap(request);
        ModelAndView mv = new ModelAndView("reference/dataDictionaryDetails");
        mv.addObject("id", param.get("id"));
        mv.addObject("_timer_", param.get("_timer_"));
        return mv;
    }

    @RequestMapping("/detail")
    @ResponseBody
    public PageResponse showDetail(HttpServletRequest request) {
        Map<?, ?> param = getParamMap(request);
        String id = (String) param.get("id");
        List<Reference> references = new ArrayList<Reference>();
        if (!StringUtil.isEmpty(id))
            references.add(referenceService.getById(id));
        return getPageResponse(references);
    }

    @RequestMapping("/showTree")
    public String showTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<?, ?> param = getParamMap(request);
        String id = (String) param.get("id");
        PrintWriter writer = null;
        response.setCharacterEncoding("utf-8");
        List<Reference> references = new ArrayList<Reference>();
        JSONArray jArray = new JSONArray();
        if (StringUtil.isEmpty(id)) {
            references = referenceService.getRootNode("root");
            if (references.size() == 0) {
                Reference rootReference = new Reference();
                rootReference.setIsParent("1");
                rootReference.setCode("root");
                rootReference.setCodeDesc("基础数据");
                rootReference.setParentId("0");
                referenceService.addNode(rootReference);
                references.add(rootReference);
            }
        } else references = referenceService.getAllSubNodes(id);
        for (Reference reference : references) {
            JSONObject json = new JSONObject();
            json.put("id", reference.getDBId());
            json.put("pId", reference.getParentId());
            json.put("name", reference.getCodeDesc());
            json.put("isParent", "1".equals(reference.getIsParent()));
            jArray.put(json);
        }

        try {
            writer = response.getWriter();
            String array = jArray.toString();
            writer.write(array);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequestMapping("/editNode")
    public ModelAndView editNode(@RequestParam("id") String id) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/reference/nodeDetail");

        Reference reference = referenceService.getById(id);
        String parentId = reference.getParentId();
        Reference pReference = referenceService.getById(parentId);
        mv.addObject("reference", reference);
        mv.addObject("parentDesc", pReference != null ? pReference.getCodeDesc() : "");
        Map<String, String> activate = new HashMap<String, String>();
        activate.put("1", "激活");
        activate.put("0", "不激活");
        mv.addObject("activate", activate);
        return mv;
    }

    @RequestMapping("/addNode")
    public ModelAndView addNode(@RequestParam("parentId") String parentId, @RequestParam("isParent") String isParent) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/reference/nodeDetail");

        Reference reference = new Reference();
        reference.setParentId(parentId);
        reference.setIsParent(isParent);
        reference.setEditDate(new Date());
        reference.setEditUser("yusheng");
        Reference pReference = referenceService.getById(parentId);
        mv.addObject("reference", reference);
        mv.addObject("parentDesc", pReference.getCodeDesc());
        Map<String, String> activate = new HashMap<String, String>();
        activate.put("1", "激活");
        activate.put("0", "不激活");
        mv.addObject("activate", activate);
        return mv;
    }

    @RequestMapping("/saveNode")
    @ResponseBody
    public Map<String, Integer> saveNode(@ModelAttribute("reference") Reference reference, HttpServletRequest request) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String a2 = sdf.format(new Date());
        Date date = sdf.parse(a2);
        reference.setEditDate(date);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        reference.setEditUser(user.getUserName());
        if (StringUtil.isEmpty(reference.getDBId()))
            referenceService.addNode(reference);
        else
            referenceService.updateNode(reference);
        Map<String, Integer> rtn = new HashMap<String, Integer>();
        rtn.put("status", 1);
        return rtn;
    }

    @RequestMapping("/deleteNode")
    @ResponseBody
    public ResponseStatus deleteNode(@RequestParam("id") String id) {
        ResponseStatus responseStatus = new ResponseStatus();
        if (!StringUtil.isEmpty(id))
            referenceService.deleteNode(id);
        responseStatus.setStatus(ResponseStatus.SUCCESS);
        return null;
    }

    @RequestMapping("/clearCache")
    @ResponseBody
    public int clearCache() {
        referenceCacheService.resetCache();
        return ResponseStatus.SUCCESS;
    }

    @RequestMapping("/findGroupList")
    @ResponseBody
    public List<Reference> getReferenceByGroupName(@RequestParam("groupName") String groupName) {
        return referenceService.getByGroupName(groupName);
    }
}
