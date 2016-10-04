package ru.fors.sample.core;

import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author Mikhail Kokorin
 *         date: 20.09.2016
 *         time: 10:28
 */
public class CustomHibernateJpaVendorAdapter extends HibernateJpaVendorAdapter {
    private final HibernateJpaDialect jpaDialect = new CustomHibernateJpaDialect();

    @Override
    public HibernateJpaDialect getJpaDialect() {
        return jpaDialect;
    }
}
