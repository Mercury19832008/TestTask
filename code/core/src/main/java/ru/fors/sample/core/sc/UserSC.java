package ru.fors.sample.core.sc;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import ru.fors.sample.core.dto.User;

/**
 * @author Mikhail Kokorin
 *         date: 04.10.2016
 *         time: 19:39
 */
public class UserSC extends SearchCondition<User> {
    private Long id;
    private String login;
    private String name;

    public UserSC() {
    }

    @Override
    public Class<User> getPersistentClass() {
        return User.class;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void buildCriteria(Criteria criteria) {
        if (id != null) {
            criteria.add(Restrictions.idEq(id));
        }
        if (!StringUtils.isEmpty(login)) {
            criteria.add(Restrictions.eq("login", login));
        }

        if (getOrderBy().isEmpty()) {
            addOrderBy("login", true);
        }
    }
}
