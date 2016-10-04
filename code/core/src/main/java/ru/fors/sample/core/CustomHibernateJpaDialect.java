package ru.fors.sample.core;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

import javax.persistence.PersistenceException;

/**
 * @author Mikhail Kokorin
 *         date: 20.09.2016
 *         time: 10:22
 *         needs to decide
 *         java.lang.NoSuchMethodError: org.hibernate.Session.getFlushMode()Lorg/hibernate/FlushMode;
 *         <p>
 *         decide:
 *         http://blog.arnoldgalovics.com/2016/06/09/session-level-hibernate-jdbc-batching/
 */
public class CustomHibernateJpaDialect extends HibernateJpaDialect {
    public CustomHibernateJpaDialect() {
        super();
    }

    @Override
    protected FlushMode prepareFlushMode(final Session session, final boolean readOnly) throws PersistenceException {
        final FlushMode flushMode = session.getHibernateFlushMode();
        if (readOnly) {
            // We should suppress flushing for a read-only transaction.
            if (!flushMode.equals(FlushMode.MANUAL)) {
                session.setFlushMode(FlushMode.MANUAL);
                return flushMode;
            }
        } else {
            // We need AUTO or COMMIT for a non-read-only transaction.
            if (flushMode.lessThan(FlushMode.COMMIT)) {
                session.setFlushMode(FlushMode.AUTO);
                return flushMode;
            }
        }
        // No FlushMode change needed...
        return null;
    }
}
