package ru.fors.sample.core;

import org.springframework.transaction.annotation.Transactional;
import ru.fors.sample.core.sc.ISearchCondition;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnusedDeclaration"})
public interface BaseService extends WebpmService {
    <T> List<T> find(ISearchCondition<T> sc);

    int count(ISearchCondition sc);

    <T> T getById(Class<T> clazz, Serializable id);

    <T> T reload(Class<T> clazz, Serializable id);

    <T> T getExternalById(Class<T> clazz, Long id);

    <T> T store(T object);

    void delete(Object object);

    void delete(Class clazz, Serializable id);

    List<Map<String, Serializable>> executeNativeQuery(String query, Object... params);

    Object executeNativeFunction(String query, Object... params);

    void refresh(Object entity);

    void hqlUpdate(String queryString, Object... values);

    List find(String hqlQueryString);

    <T> T findSingle(ISearchCondition<T> sc);

    List executeHqlQuery(String queryString, Object... params);

    void executeHqlUpdate(String queryString, Object... values);

    <T> List<T> loadAll(Class<T> objectClass);

    <T> T loadByField(Class<T> clazz, String fieldName, Object value);

    void flushSession();

    void evict(Object o);

    void clearSession();

    <T> T loadByCode(Class<T> clazz, String value);

	@Transactional(readOnly = false)
	<T> void storeBatch(List<T> objects);
}
