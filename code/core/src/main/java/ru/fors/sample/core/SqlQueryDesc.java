package ru.fors.sample.core;

import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.type.Type;

import java.io.Serializable;

/**
 * Author: Alexey Perminov
 * Date: 24.10.2010
 * Time: 23:25:55
 */
public class SqlQueryDesc implements Serializable {
    private String sql;
    private Object[] params;
    private Type[] types;

    public SqlQueryDesc() {
    }

    public SqlQueryDesc(String sql, Object[] params, Type[] types) {
        this.sql = sql;
        this.params = params;
        this.types = types;
    }

    public Object[] getParams() {
        return params;
    }

    public String getSql() {
        return sql;
    }

    public Type[] getTypes() {
        return types;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setTypes(Type[] types) {
        this.types = types;
    }

    public String getRootAlias() {
        return CriteriaQueryTranslator.ROOT_SQL_ALIAS;
    }

    public String getSelectIdSql() {
        return "select " + getRootAlias() + ".id " + getSql().substring(getSql().indexOf("from"));
    }
}
