package nl.ajax.alert.db;

import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import nl.ajax.alert.db.models.Subscription;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

@Slf4j
public class SubscriptionDAO extends AbstractDAO<Subscription> {
    public SubscriptionDAO(final SessionFactory sessionFactory) { super(sessionFactory);}

    public List<Subscription> findAllSubscriptionsByMatchAgainst(String matchAgainst) {
        try (Session session = currentSession()) {
            Query<Subscription> query = session.createQuery("FROM Subscription WHERE matchAgainst = :matchAgainst", Subscription.class);
            query.setParameter("matchAgainst", matchAgainst);
            return query.list();
        } catch (Exception e) {
            log.error("Failed to find subscriptions by matchAgainst: {}", matchAgainst, e);
            throw e;
        }
    }

    public void save(Subscription subscription) { persist(subscription); }

    public void delete(Subscription subscription) {
        try (Session session = currentSession()) {
            session.beginTransaction();
            session.remove(subscription);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Failed to delete subscription: {}", subscription.getId(), e);
            throw e;
        }
    }}
