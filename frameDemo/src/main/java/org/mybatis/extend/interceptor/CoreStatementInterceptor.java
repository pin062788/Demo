package org.mybatis.extend.interceptor;

import com.ocellus.platform.model.PageRequest;
import com.ocellus.platform.model.PageResponse;
import com.ocellus.platform.model.Restrict;
import com.ocellus.platform.model.User;
import com.ocellus.platform.utils.ReflectUtil;
import com.ocellus.platform.utils.StringUtil;
import com.ocellus.platform.utils.WebUtil;
import com.ocellus.platform.utils.query.QueryUtil;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.log4j.Logger;
import org.mybatis.extend.dialect.Dialect;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class CoreStatementInterceptor implements Interceptor {
    private final static Logger log = Logger
            .getLogger(CoreStatementInterceptor.class);
    private Dialect dialect;

    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        if (boundSql == null || boundSql.getSql() == null || "".equals(boundSql.getSql()))
            return null;
        String orignalSting = boundSql.getSql();
        //如果当前是INSERT、UPDATE、INSERT操作，不拦截
        if (!(orignalSting.toUpperCase().trim().startsWith("SELECT") || orignalSting.toUpperCase().trim().startsWith("WITH"))) {
            return invocation.proceed();
        }
        User user = WebUtil.getLoginUser();
        PageRequest req = PageRequest.get();
        if (user == null && !req.isPaging() && StringUtil.isEmpty(req.getSidx())) {
            return invocation.proceed();
        } else {
            String buildSql = orignalSting;
            //权限数据拦截
            if (user != null) {
                List<Restrict> userRestricts = user.getRestricts();
                if (userRestricts != null && !userRestricts.isEmpty()) {
                    buildSql = QueryUtil.addConstraints(orignalSting, userRestricts, dialect);
                }
            }
            MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory());
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
            // 获得排序字段
            String sidColumn = getSidColumn(mappedStatement, req.getSidx());
            //排序拦截
            if (!StringUtil.isEmpty(sidColumn)) {
                buildSql = dialect.buildOrderByString(buildSql, sidColumn, req.getSord());
            }
            //分页拦截
            if (req.isPaging()) {
                int total = getTotal(mappedStatement, boundSql, buildSql);
                buildSql = dialect.buildLimitString(buildSql, (generateResp(req, total).getPage() - 1) * req.getRows(), req.getRows());
                if (log.isDebugEnabled()) {
                    log.debug("Paging SQL: " + buildSql);
                }
            }
            metaStatementHandler.setValue("delegate.boundSql.sql", buildSql);
            metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
            metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
            //Fix issue: paging function do not need to involved in relationship query
            req.setPaging(false);
            req.setSidx(null);
            return invocation.proceed();
        }
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }

    private PageResponse generateResp(PageRequest req, int total) {
        // generate reponse
        PageResponse resp = PageResponse.get();
        resp.setRecords(total);
        resp.setPage(req.getPage());
        int numOfPages = 0;
        if (total > 0) {
            numOfPages = total % req.getRows() == 0 ? total / req.getRows() : total / req.getRows() + 1;
        }
        resp.setTotal(numOfPages);
        PageRequest.remove();
        return resp;
    }

    private String getSidColumn(MappedStatement mappedStatement, String sidx) {
        if (StringUtil.isEmpty(sidx)) {
            return "";
        }

        if (sidx.contains(",")) {
            return sidx;
        }
        List<ResultMap> maps = mappedStatement.getResultMaps();
        String sidColumn = null;

        //修改代码使wlxx.wlBm格式的值也能使用
        String prefix = "";
        String col = "";
        if (sidx.contains(".")) {
            prefix = sidx.split("\\.")[0];
            col = sidx.split("\\.")[1];
        } else {
            col = sidx;
        }

        if (!StringUtil.isEmpty(col)) {

            for (ResultMap map : maps) {
                List<ResultMapping> propMaps = map.getPropertyResultMappings();
                for (ResultMapping propMap : propMaps) {
                    String prop = propMap.getProperty();
                    if (col.equalsIgnoreCase(prop)) {
                        sidColumn = propMap.getColumn();
                        break;
                    }
                }
            }


        }
        if (StringUtil.isEmpty(sidColumn) && !StringUtil.isEmpty(col)) {
            sidColumn = col;
        }
        //为wlxx.wlBm 添加wlxx. 前缀
        if (sidx.contains(".")) {
            sidColumn = prefix + "." + sidColumn;
        }

        return sidColumn;
    }

    private int getTotal(MappedStatement mappedStatement, BoundSql boundSql, String orignalSting) throws SQLException {
        Connection connection = null;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        int total = 0;
        try {
            // get total record number
            String countSql = dialect.buildCountString(orignalSting);
            Object parameterObject = boundSql.getParameterObject();
            if (log.isDebugEnabled()) {
                log.debug("Count SQL: " + countSql);
            }
            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            countStmt = connection.prepareStatement(countSql.toString());
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql.toString(), boundSql.getParameterMappings(), parameterObject);
            Field metaParamsField = ReflectUtil.getFieldByFieldName(boundSql, "metaParameters");
            if (metaParamsField != null) {
                MetaObject mo = (MetaObject) ReflectUtil.getValueByFieldName(boundSql, "metaParameters");
                ReflectUtil.setValueByFieldName(countBS, "metaParameters", mo);
            }
            setParameters(countStmt, mappedStatement, countBS, parameterObject);
            rs = countStmt.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (rs != null) rs.close();
            if (countStmt != null) countStmt.close();
            if (connection != null) connection.close();
        }
        return total;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }
}
