package com.ocellus.platform.controller;

import com.ocellus.platform.model.AbstractModel;
import com.ocellus.platform.model.PageRequest;
import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.model.User;
import com.ocellus.platform.service.AbstractService;
import com.ocellus.platform.utils.Constants;
import com.ocellus.platform.utils.NumberUtil;
import com.ocellus.platform.utils.PageConstants;
import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.web.view.AjaxView;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class BaseController <T extends AbstractModel, PK extends Serializable>{
    protected static Logger logger = Logger.getLogger(BaseController.class);
    /** common action start**/
    private AbstractService<T, PK> baseService;
    public void setBaseService(AbstractService<T, PK> baseService) {
        this.baseService = baseService;
    }

    private String modelName;
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getModelName() {
        return this.modelName;
    }

    public String getModelPageRoot() {
        return Constants.BUSSINESS_PACKAGE_NAME+Constants.SLASH_STR+ getModelName() +Constants.SLASH_STR;
    }

    @RequestMapping("/index")
    public ModelAndView showIndex(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(getModelPageRoot()+modelName+ PageConstants.SUFFIX_MAIN_PAGE);
        mv.addObject("modelName",getModelName());
        Map<String, String> requestMap = getParamMap(request);
        mv.addAllObjects(requestMap);
        indexModelObject(mv);
        return mv;
    }
    public void indexModelObject(ModelAndView mv){
    }

    @ResponseBody
    @RequestMapping("/getBaseList")
    public PageResponse getBaseList(HttpServletRequest request) {
        Map<String, String> param = getParamMapWithPage(request);
        return getPageResponse(baseService.search(param));
    }

    @RequestMapping("/editBase")
    public ModelAndView editBase(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView(getModelPageRoot()+getModelName()+ PageConstants.SUFFIX_EDIT_PAGE);
        mv.addObject("modelName",getModelName());
        Map<String, String> param = getParamMap(request);
        String id = param.get("id");
        T model = baseService.getBaseBean();
        if(!StringUtil.isEmpty(id)){
            model = baseService.getById((PK) id);
        }
        mv.addAllObjects(param);
        editModelObject(mv,model,param);
        mv.addObject("model",model);
        return mv;
    }
    public void editModelObject(ModelAndView mv,T model,Map<String, String> requestParam){
    }

    @ResponseBody
    @RequestMapping("/saveBase")
    public AjaxView saveBase(@RequestBody T vo){
        AjaxView rtn = new AjaxView();
        try {
            boolean canSave = beforeSave(vo,rtn);
            if(canSave){
                baseService.save(vo);
                afterSave(vo,rtn);
                rtn.setSuccess().setMessage(PageConstants.SAVE_SUCCESSED);
            }
        }catch (Exception e) {
            e.printStackTrace();
            rtn.setFailed().setMessage(PageConstants.SAVE_EXCEPTION+"\n" + e.getMessage());
        }
        return rtn;
    }
    public boolean beforeSave(T vo,AjaxView rtn){
        return true;
    }
    public void afterSave(T vo,AjaxView rtn){
        rtn.setData(vo);
    }

    @RequestMapping("/deleteBase")
    @ResponseBody
    public AjaxView deleteBase(HttpServletRequest request) {
        AjaxView ajaxView = new AjaxView();
        try {
            Map<String, String> map = getParamMap(request);
            String ids = map.get("ids");
            List<String> idList = StringUtil.toList(ids, Constants.COMA_STR);
            if (idList!=null && !idList.isEmpty()) {
                boolean canDelete = beforeDelete(idList, ajaxView);
                if(canDelete){
                    baseService.deleteByIds((List<PK>) idList);
                    afterDelete(idList);
                    ajaxView.setSuccess().setMessage(PageConstants.DELETE_SUCCESSED);
                }
            }else{
                ajaxView.setMessage(PageConstants.DELETE_FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ajaxView.setMessage(PageConstants.DELETE_EXCEPTION+"\n" + e.getMessage());
        }
        return ajaxView;
    }
    public boolean beforeDelete(List<String> idList,AjaxView rtn){
        return true;
    }
    public void afterDelete(List<String> idList){
    }
    /** common action end**/

    protected Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        try {
            Enumeration<?> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = URLDecoder.decode(paramNames.nextElement().toString(), "UTF-8");
                String paramValue = URLDecoder.decode(request.getParameter(paramName), "UTF-8");
                paramMap.put(paramName, paramValue);
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Error occur when process parameters", e);
        }
        return paramMap;
    }

    protected Map<String, String> getParamMapWithPage(HttpServletRequest request) {
        Map<String, String> paramMap = getParamMap(request);
        String page = paramMap.get("page");
        String rows = paramMap.get("rows");
        PageRequest req = PageRequest.get();
        if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(rows)) {
            req.setPaging(true);
            req.setPage(NumberUtil.toInteger(paramMap.get("page")));
            req.setRows(NumberUtil.toInteger(paramMap.get("rows")));
        }
        req.setSord(paramMap.get("sord"));
        req.setSidx(paramMap.get("sidx"));
        return paramMap;
    }

    protected PageResponse getPageResponse(Object rows) {
        PageResponse rep = PageResponse.get();
        rep.setRows(rows);
        PageRequest.remove();
        PageResponse.remove();
        return rep;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected Map getParamMapBaseUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
        Map userParams = user.getUserPeculiarParams();
        Map params = getParamMap(request);
        if (userParams != null && !userParams.isEmpty()) {
            params.putAll(userParams);
        }
        return params;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(
                dateFormat, false));
    }
}
