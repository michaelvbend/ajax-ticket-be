package nl.ajax.alert.core;

import nl.ajax.alert.api.MatchDTO;
import nl.ajax.alert.client.TwilioService;
import nl.ajax.alert.core.types.MatchUpdateListener;
import nl.ajax.alert.db.models.Subscription;

import java.util.List;

public class NotificationService implements MatchUpdateListener {
    private final TwilioService twilioService;
    private final SubscribeService subscribeService;

    public NotificationService(TwilioService twilioService, SubscribeService subscribeService) {
        this.twilioService = twilioService;
        this.subscribeService = subscribeService;
    }

    @Override
    public void onMatchUpdate(MatchDTO matchDTO) {
        if (!matchDTO.isSoldOut()) {
            List<Subscription> subscriptionList = subscribeService.getSubscriptionDetails(matchDTO.getAwayTeam())
                    .stream()
                    .toList();

            twilioService.sendEmail(subscriptionList, matchDTO);
        }
    }
}
