package org.mybatis.extend.dialect;

import com.ocellus.platform.model.Restrict;
import com.ocellus.platform.utils.StringUtil;

public class MySQLDialect extends Dialect {

    @Override
    public String buildLimitString(String sql, int offset, int limit) {
        if (offset < 0) {
            offset = 0;
        }
        final StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);
        pagingSelect.append("select * from ( ");
        pagingSelect.append(sql);
        pagingSelect.append(" ) tb limit " + offset + "," + limit);
        return pagingSelect.toString();
    }

    @Override
    public String buildOrderByString(String sql, String sidx, String sord) {
        if (sql.indexOf("order by") > 0) {
            sql = sql.replaceAll("order by", " order by " + sidx + " " + sord + ", ");
        } else if (sql.indexOf("ORDER BY") > 0) {
            sql = sql.replaceAll("ORDER BY", " order by " + sidx + " " + sord + ", ");
        } else {
            sql += " order by " + sidx + " " + sord;
        }
        return sql;
    }

    @Override
    public String buildRestrictExpression(Restrict r, String tableAlia) {
        StringBuffer expression = new StringBuffer();
        expression.append(StringUtil.isEmpty(tableAlia) ? r.getTableName() : tableAlia);
        expression.append(".");
        String columnType = r.getColumnType();
        String restrictValue = r.getRestrictValue();
        expression.append(r.getColumnName()).append(" ").append(optMap.get(r.getOptCode())).append(" ");
        if ("text".equals(columnType)) {
            expression.append("'").append(restrictValue).append("'");
        } else if ("date".equals(columnType)) {
            expression.append("str_to_date('").append(restrictValue).append("','%Y-%m-%d')");
        } else if ("number".equals(columnType)) {
            expression.append(restrictValue);
        }

        return expression.toString();
    }

}
