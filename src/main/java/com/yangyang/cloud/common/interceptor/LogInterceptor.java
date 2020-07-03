package com.yangyang.cloud.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.yangyang.cloud.common.bean.User;
import com.yangyang.cloud.keycloak.SecurityContextUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.util.*;

/**
 * 查询日志拦截器
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
        }
)
public class LogInterceptor implements Interceptor {

    private static final Logger queryLog = LoggerFactory.getLogger("queryLog");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取xml中的一个select/update/insert/delete节点，主要描述的是一条SQL语句
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        // 获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        // BoundSql就是封装myBatis最终产生的sql类
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        // 获取节点的配置
        Configuration configuration = mappedStatement.getConfiguration();
        Object returnVal = invocation.proceed();
        //获取sql和参数
        HashMap<String, Object> map = getSql(configuration, boundSql);
        //获取userId userName
        User loginUser = SecurityContextUtil.getLoginUser();
        if (loginUser != null) {
            queryLog.info("user_ID: {} ;user_name : {} ;parmas: {} ;sql : {} ;", loginUser.getId(), loginUser.getName(),
                    map.get("params"), map.get("sql"));
        } else {
            queryLog.info("user_ID: System ;user_name : System ;parmas: {} ;sql : {} ;", map.get("params"), map.get("sql"));
        }
        return returnVal;
    }

    /**
     * 获取SQL
     *
     * @param configuration
     * @param boundSql
     * @return
     */
    private HashMap<String, Object> getSql(Configuration configuration, BoundSql boundSql) {
        HashMap<String, Object> map = new HashMap<>();
        JSONObject params = new JSONObject();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterObject == null || parameterMappings.size() == 0) {
            map.put("sql", sql);
            map.put("params", "{}");
            return map;
        }
        // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
        MetaObject metaObject = configuration.newMetaObject(parameterObject);
        if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
            String key = parameterMappings.get(0).getProperty();
            String value = getParameterValue(parameterObject);
            sql = sql.replaceFirst("\\?", value);
            params.put(key, value);
        } else {
            for (ParameterMapping parameterMapping : parameterMappings) {
                String propertyName = parameterMapping.getProperty();
                String key = propertyName;
                Object value = null;
                if (propertyName.contains(".")) {
                    String[] keys = propertyName.split("\\.");
                    key = keys[keys.length - 1];
                }
                if (metaObject.hasGetter(propertyName)) {
                    value = metaObject.getValue(propertyName);
                    sql = sql.replaceFirst("\\?", getParameterValue(value));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    value = boundSql.getAdditionalParameter(propertyName);
                    sql = sql.replaceFirst("\\?", getParameterValue(value));
                }
                params.put(key, value);
            }
        }
        map.put("sql", sql);
        map.put("params", params);
        return map;
    }

    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
