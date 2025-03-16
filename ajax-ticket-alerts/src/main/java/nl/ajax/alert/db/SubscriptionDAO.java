package nl.ajax.alert.db;

import io.dropwizard.hibernate.AbstractDAO;
import nl.ajax.alert.db.models.Subscription;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class SubscriptionDAO extends AbstractDAO<Subscription> {
    private final SessionFactory sessionFactory;
    public SubscriptionDAO(final SessionFactory sessionFactory) { super(sessionFactory); this.sessionFactory = sessionFactory; }

    public List<Subscription> findAllSubscriptions() {
        try (Session session = this.sessionFactory.openSession()) {
            return session.createQuery("FROM Subscription", Subscription.class).list();
        }
    }

    public List<Subscription> findAllSubscriptionsByMatchAgainst(String matchAgainst) {
        try (Session session = this.sessionFactory.openSession()) {
            return session.createQuery("FROM Subscription WHERE matchAgainst = :matchAgainst", Subscription.class)
                    .setParameter("matchAgainst", matchAgainst)
                    .list();
        }
    }

    public void save(Subscription subscription) { persist(subscription); }
}
