package ru.fors.sample.core;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.loader.spi.AfterLoadAction;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.hibernate.transform.RootEntityResultTransformer;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.fors.sample.core.exception.ObjectNotFoundException;
import ru.fors.sample.core.exception.ValidationException;
import ru.fors.sample.core.sc.SearchCondition;
import ru.fors.sample.core.utils.PersistenceUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.sql.Date;
import java.util.*;

//@Repository("baseDao") - iám set disabled because for started application throw error:
//http://gal-levinsky.blogspot.ru/2012/03/resolve-circular-dependency-in-spring.html

//now use: <bean id="baseDao" class="ru.fors.sample.core.BaseDaoImpl" lazy-init="true"></bean>
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class BaseDaoImpl implements BaseDao {
    @PersistenceContext
    protected EntityManager entityManager;

    public <T> T load(Class<T> clazz, Serializable id) {
        if (id == null)
            return null;
        T result = entityManager.find(clazz, id);
        if (result == null)
            throw new ObjectNotFoundException("Запрашиваемый объект не найден в БД, возможно он был удален. Обновите пожалуйста страницу.");
        return result;
    }

    public <T> T reload(Class<T> clazz, Serializable id) {
        if (id == null)
            return null;
        T result = entityManager.find(clazz, id);
        if (result == null)
            throw new ObjectNotFoundException("Запрашиваемый объект не найден в БД, возможно он был удален. Обновите пожалуйста страницу.");
        entityManager.refresh(result);
        return result;
    }

    public void refresh(Object entity) {
        entityManager.refresh(entity);
    }

    public <T> T loadLazy(Class<T> clazz, Serializable id) {
        if (id == null)
            return null;
        T obj = entityManager.find(clazz, id);
        if (obj == null)
            throw new ObjectNotFoundException("Requested object (" + clazz.getSimpleName() + ", id=" + id.toString() + ") not found.");
        PersistenceUtils.fetchLazy(obj);
        return obj;
    }

    public <T> T loadBySysname(Class<T> clazz, String sysnameValue) {
        return loadByField(clazz, sysnameValue, "sysname");
    }

    public <T> T loadLazyBySysname(Class<T> clazz, String sysnameValue) {
        T obj = loadByField(clazz, sysnameValue, "sysname");
        PersistenceUtils.fetchLazy(obj);
        return obj;
    }

    public List find(String queryString) throws DataAccessException {
        return find(queryString, (Object[]) null);
    }

    @Override
    public List find(final String queryString, final Object... values) {
        return find(queryString, -1, -1, values);
    }

    @Override
    public List find(final String hqlQueryString, int offset, int limit, final Object... values) {
        Query queryObject = entityManager.createQuery(hqlQueryString);
        if (offset >= 0)
            queryObject.setFirstResult(offset);
        if (limit >= 0)
            queryObject.setMaxResults(limit);
        if (values != null)
            for (int i = 0; i < values.length; i++)
                queryObject.setParameter(i, values[i]);
        List result = queryObject.getResultList();
        return result;
    }

    @Override
    public void executeHqlUpdate(final String queryString, final Object... values) {
        entityManager.flush();
        Query queryObject = entityManager.createQuery(queryString);
        if (values != null)
            for (int i = 0; i < values.length; i++)
                queryObject.setParameter(i , values[i]);
        queryObject.executeUpdate();
    }

    @Override
    public List executeHqlQuery(final String queryString, final Object... params) {
        entityManager.flush();
        Query queryObject = entityManager.createQuery(queryString);
        if (params != null)
            for (int i = 0; i < params.length; i++)
                queryObject.setParameter(i , params[i]);
        return queryObject.getResultList();
    }

    @SuppressWarnings({"JpaQlInspection"})
    public <T> T loadByField(Class<T> clazz, Object value, String field) {
        List<T> list = find("select o from " + clazz.getName() + " o where o." + field + "=?", value);
        if (list.isEmpty())
            return null;
        PersistenceUtils.fetchLazy(list.get(0));
        return list.get(0);
    }

    public <T> T store(T obj) {
        T result = obj == null ? null : entityManager.merge(obj);
        PersistenceUtils.fetchLazy(result);
        return result;
    }

    @Override
    public <T> void storeBatch(List<T> objects) {
        for (T obj : objects) {
            if (obj != null)
                entityManager.merge(obj);
        }
    }

    public void delete(Class clazz, Serializable id) {
        Object obj = loadLazy(clazz, id);
        if (obj != null) {
            entityManager.remove(obj);
            entityManager.flush();
        }
    }

    public void delete(Object object) {
        entityManager.remove(object);
        entityManager.flush();
    }

    public <T> List<T> load(Class<T> objectClass, List<? extends Serializable> ids) {
        List<T> res = new ArrayList<T>(ids.size());
        for (Serializable id : ids)
            res.add(load(objectClass, id));
        for (Iterator<T> iterator = res.iterator(); iterator.hasNext(); )
            if (iterator.next() == null)
                iterator.remove();
        return res;
    }

    @SuppressWarnings({"JpaQlInspection"})
    public <T> List<T> loadAll(Class<T> objectClass) {
        List<T> result = find("select o from " + objectClass.getName() + " o");
        return result;
    }

    protected Session getSession() {
        return (Session) entityManager.getDelegate();
    }

    public int count(final SearchCondition condition) {
        if (condition == null)
            return 0;
        Criteria criteria = getSession().createCriteria(condition.getPersistentClass());
        condition.buildCriteriaInt(criteria, true);
        criteria.setProjection(Projections.rowCount());
        return ((Number) criteria.list().get(0)).intValue();
    }

    public <T> List<T> find(final SearchCondition<T> condition) {
        if (condition == null)
            return new ArrayList<T>();
        Criteria criteria = getSession().createCriteria(condition.getPersistentClass());
        condition.buildCriteriaInt(criteria, false);
        if (criteria instanceof CriteriaImpl && ((CriteriaImpl) criteria).getResultTransformer() == RootEntityResultTransformer.INSTANCE)
            criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
        List<T> result = criteria.list();
        return result;
    }

    public List<Long> execute(final String query) {
        entityManager.flush();
        List db = getSession().createSQLQuery(query).list();
        List<Long> result = new ArrayList<Long>(db.size());
        for (Object value : db)
            result.add(((Number) value).longValue());
        return result;
    }

    public List<Object[]> executeAny(final String query, final int size) {
        SQLQuery sql = getSession().createSQLQuery(query);
        sql.setFetchSize(size > 0 ? size : 100);
        if (size > 0)
            sql.setMaxResults(size);
        return sql.list();
    }

    public int executeUpdate(String... query) {
        entityManager.flush();
        int result = 0;
        for (final String sql : query)
            result += getSession().createQuery(sql).executeUpdate();
        return result;
    }

    @SuppressWarnings({"deprecation"})
    public List<Map<String, Serializable>> executeNativeQuery(String query, Object... params) {
        final IModel<List<Map<String, Serializable>>> result = new Model(new ArrayList<Map<String, Serializable>>());
        try {
            getSession().doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    if (query.startsWith("{") || query.startsWith("call")) {
                        entityManager.flush();
                        CallableStatement statement = null;
                        try {
                            statement = connection.prepareCall(query);
                            int index = 1;
                            for (Object param : params) {
                                statement.setObject(index, param);
                                index++;
                            }
                            statement.execute();
                        } finally {
                            if (statement != null) {
                                statement.close();
                            }
                        }
                        result.setObject(null);
                    } else {

                        PreparedStatement ps = null;
                        try {
                            ps = connection.prepareStatement(query);
                            if (params != null)
                                for (int i = 0; i < params.length; i++)
                                    ps.setObject(i + 1, modifySQLStringParameter(params[i]));
                            if (query.startsWith("update") || query.startsWith("delete") || query.startsWith("insert")
                                    || query.startsWith("analyze")) {
                                ps.executeUpdate();
                            } else {
                                ResultSet rs = ps.executeQuery();
                                while (rs.next()) {
                                    Map<String, Serializable> row = new HashMap<String, Serializable>() {
                                        @Override
                                        public Serializable get(Object key) {
                                            if (key != null)
                                                key = ((String) key).toLowerCase();
                                            return super.get(key);
                                        }
                                    };
                                    ResultSetMetaData metaData = rs.getMetaData();
                                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                                        String name = metaData.getColumnName(i);
                                        Object obj = rs.getObject(i);
                                        if (obj instanceof Date)
                                            obj = rs.getTimestamp(i);
                                        row.put(name, (Serializable) modifyReturnObject(obj));
                                    }
                                    result.getObject().add(row);
                                }
                            }
                        } finally {
                            if (ps != null)
                                ps.close();
                        }
                    }
                }
            });

            return result.getObject();
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"deprecation"})
    public Object executeNativeFunction(String query, Object... params) {
        final IModel<Object> result = new Model();
        try {

            getSession().doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    entityManager.flush();
                    CallableStatement statement = null;
                    try {
                        statement = connection.prepareCall(query);

                        statement.registerOutParameter(1, Types.NUMERIC);
                        int index = 2;
                        for (Object param : params) {
                            statement.setObject(index, param);
                            index++;
                        }
                        statement.execute();
                        result.setObject(statement.getObject(1));
                    } finally {
                        if (statement != null) {
                            statement.close();
                        }
                    }
                }
            });

            return result.getObject();

        } catch (HibernateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Object modifySQLStringParameter(Object value) {
        if (value instanceof String)
            return StringUtils.replace((String) value, "'", "''");
        if (value instanceof java.util.Date)
            return new Timestamp(((java.util.Date) value).getTime());
        return value;
    }

    private Object modifyReturnObject(Object o) {
        if (o == null)
            return null;
        if (o instanceof BigDecimal)
            return ((BigDecimal) o).doubleValue();

        if (o instanceof BigInteger)
            return ((BigInteger) o).longValue();
        return o;
    }

    @Override
    public SqlQueryDesc getCriteriaSql(final SearchCondition condition, boolean aggregate) {
        if (condition == null)
            return null;
        Criteria criteria = getSession().createCriteria(condition.getPersistentClass());
        condition.buildCriteriaInt(criteria, aggregate);
        if (aggregate)
            criteria.setProjection(Projections.rowCount());
        final SqlQueryDesc result = new SqlQueryDesc();
        final SessionFactoryImplementor factory = getSessionFactory();
        CriteriaLoader loader = new CriteriaLoader(
                (OuterJoinLoadable) factory.getEntityPersister(condition.getPersistentClass().getName()),
                factory,
                (CriteriaImpl) criteria,
                factory.getImplementors(condition.getPersistentClass().getName())[0],
                new LoadQueryInfluencers(factory)
        ) {
            @Override
            protected String preprocessSQL(String sql, QueryParameters parameters, Dialect dialect,
                                           List<AfterLoadAction> afterLoadActions) throws HibernateException {
                result.setSql(parameters.getFilteredSQL());
                result.setParams(parameters.getFilteredPositionalParameterValues());
                result.setTypes(parameters.getFilteredPositionalParameterTypes());
                throw new ValidationException("dummy");
            }
        };
        try {
            loader.list((SessionImplementor) getSession());
        } catch (ValidationException e) {
        }
        return result;
    }

    public SessionFactoryImplementor getSessionFactory() {
        return (SessionFactoryImplementor) ((Session) entityManager.getDelegate()).getSessionFactory();
    }

    @Override
    public void flushSession() {
        getSession().flush();
    }

    @Override
    public void clearSession() {
        entityManager.clear();
    }

    @Override
    public void evict(Object o) {
        getSession().evict(o);
    }
}
