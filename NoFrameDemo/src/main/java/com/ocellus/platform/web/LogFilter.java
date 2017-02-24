package com.ocellus.platform.web;


import com.ocellus.platform.model.Resource;
import com.ocellus.platform.model.User;
import com.ocellus.platform.model.UserLog;
import com.ocellus.platform.service.LogService;
import com.ocellus.platform.service.ResourceService;
import com.ocellus.platform.utils.ApplicationContextHolder;
import com.ocellus.platform.utils.Constants;
import com.ocellus.platform.utils.ParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class LogFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(LogFilter.class);
    private String loginPage = "/login.jsp";
    private String loginAction = "/loginAuth.do";
    private String exclude = "";
    public static List<Resource> MENUSLIST;

    public void init(FilterConfig filterConfig) throws ServletException {
        exclude = filterConfig.getInitParameter("exclude");
        logger.info("logger" + exclude);
        MENUSLIST = ApplicationContextHolder.getBean(ResourceService.class).search(ParamUtil.setParam("resourceType", Constants.RESOURCE_TYPE_MENU));
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ctxPath = httpRequest.getContextPath();
        String uri = httpRequest.getRequestURI();
        String fullUrl = httpRequest.getRequestURL().toString();

        String databaseUri = "";
        if (uri.indexOf("/", 1) == -1) {
            databaseUri = uri;
        } else {
            databaseUri = uri.substring(uri.indexOf("/", 1), uri.length());
        }
        databaseUri = databaseUri.replaceAll("//", "/");

        int isService = uri.indexOf("services");
        if (uri.endsWith(ctxPath)
                || uri.endsWith(ctxPath + "/")
                || uri.endsWith("/home/getMenuNav.do")
                || uri.endsWith("/home/getUserLogin.do")
                || uri.endsWith("/chat/askServ")
                || uri.endsWith("/chat/getUserLists.do")
                || isService != -1
                ) {
            chain.doFilter(request, response);
        } else {
            HttpSession session = ((HttpServletRequest) request).getSession();
            User user = (User) session.getAttribute(Constants.SESSION_USER_KEY);
            if (user != null) {
                UserLog userLog = null;
                for (int i = 0; i < MENUSLIST.size(); i++) {
                    if (databaseUri.equals(MENUSLIST.get(i).getResourceUrl()) && MENUSLIST.get(i).isActiveModule()) {
                        userLog = new UserLog();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = formatter.format(new Date());
                        userLog.setTime(time);
                        userLog.setUserId(user.getUserId());
                        userLog.setMessage(MENUSLIST.get(i).getResourceName());
                        userLog.setType("用户访问");
                        logger.debug("匹配后：" + MENUSLIST.get(i).getResourceName());
                        ApplicationContextHolder.getBean(LogService.class).insertUserLog(userLog);
                        break;
                    }
                    if (i == MENUSLIST.size() - 1) {
                        userLog = new UserLog();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = formatter.format(new Date());
                        userLog.setTime(time);
                        userLog.setUserId(user.getUserId());
                        userLog.setMessage("访问:" + fullUrl);
                        userLog.setType("用户访问");
                        logger.debug("无匹配数据");
                        ApplicationContextHolder.getBean(LogService.class).insertUserLog(userLog);
                    }
                }
            }
            chain.doFilter(request, response);
        }
    }

    public void destroy() {
    }

}
