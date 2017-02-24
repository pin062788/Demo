package com.ocellus.platform.utils;

import java.util.HashMap;

public class ParamUtil {
    public static Param setParam(String key, Object value) {
        return new ParamUtil().getParamInstance(key, value);
    }

    private Param getParamInstance(String key, Object value) {
        return new Param(key, value);
    }

    public class Param extends HashMap<String, Object> {

        public Param(String key, Object value) {
            super();
            put(key, value);
        }


        public Param setParam(String key, Object value) {
            put(key, value);
            return this;
        }
    }
}
