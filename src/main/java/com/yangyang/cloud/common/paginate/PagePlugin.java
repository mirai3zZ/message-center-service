package com.yangyang.cloud.common.paginate;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import javax.xml.bind.PropertyException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Mybatis实现物理分页的拦截器
 *
 * @版本 V 1.0
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }),
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})})
public class PagePlugin implements Interceptor {
    //数据库方言
    private static String dialect = "";
    //mapper.xml中需要拦截的ID(正则匹配)
    private static String pageSqlId = "";
    private static final Log logger = LogFactory.getLog(PagePlugin.class);


    /**
     * 拦截sql判断是否为分页查询
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation ivk) throws Throwable {

        Object result = null;
        String method = ivk.getMethod().getName();

        if (ivk.getTarget() instanceof RoutingStatementHandler) {
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
            BaseStatementHandler delegate = (BaseStatementHandler) ReflectHelper.getValueByFieldName(statementHandler, "delegate");
            MetaObject shMeta = SystemMetaObject.forObject(statementHandler);
            MappedStatement mappedStatement = (MappedStatement) ReflectHelper.getValueByFieldName(delegate, "mappedStatement");
            //拦截需要分页的SQL
            if (mappedStatement.getId().matches(pageSqlId)) {
                BoundSql boundSql = statementHandler.getBoundSql();
                //分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
                Object parameterObject = boundSql.getParameterObject();
                if (parameterObject == null) {
                    throw new NullPointerException("parameterObject尚未实例化！");
                } else {
                    String sql = boundSql.getSql();
                    sql = sql.replaceFirst("SELECT", "SELECT SQL_CALC_FOUND_ROWS").replaceFirst("select", "select SQL_CALC_FOUND_ROWS");
                    int count = 0;
                    Page page = null;
                    //参数就是Page实体
                    if (parameterObject instanceof Page) {
                        page = (Page) parameterObject;
                    }
                    // 使用map传参
                    else if (parameterObject instanceof Map<?, ?>) {
                        page = (Page) ((Map<String, Object>) parameterObject).get("page");
                    } else {
                        //参数为某个实体，该实体拥有Page属性
                        Field pageField = ReflectHelper.getFieldByFieldName(parameterObject, "page");
                        if (pageField != null) {
                            page = (Page) ReflectHelper.getValueByFieldName(parameterObject, "page");
                            page = page == null ? new Page() : page;
                            //通过反射，对实体对象设置分页对象
                            ReflectHelper.setValueByFieldName(parameterObject, "page", page);
                        } else {
                            throw new NoSuchFieldException(parameterObject.getClass().getName() + "不存在 page 属性！");
                        }
                    }
                    String pageSql = generatePageSql(sql, page);
                    //将分页sql语句反射回BoundSql.
                    ReflectHelper.setValueByFieldName(boundSql, "sql", pageSql);

                    shMeta.setValue("delegate.boundSql.sql", pageSql);

                    if (method.equals("prepare")) {

                        result = ivk.proceed();

                    } else if (method.equals("query")) {

                        result = ivk.proceed();
                        Statement stat = (Statement) ivk.getArgs()[0];
                        count = this.getTotal(stat.getConnection());

                        if (page != null) {
                            page.setiTotalRecords(count);
                            page.setiTotalDisplayRecords(count);
                        }
                    }
                }
            } else {
                result = ivk.proceed();
            }
        } else {
            result = ivk.proceed();
        }
        return result;
    }


    /**
     * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    @SuppressWarnings({"unused", "unchecked"})
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
                    @SuppressWarnings("rawtypes")
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

    /**
     * 根据数据库方言，生成特定的分页sql
     *
     * @param sql
     * @param page
     * @return
     */
    private String generatePageSql(String sql, Page page) {
        if (page != null && StringUtils.isNotEmpty(dialect)) {
            StringBuffer pageSql = new StringBuffer();
            if ("mysql".equals(dialect)) {
                pageSql.append(sql);
                pageSql.append(" limit " + page.getStart() + "," + page.getLength());
            } else if ("oracle".equals(dialect)) {
                pageSql.append("select * from (select tmp_tb.*,ROWNUM row_id from (");
                pageSql.append(sql);
                pageSql.append(") as tmp_tb where ROWNUM<=");
                pageSql.append(page.getStart() + page.getLength());
                pageSql.append(") where row_id>");
                pageSql.append(page.getStart());
            }
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    /**
     *
     */
    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    /**
     * 设置参数
     */
    @Override
    public void setProperties(Properties p) {
        dialect = p.getProperty("dialect");
        if (StringUtils.isEmpty(dialect)) {
            try {
                throw new PropertyException("dialect property is not found!");
            } catch (PropertyException e) {
                e.printStackTrace();
                logger.error("系统分页设置参数异常");
            }
        }
        pageSqlId = p.getProperty("pageSqlId");
        if (StringUtils.isEmpty(pageSqlId)) {
            try {
                throw new PropertyException("pageSqlId property is not found!");
            } catch (PropertyException e) {
                e.printStackTrace();
                logger.error("系统分页设置参数异常");
            }
        }
    }


    private int getTotal(Connection connection) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("select found_rows()");
            ps.execute();
            rs = ps.getResultSet();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("picp系统获取查询总数异常");
            }
        }
    }
}
