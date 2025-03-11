package nl.ajax.alert;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.ajax.alert.AjaxAlertingConfiguration;
import nl.ajax.alert.core.MatchService;
import nl.ajax.alert.resources.MatchResource;

public class AjaxAlertingApplication extends Application<AjaxAlertingConfiguration> {

    public static void main(final String[] args) throws Exception {
        new AjaxAlertingApplication().run(args);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void initialize(final Bootstrap<AjaxAlertingConfiguration> bootstrap) {
    }

    @Override
    public void run(final AjaxAlertingConfiguration configuration,
                    final Environment environment) {
        MatchService matchService = new MatchService();
        environment.jersey().register(new MatchResource(matchService));
    }

}
