package nl.ajax.alert;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.ajax.alert.core.MatchService;
import nl.ajax.alert.db.MatchDAO;
import nl.ajax.alert.db.models.Match;
import nl.ajax.alert.resources.MatchResource;

public class AjaxAlertingApplication extends Application<AjaxAlertingConfiguration> {
    private final HibernateBundle<AjaxAlertingConfiguration> hibernateBundle =
            new HibernateBundle<>(Match.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(AjaxAlertingConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    public static void main(final String[] args) throws Exception {
        new AjaxAlertingApplication().run(args);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void initialize(final Bootstrap<AjaxAlertingConfiguration> bootstrap) {
        bootstrap.getObjectMapper().registerModule(new ParameterNamesModule());
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final AjaxAlertingConfiguration configuration,
                    final Environment environment) {
        final MatchDAO matchDAO = new MatchDAO(hibernateBundle.getSessionFactory());
        MatchService matchService = new MatchService(matchDAO);
        environment.jersey().register(new MatchResource(matchService));
    }
}
