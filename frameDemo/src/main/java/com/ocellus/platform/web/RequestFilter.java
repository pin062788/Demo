package com.ocellus.platform.web;

import com.ocellus.platform.model.PageRequest;
import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.utils.NumberUtil;
import com.ocellus.platform.utils.StringUtil;
import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;


public class RequestFilter implements Filter {
    private static Logger logger = Logger.getLogger(RequestFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        String sord = request.getParameter("sord");
        String sidx = request.getParameter("sidx");
        PageRequest req = PageRequest.get();
        if (!StringUtil.isEmpty(page) && !"0".equals(page) && !StringUtil.isEmpty(rows)) {
            req.setPaging(true);
            req.setPage(NumberUtil.toInteger(page));
            req.setRows(NumberUtil.toInteger(rows));
        } else {
            req.setPaging(false);
            req.setPage(null);
            req.setRows(null);
        }
        req.setSord(sord);
        req.setSidx(sidx);
        chain.doFilter(request, response);
        PageRequest.remove();
        PageResponse.remove();
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
