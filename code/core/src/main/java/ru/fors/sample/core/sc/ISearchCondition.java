package ru.fors.sample.core.sc;

import java.io.Serializable;
import java.util.Map;

/**
 * Author: Alexey Perminov
 * Date: 26.06.2010
 * Time: 14:18:45
 */
public interface ISearchCondition<T> extends Serializable {

    Map<String, Boolean> getOrderBy();

    void setOrderBy(Map<String, Boolean> orderBy);

    Integer getPageNo();

    void setPageNo(Integer pageNo);

    Integer getPageSize();

    void setPageSize(Integer pageSize);

    void addOrderBy(String field, boolean asc);
}
