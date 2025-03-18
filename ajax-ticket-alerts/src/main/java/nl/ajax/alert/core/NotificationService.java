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
        boolean matchTicketAvailable = !matchDTO.isSoldOut();

        if (matchTicketAvailable) {
            List<String> emailList = subscribeService.getSubscriptionDetails(matchDTO.getAwayTeam())
                    .stream()
                    .map(Subscription::getEmail)
                    .toList();

            twilioService.sendEmail(emailList, matchDTO);
        }
    }
}
