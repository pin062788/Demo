package com.ocellus.platform.web.view;

import java.util.HashMap;

/**
 * The result for ajax request.
 * Status: 0 - Failed; 1 - Success; 999 - Timeout
 */
public class AjaxView extends HashMap<String, Object> {
    public AjaxView() {
        super();
    }

    public AjaxView setSuccess(boolean success) {
        put("status", success ? "1" : "0");
        return this;
    }

    public AjaxView setSuccess() {
        put("status", "1");
        return this;
    }

    public AjaxView setSuccessWithError() {
        put("status", "2");
        return this;
    }

    public AjaxView setFailed() {
        put("status", "0");
        return this;
    }

    public AjaxView setTimeout() {
        put("status", "999");
        return this;
    }

    public AjaxView setData(Object data) {
        put("data", data);
        return this;
    }

    public AjaxView setStatus(String statusCode) {
        put("status", statusCode);
        return this;
    }

    public AjaxView setMessage(String msg) {
        put("message", msg);
        return this;
    }

}
