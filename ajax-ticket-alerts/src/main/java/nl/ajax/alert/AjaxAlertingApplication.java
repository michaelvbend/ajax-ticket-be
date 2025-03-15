package nl.ajax.alert;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.dropwizard.core.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import nl.ajax.alert.core.SubscribeService;
import nl.ajax.alert.client.TwilioService;
import nl.ajax.alert.core.MatchService;
import nl.ajax.alert.core.NotificationService;
import nl.ajax.alert.db.MatchDAO;
import nl.ajax.alert.db.SubscriptionDAO;
import nl.ajax.alert.db.models.Match;
import nl.ajax.alert.db.models.Subscription;
import nl.ajax.alert.resources.MatchResource;
import nl.ajax.alert.resources.SubscribeResource;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import java.util.EnumSet;

public class AjaxAlertingApplication extends Application<AjaxAlertingConfiguration> {
    private final HibernateBundle<AjaxAlertingConfiguration> hibernateBundle =
            new HibernateBundle<>(Match.class, Subscription.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(AjaxAlertingConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    public static void main(final String[] args) throws Exception {
        new AjaxAlertingApplication().run(args);
    }

    @Override
    public void initialize(final Bootstrap<AjaxAlertingConfiguration> bootstrap) {
        bootstrap.getObjectMapper().registerModule(new ParameterNamesModule());
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final AjaxAlertingConfiguration configuration,
                    final Environment environment) {
        enableCors(environment);
        final MatchDAO matchDAO = new MatchDAO(hibernateBundle.getSessionFactory());
        final SubscriptionDAO subscriptionDAO = new SubscriptionDAO(hibernateBundle.getSessionFactory());
        final SubscribeService subscribeService = new SubscribeService(subscriptionDAO);

        TwilioService twilioService = new TwilioService(
                configuration.getTwilioAccountSid(),
                configuration.getTwilioAuthToken()
        );
        NotificationService notificationService = new NotificationService(twilioService, subscribeService);
        MatchService matchService = new MatchService(matchDAO);
        matchService.addListener(notificationService);
        environment.jersey().register(new MatchResource(matchService));
        environment.jersey().register(new SubscribeResource(subscribeService));

    }
    private void enableCors(Environment environment) {
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,POST,PUT,DELETE,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    }
    public HibernateBundle<AjaxAlertingConfiguration> getHibernateBundle() {
        return hibernateBundle;
    }
}
