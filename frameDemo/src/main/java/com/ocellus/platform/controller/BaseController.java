package com.ocellus.platform.controller;

import com.ocellus.platform.dao.UserDAO;
import com.ocellus.platform.model.PageRequest;
import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.model.User;
import com.ocellus.platform.utils.Constants;
import com.ocellus.platform.utils.NumberUtil;
import com.ocellus.platform.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    protected static Logger logger = Logger.getLogger(BaseController.class);

    @Autowired
    private UserDAO userDao;

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
