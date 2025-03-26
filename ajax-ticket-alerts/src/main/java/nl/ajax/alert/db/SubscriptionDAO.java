package nl.ajax.alert.db;

import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import nl.ajax.alert.db.models.Subscription;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;

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

    public void save(Subscription subscription) {
        log.info(String.valueOf(persist(subscription))); }

    public void delete(UUID userToken) {
        try {
            int deletedCount = currentSession().createMutationQuery("DELETE FROM Subscription WHERE userToken = :user_token")
                    .setParameter("user_token", userToken)
                    .executeUpdate();
            log.info("Deleted {} subscription(s) for user token: {}", deletedCount, userToken);
        } catch (Exception e) {
            log.error("Failed to delete subscription for user token: {}", userToken, e);
            throw e;
        }
    }
}
