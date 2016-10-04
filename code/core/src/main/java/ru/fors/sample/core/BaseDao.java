package ru.fors.sample.core;


import org.hibernate.engine.spi.SessionFactoryImplementor;
import ru.fors.sample.core.sc.SearchCondition;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnusedDeclaration"})
public interface BaseDao {
    <T> T load(Class<T> clazz, Serializable id);

    <T> T reload(Class<T> clazz, Serializable id);

    void refresh(Object entity);

    <T> T loadLazy(Class<T> clazz, Serializable id);

    <T> T loadBySysname(Class<T> clazz, String sysnameValue);

    <T> T loadLazyBySysname(Class<T> clazz, String sysnameValue);

    <T> T loadByField(Class<T> clazz, Object value, String field);

    <T> T store(T obj);

    void delete(Class clazz, Serializable id);

    void delete(Object object);

    <T> List<T> load(Class<T> objectClass, List<? extends Serializable> ids);

    <T> List<T> loadAll(Class<T> objectClass);

    int count(SearchCondition condition);

    <T> List<T> find(SearchCondition<T> condition);

    List<Long> execute(String query);

    List<Object[]> executeAny(String query, int size);

    int executeUpdate(String... query);

    List<Map<String, Serializable>> executeNativeQuery(String query, Object... params);

    Object executeNativeFunction(String query, Object... params);

    SessionFactoryImplementor getSessionFactory();

    void executeHqlUpdate(String queryString, Object... values);

    List find(String hql, int offset, int limit, Object... values);

    List find(String hql, Object... values);

    List executeHqlQuery(String queryString, Object... params);

    SqlQueryDesc getCriteriaSql(SearchCondition condition, boolean aggregate);

    void flushSession();

    void clearSession();

    void evict(Object o);

    <T> void storeBatch(List<T> objects);
}
