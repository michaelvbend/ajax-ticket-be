package nl.ajax.alert.core;

import nl.ajax.alert.api.request.SubscriptionRequest;
import nl.ajax.alert.db.SubscriptionDAO;
import nl.ajax.alert.db.models.Subscription;

import java.util.List;
import java.util.UUID;

public class SubscribeService {

    private final SubscriptionDAO subscriptionDAO;

    public SubscribeService(SubscriptionDAO subscriptionDAO) {
        this.subscriptionDAO = subscriptionDAO;
    }

    public List<Subscription> getSubscriptionDetails(String matchAgainst) {
        return subscriptionDAO.findAllSubscriptionsByMatchAgainst(matchAgainst);
    }

    public void saveSubscriptionDetails(SubscriptionRequest subscriptionRequest) {
        subscriptionDAO.save(new Subscription(subscriptionRequest.getEmail(), subscriptionRequest.getMatchAgainst()));
    }

    public void deleteSubscriptionDetails(UUID userToken) {
        subscriptionDAO.delete(userToken);
    }
}
