package org.mybatis.extend.dialect;

import com.ocellus.platform.model.Restrict;

import java.util.HashMap;
import java.util.Map;

public abstract class Dialect {
    public static Map optMap = new HashMap();

    static {
        optMap.put("eq", "=");
        optMap.put("gt", ">");
        optMap.put("lt", "<");
        optMap.put("ge", ">=");
        optMap.put("le", "<=");
        optMap.put("ne", "!=");
        optMap.put("like", "LIKE");
    }

    public abstract String buildLimitString(String sql, int offset, int limit);

    public abstract String buildOrderByString(String sql, String sidx, String sord);

    public String buildCountString(String sql) {
        return "select count(*) from (" + sql + ") temp";
    }

    public abstract String buildRestrictExpression(Restrict r, String tableAlia);
}
