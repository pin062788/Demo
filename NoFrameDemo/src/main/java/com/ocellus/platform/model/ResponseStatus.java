package com.ocellus.platform.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于ajax调用返回调用状态、相应消息
 *
 * @author zhou.wanggen
 */
public final class ResponseStatus {
    public static int SUCCESS = 200;
    public static int FAILED = 500;
    public HashMap data = new HashMap();
    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }

    public Map getData() {
        return data;
    }
}
