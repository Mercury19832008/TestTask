package ru.fors.sample.core;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fors.sample.core.dto.Dto;
import ru.fors.sample.core.sc.ISearchCondition;
import ru.fors.sample.core.sc.SearchCondition;

import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("baseService")
public class BaseServiceImpl extends WebpmServiceImpl implements BaseService {
    @Autowired
    protected BaseDao baseDao;

    public <T> List<T> find(ISearchCondition<T> sc) {
        return baseDao.find((SearchCondition<T>) sc);
    }

    @Override
    public <T> T findSingle(ISearchCondition<T> sc) {
        List<T> list = baseDao.find((SearchCondition<T>) sc);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List find(final String hqlQueryString) {
        return baseDao.find(hqlQueryString);
    }

    public int count(ISearchCondition sc) {
        return baseDao.count((SearchCondition) sc);
    }

    public <T> T getById(Class<T> clazz, Serializable id) {
        return baseDao.loadLazy(clazz, id);
    }

    @Override
    public <T> List<T> loadAll(Class<T> objectClass) {
        return baseDao.loadAll(objectClass);
    }

    @Override
    public <T> T loadByField(Class<T> clazz, String fieldName, Object value) {
        return baseDao.loadByField(clazz, value, fieldName);
    }

    @Override
    public <T> T loadByCode(Class<T> clazz, String value) {
        return loadByField(clazz, "code", value);
    }

    public void refresh(Object entity) {
        baseDao.refresh(entity);
    }


    public <T> T reload(Class<T> clazz, Serializable id) {
        return baseDao.reload(clazz, id);
    }

    public <T> T getExternalById(Class<T> clazz, Long id) {
        return baseDao.loadLazy(clazz, id);
    }

    @Transactional(readOnly = false)
    public <T> T store(T entity) {
        T saved = baseDao.store(entity);
        BeanUtils.copyProperties(saved, entity, getTransientFields(entity.getClass()));
        if (entity instanceof Dto)
            try {
                entity.getClass().getMethod("setId", ((Dto) saved).getId().getClass()).invoke(entity, ((Dto) saved).getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        return entity;
    }

    @Override
    @Transactional(readOnly = false)
    public <T> void storeBatch(List<T> objects) {
        baseDao.storeBatch(objects);
    }

    private String[] getTransientFields(Class clazz) {
        List<String> result = new ArrayList<String>();
        for (Field field : clazz.getDeclaredFields())
            if (field.getAnnotation(Transient.class) != null)
                result.add(field.getName());
        return result.toArray(new String[result.size()]);
    }

    @Transactional(readOnly = false)
    public void delete(Object object) {
        baseDao.delete(object);
    }

    @Transactional(readOnly = false)
    public void delete(Class clazz, Serializable id) {
        baseDao.delete(clazz, id);
    }

    @Transactional(readOnly = false)
    public List<Map<String, Serializable>> executeNativeQuery(String query, Object... params) {
        return baseDao.executeNativeQuery(query, params);
    }

    @Transactional(readOnly = false)
    public Object executeNativeFunction(String query, Object... params) {
        return baseDao.executeNativeFunction(query, params);
    }

    @Override
    @Transactional(readOnly = false)
    public void hqlUpdate(String queryString, Object... values) {
        baseDao.executeHqlUpdate(queryString, values);
    }

    @Override
    public void executeHqlUpdate(final String queryString, final Object... values) {
        baseDao.executeHqlUpdate(queryString, values);
    }

    @Override
    public List executeHqlQuery(final String queryString, final Object... params) {
        return baseDao.executeHqlQuery(queryString, params);
    }


    @Override
    public void flushSession() {
        baseDao.flushSession();
    }

    @Override
    public void clearSession() {
        baseDao.flushSession();
    }

    @Override
    public void evict(Object o) {
        baseDao.evict(o);
    }
}
