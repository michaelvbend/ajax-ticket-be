package nl.ajax.alert;

import io.dropwizard.core.setup.Environment;
import nl.ajax.alert.client.TwilioService;
import nl.ajax.alert.core.MatchService;
import nl.ajax.alert.core.NotificationService;
import nl.ajax.alert.core.SubscribeService;
import nl.ajax.alert.db.MatchDAO;
import nl.ajax.alert.db.SubscriptionDAO;
import nl.ajax.alert.resources.MatchResource;
import nl.ajax.alert.resources.SubscribeResource;
import org.mockito.Mockito;

public class TestAjaxAlertingApplication extends AjaxAlertingApplication {
    private static TwilioService twilioServiceMock = Mockito.mock(TwilioService.class);

    public TestAjaxAlertingApplication() {
        super(); // Call parent constructor
    }

    @Override
    public void run(AjaxAlertingConfiguration configuration, Environment environment) {
        final MatchDAO matchDAO = new MatchDAO(getHibernateBundle().getSessionFactory());
        final SubscriptionDAO subscriptionDAO = new SubscriptionDAO(getHibernateBundle().getSessionFactory());

        NotificationService notificationService = new NotificationService(twilioServiceMock, new SubscribeService(subscriptionDAO));
        MatchService matchService = new MatchService(matchDAO);
        matchService.addListener(notificationService);

        environment.jersey().register(new MatchResource(matchService));
        environment.jersey().register(new SubscribeResource(new SubscribeService(subscriptionDAO)));
    }

    public static void setTwilioServiceMock(TwilioService mock) {
        twilioServiceMock = mock;
    }
}
