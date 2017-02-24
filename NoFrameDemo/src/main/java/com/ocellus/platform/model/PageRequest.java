package com.ocellus.platform.model;


import com.ocellus.platform.utils.NumberUtil;
import com.ocellus.platform.utils.StringUtil;

import javax.servlet.ServletRequest;


public class PageRequest implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static ThreadLocal<PageRequest> tl = new ThreadLocal<PageRequest>();

    public PageRequest() {
    }

    public PageRequest(ServletRequest request) {
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        if (!StringUtil.isEmpty(page) && !StringUtil.isEmpty(rows)) {
            this.paging = true;
            this.page = NumberUtil.toInteger(page);
            this.rows = NumberUtil.toInteger(rows);
        }
        this.sidx = request.getParameter("sidx") + "";
        this.sord = request.getParameter("sord") + "";
        tl.set(this);
    }

    public static PageRequest get() {
        PageRequest req = tl.get();
        if (req == null) {
            req = new PageRequest();
            tl.set(req);
        }
        return req;
    }

    public static void remove() {
        tl.remove();
    }

    // 是否分页
    private boolean paging;
    // 当前页码
    private Integer page;
    // 页面可显示行数
    private Integer rows;
    // 用于排序的列名
    private String sidx;
    // 排序的方式desc/asc
    private String sord;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

}
