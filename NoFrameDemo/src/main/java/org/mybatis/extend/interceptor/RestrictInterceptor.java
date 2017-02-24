package org.mybatis.extend.interceptor;

import com.ocellus.platform.model.Restrict;
import com.ocellus.platform.model.User;
import com.ocellus.platform.utils.WebUtil;
import com.ocellus.platform.utils.query.QueryUtil;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.log4j.Logger;
import org.mybatis.extend.dialect.Dialect;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;


@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class RestrictInterceptor implements Interceptor {
    private final static Logger log = Logger.getLogger(RestrictInterceptor.class);
    private Dialect dialect;

    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        if (boundSql == null || boundSql.getSql() == null || "".equals(boundSql.getSql()))
            return null;
        String orignalSting = boundSql.getSql();
        //如果当前是INSERT、UPDATE、INSERT操作，拦截器不进行分页
        if (orignalSting.toUpperCase().contains("TB_RESTRICT") || !(orignalSting.toUpperCase().trim().startsWith("SELECT") || orignalSting.toUpperCase().trim().startsWith("WITH"))) {
            return invocation.proceed();
        }
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory());

        User user = WebUtil.getLoginUser();
        if (user != null) {
            List<Restrict> userRestricts = user.getRestricts();
            if (userRestricts != null && !userRestricts.isEmpty()) {
                String restrictSQLStr = QueryUtil.addConstraints(orignalSting, userRestricts, dialect);
                metaStatementHandler.setValue("delegate.boundSql.sql", restrictSQLStr);
            }
        }

        return invocation.proceed();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }
}
