package ru.fors.sample.core.sc;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.*;

/**
 * Author: Alexey Perminov
 * Date: 26.06.2010
 * Time: 14:15:05
 */
public abstract class SearchCondition<T> implements ISearchCondition<T> {
    private Integer pageNo;
    private Integer pageSize;
    private Map<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
    private Map<String, Object> customFields = new HashMap<String, Object>();

    public SearchCondition() {
    }

    public Map<String, Boolean> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Map<String, Boolean> orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setLimit(Integer limit) {
        if (limit == null) {
            setPageNo(null);
            setPageSize(null);
        } else {
            setPageSize(limit);
            setPageNo(0);
        }
    }

    public void addOrderBy(String field, boolean asc) {
        orderBy.put(field, Boolean.valueOf(asc));
    }

    protected List<Order> getOrder(Map<String, Boolean> orders) {
        List<Order> result = new ArrayList<Order>();
        if (orders == null || orders.isEmpty())
            return result;
        for (String field : orders.keySet())
            result.add(orders.get(field) ? Order.asc(field) : Order.desc(field));
        return result;
    }

    public Map<String, Object> getCustomFields() {
        return customFields;
    }

    public final void buildCriteriaInt(Criteria criteria, boolean aggregate) {
        if (!aggregate && getPageNo() != null && getPageSize() != null) {
            criteria.setFirstResult(getPageNo() * getPageSize());
            criteria.setMaxResults(getPageSize());
        }

        for (Map.Entry<String, Object> entry : customFields.entrySet())
            criteria.add(Restrictions.eq(entry.getKey(), entry.getValue()));

        buildCriteria(criteria);
        if (orderBy != null && !orderBy.isEmpty() && !aggregate)
            for (Order order : getOrder(orderBy))
                criteria.addOrder(order);
    }

    public abstract Class<T> getPersistentClass();

    public abstract void buildCriteria(Criteria criteria);

}
