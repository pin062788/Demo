package com.ocellus.platform.web;

import com.ocellus.platform.utils.Constants;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionFilter implements Filter {

    private static final Logger logger = Logger.getLogger(SessionFilter.class);
    private Boolean used;
    private String loginPage;
    private String[] excludePages;

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            String url = req.getServletPath();
            if(used){
                //是否不需要检查session
                boolean excluded = false;
                for (int i = 0; i < excludePages.length; i++) {
                    if (url.contains(excludePages[i])){
                        logger.info("url.contains(excludePages[i]);url:" + url);
                        excluded = true;
                        break;
                    }
                }
                if (!excluded) {
                    if (url.contains(".jsp") || url.contains(".do")) {
                        logger.info("url:" + url);
                        if (!url.contains(loginPage)) {
                            HttpSession session = req.getSession();
                            // 从session从取出user,如果为空说明没有登录,将其转到登录页面.
                            Object obj = session.getAttribute(Constants.SESSION_USER_KEY);
                            if (obj == null) {
                                // 跳转到登陆页面
                                res.sendRedirect(loginPage);
                                logger.info("doFilter(request, response, chain) - end session:_sess_user_key==null");
                                return;
                            }
                        }
                    }
                }
            }
            // 如果不需要检查则跳出过滤器继续执行
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("doFilter()", e);
            e.printStackTrace();
        }
    }

    public void init(FilterConfig config) throws ServletException {
        logger.info("init(FilterConfig config=" + config + ") - start");
        loginPage = config.getInitParameter("loginPage");
        used = Boolean.valueOf(config.getInitParameter("used"));
        String excludePage = config.getInitParameter("excludePage");
        excludePages = excludePage.split(";");
        logger.info("init(config) - end");
    }

    public void destroy() {
    }

}
