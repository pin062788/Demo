package com.ocellus.platform.model;

import org.activiti.engine.query.Query;

import java.util.Map;


public class PageResponse {
    private static ThreadLocal<PageResponse> tl = new ThreadLocal<PageResponse>();

    public PageResponse() {
    }

    public PageResponse(Query query, PageRequest pRequest) {
        long count = query.count();
        if (pRequest.isPaging()) {
            init(count, pRequest.getPage(), pRequest.getRows());
            this.rows = query.listPage(this.getFirstResult(), this.getRowCount());
        } else {
            this.rows = query.list();
        }
        tl.set(this);
    }

    /**
     * @param total    总记录数
     * @param page     当前页码
     * @param rowCount 每页记录数
     */
    private void init(long total, int page, int rowCount) {
        this.records = total;
        this.page = page;
        this.firstResult = (page - 1) * rowCount;
        this.rowCount = rowCount;
        long numOfPages = 0;
        if (total > 0) {
            numOfPages = records % rowCount == 0 ? total / rowCount : total / rowCount + 1;
        }
        this.total = Integer.valueOf(numOfPages + "");
    }

    public PageResponse(long total, int page, int rowCount) {
        init(total, page, rowCount);
    }

    public static PageResponse get() {
        PageResponse res = tl.get();
        if (res == null) {
            res = new PageResponse();
            tl.set(res);
        }
        return res;
    }

    public static void remove() {
        tl.remove();
    }

    // 需要显示的数据集
    private Object rows;

    // 页面可显示行数
    private int rowCount;

    // 当前码
    private int page;

    // 数据总数
    private long records;

    // 可显示的页数
    private int total;

    private int firstResult;

    // 自定义数据
    private Map<String, Object> userdata;

    public Object getRows() {
        return rows;
    }

    /**
     * 需要显示的数据集
     *
     * @param rows
     */
    public void setRows(Object rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getRecords() {
        return records;
    }

    public void setRecords(long records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        if (total < getPage()) {
            setPage(total);
        }
    }

    public Map<String, Object> getUserdata() {
        return userdata;
    }

    public void setUserdata(Map<String, Object> userdata) {
        this.userdata = userdata;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

}
