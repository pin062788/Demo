package com.ocellus.platform.utils.query;

import com.ocellus.platform.model.Restrict;
import com.ocellus.platform.utils.StringUtil;
import org.apache.log4j.Logger;
import org.mybatis.extend.dialect.Dialect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class QueryUtil {
    private QueryUtil() { /* Singleton */ }

    public static String prefixTableName(final String sql, final String column) {
        final QueryScanner scanner = new QueryScanner(sql);
        scanner.scan();
        final QueryBuilder builder = new QueryBuilder(sql, scanner.getComponentList());
        builder.build();
        final List queryList = builder.getQueryList();
        final Query query = (Query) queryList.get(0);
        return prefixTableName(query, column);
    }

    public static String prefixTableName(final Query query, final String column) {
        final QueryAnalyzer qa = new QueryAnalyzer();
        return qa.formatFullyQualifiedName(query, column);
    }

    public static String addConstraints(final String sql, final List<Restrict> userRestricts, Dialect dialect) {

        final boolean debug = logger.isDebugEnabled();

        StringBuffer result = new StringBuffer();
        final QueryScanner scanner = new QueryScanner(sql);
        scanner.scan();

        final QueryAnalyzer qa = new QueryAnalyzer();
        final QueryModifier qm = new QueryModifier();

        final QueryBuilder builder = new QueryBuilder(sql, scanner.getComponentList());
        builder.build();

        Query lastQuery = null;

        final Iterator queryIt = builder.getQueryList().iterator();
        while (queryIt.hasNext()) {
            final Query query = (Query) queryIt.next();

            if (lastQuery != null) {
                result.append(sql.substring(lastQuery.getEndPos() + 1, query.getStartPos()));
            }

            final QueryTables qt = qa.fetchTables(query);

            final StringBuffer sb = new StringBuffer();
            final StringBuffer expression = new StringBuffer();
            if (userRestricts != null) {
                Restrict lastRestrict = null;
                for (Restrict restrict : userRestricts) {
                    final String tableName = restrict.getTableName().toLowerCase();
                    List tableComponents = qt.get(tableName);
                    if (tableComponents != null) {
                        if (lastRestrict == null) {
                            expression.append(" (");
                        } else if (!lastRestrict.getRoleId().equals(restrict.getRoleId())
                                || !lastRestrict.getTableName().equals(restrict.getTableName())) {
                            expression.append(" ) AND (");
                        } else {
                            expression.append(" " + (StringUtil.isEmpty(restrict.getConnOpt()) ? "AND" : restrict.getConnOpt()) + " ");
                        }
                        expression.append(dialect.buildRestrictExpression(restrict, ((IQueryComponent) tableComponents.get(0)).getAlias()));
                        lastRestrict = restrict;
                    }
                }
                if (expression.length() > 0) {
                    expression.append(") ");
                    sb.append(" (" + expression + ") ");
                }
                log(debug, "Adding user restriction: " + expression);
            }

            if (sb.length() > 0) {
                final String newSql = qm.insertConstraints(query, sb.toString());
                result.append(newSql);
            } else {
                result.append(query.getValue());
            }

            lastQuery = query;
        }
        log(debug, "Query:\n" + result);
        return result.toString();
    }

    public static String addJoins(final String sql, final String join) {
        if (StringUtil.isEmpty(join)) return sql;

        final boolean debug = "true".equals(System.getProperty("DBUtil.debug"));

        StringBuffer result = new StringBuffer();
        final QueryScanner scanner = new QueryScanner(sql);
        scanner.scan();

        final QueryModifier qm = new QueryModifier();
        final QueryBuilder builder = new QueryBuilder(sql, scanner.getComponentList());
        builder.build();

        Query lastQuery = null;

        final Iterator queryIt = builder.getQueryList().iterator();
        while (queryIt.hasNext()) {
            final Query query = (Query) queryIt.next();

            if (lastQuery != null)
                result.append(sql.substring(lastQuery.getEndPos() + 1, query.getStartPos()));

            final String newSql = qm.insertJoin(query, join);
            result.append(newSql).append(" ");

            lastQuery = query;
        }
        log(debug, "Query:\n" + result);
        return result.toString();
    }

    public static Collection getTables(final String sql) {
        ArrayList tables = new ArrayList();

        final QueryScanner scanner = new QueryScanner(sql);
        scanner.scan();

        final QueryAnalyzer qa = new QueryAnalyzer();

        final QueryBuilder builder = new QueryBuilder(sql, scanner.getComponentList());
        builder.build();

        final Iterator queryIt = builder.getQueryList().iterator();
        if (queryIt.hasNext()) {
            final Query query = (Query) queryIt.next();
            final QueryTables qt = qa.fetchTables(query);
            if (qt != null) {
                for (Iterator tableIt = qt.iterator(); tableIt.hasNext(); ) {
                    QueryComponent qcomponent = (QueryComponent) tableIt.next();
                    if (qcomponent != null) {
                        String table;
                        if (qcomponent instanceof QueryColumnComponent)
                            table = ((QueryColumnComponent) qcomponent).getQueryTableComponent().toString().toLowerCase();
                        else
                            table = qcomponent.toString().toLowerCase();
                        if (!tables.contains(table)) {
                            tables.add(table);
                        }
                    }
                }
            }
        }
        return tables;
    }


    public static void main(String[] args) {
        String sql = " SELECT DISTINCT o.order_id, o.order_number,o.contract_number,"
                + " o.nc_create_date,"
                + "o.totality,"
                + " o.transport_type,"
                + "  o.order_type,"
                + "o.urgent,"
                + " o.order_status,"
                + "o.earliest_reach_date,"
                + " o.latest_reach_date,"
                + "o.nc_earliest_reach_date,"
                + " o.nc_latest_reach_date,"
                + " o.process_status,"
                + " case when ctc.perm_date is not null THEN"
                + " '订单还有' || to_char(TO_DATE(to_char(ctc.perm_date, 'YYYY-MM-DD'),"
                + "   'YYYY-MM-DD') -  to_date(to_char(SYSDATE, 'YYYY-MM-DD'), 'YYYY-MM-DD'))"
                + " || '天过期 ' || to_char(o.remark) else to_char(o.remark)"
                + " end as remark, t.tm_id,t.transport_code,t.plate_number,"
                + " (case when o.process_status = '2' then   1 else 0 end) as stowage,"
                + " c.custom_id, c.custom_name,  r.repertory_name,r.repertory_id, r.unit_id,"
                + " reference.code_desc order_status_name,"
                + "referenceStatus.code_desc process_status_name,"
                + " refeTransportType.code_desc TRANSPORT_TYPE_NAME"
                + " FROM (select * from tb_order where contract_number not in (SELECT order_id"
                + " FROM TB_C_TRANS_CERT  where perm_date is not null and perm_date &lt; sysdate"
                + " and perm_days is not null and perm_days &lt;&gt; 0)) o"
                + " LEFT JOIN TB_C_TRANS_CERT ctc on o.contract_number = ctc.order_id LEFT JOIN tb_repertory r"
                + " ON r.repertory_id = o.repertory_id  and r.ACTIVATE = '1' LEFT JOIN tb_customer c ON o.custom_id = c.custom_id"
                + " and c.ACTIVATE = '1'  LEFT JOIN (select tm.tm_id, tm.transport_code,"
                + "tm.plate_number,  ts.contract_number from tb_transport_manage tm "
                + "inner join tb_transport_stop ts on tm.tm_id = ts.tm_id and ts.stop_type = 67"
                + " where tm.transport_manage_status != '10') t  on t.contract_number = o.contract_number"
                + "  LEFT JOIN (SELECT leaf.id, leaf.code, leaf.code_desc  FROM tb_reference parent"
                + " INNER JOIN tb_reference leaf  ON parent.id = leaf.parent_id"
                + " WHERE parent.code = 'endProductOrderStatus') reference"
                + " ON trim(reference.code) = trim(o.order_status)"
                + "  LEFT JOIN (SELECT leaf.id, leaf.code, leaf.code_desc"
                + "  FROM tb_reference parent"
                + " INNER JOIN tb_reference leaf"
                + "  ON parent.id = leaf.parent_id"
                + " WHERE parent.code = 'processStatus') referenceStatus"
                + "  ON trim(referenceStatus.code) = trim(o.process_status)"
                + " LEFT JOIN (SELECT leaf.id, leaf.code, leaf.code_desc"
                + " FROM tb_reference parent"
                + " INNER JOIN tb_reference leaf "
                + " ON parent.id = leaf.parent_id"
                + " WHERE parent.code = 'transportType') refeTransportType"
                + " ON trim(refeTransportType.code) = trim(o.transport_type)";

        sql = "	 SELECT O.*,C.*,CA.*,D.*,P.*,R.* "
                + "FROM TB_ORDER O "
                + " INNER JOIN TB_CUSTOMER  ON O.CUSTOM_ID=C.CUSTOM_ID "
                + " INNER JOIN TB_CUSTOMER_ADDRESS  ON CA.custom_code=C.custom_code"
                + " INNER JOIN TB_ORDER_DETAIL D ON O.ORDER_ID = D.ORDER_ID"
                + " INNER JOIN TB_REPERTORY R ON  R.REPERTORY_ID=O.REPERTORY_ID"
                + " INNER JOIN TB_PRODUCT P ON D.PRODUCT_ID=P.PRODUCT_ID "
                + " WHERE (O.PROCESS_STATUS ='0' OR  O.PROCESS_STATUS IS NULL or O.PROCESS_STATUS ='3')";
        List userRestrictions = new ArrayList();
        Restrict r = new Restrict();
        r.setRoleId("1");
        r.setTableName("tb_order");
        r.setColumnName("REPERTORY_ID");
        r.setColumnType("text");
        r.setOptCode("=");
        r.setRestrictValue("1");

        Restrict r1 = new Restrict();
        r1.setRoleId("1");
        r1.setTableName("tb_order");
        r1.setColumnName("CUSTOM_ID");
        r1.setColumnType("text");
        r1.setOptCode("=");
        r1.setRestrictValue("2");
        Restrict r2 = new Restrict();
        r2.setRoleId("2");
        r2.setTableName("TB_ORDER_DETAIL");
        r2.setColumnName("CUSTOM_ID");
        r2.setColumnType("text");
        r2.setOptCode("=");
        r2.setRestrictValue("2");
        Restrict r3 = new Restrict();
        r3.setRoleId("2");
        r3.setTableName("TB_CUSTOMER_ADDRESS");
        r3.setColumnName("CUSTOM_ID");
        r3.setColumnType("text");
        r3.setOptCode("=");
        r3.setRestrictValue("2");
        userRestrictions.add(r);
        userRestrictions.add(r1);
        userRestrictions.add(r2);
        userRestrictions.add(r3);
        // addConstraints(sql, userRestrictions);
    }

    private static void log(final boolean debug, final String message) {
        if (debug) {
            logger.info(message);
        } else {
            logger.debug(message);
        }
    }

    private final static Logger logger = Logger.getLogger(QueryUtil.class);
}
