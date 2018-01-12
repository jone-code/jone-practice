/*
 * @(#)MySqlIntercept.java 2017年12月20日下午2:20:12 zbjs Copyright 2017 Thuisoft,
 * Inc. All rights reserved. THUNISOFT PROPRIETARY/CONFIDENTIAL. Use is subject
 * to license terms.
 */
package com.thunisoft.zbjs.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * MySqlIntercept
 * @author huayu
 * @version 1.0
 *
 */
@Intercepts(@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }))
public class MySqlIntercept implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object obj = invocation.getTarget();
        if (obj instanceof StatementHandler) {
            MetaObject metaStatementHandler = SystemMetaObject.forObject(obj);
            while (metaStatementHandler.hasGetter("h")) {
                Object plugin = metaStatementHandler.getValue("h");
                metaStatementHandler = SystemMetaObject.forObject(plugin);
                Object target = metaStatementHandler.getValue("target");
                metaStatementHandler = SystemMetaObject.forObject(target);
            }
            String sql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
            //解析sql 
            sql = parseSql(sql);
            //再把sql装回去
            metaStatementHandler.setValue("delegate.boundSql.sql", sql);
        } else {
            System.out.println("类型不对");
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 解析sql
     * @param sql
     * @return
     */
    private String parseSql(String sql) {
        StringBuilder sb = new StringBuilder(sql);
        if (sql.contains("where") || sql.contains("WHERE")) {
            int len = sb.indexOf("where");
            sb.insert(len + 5, "1=1 and ");
        } else {
            sb.append(" where 1=1");
        }
        return sb.toString();
    }
}
