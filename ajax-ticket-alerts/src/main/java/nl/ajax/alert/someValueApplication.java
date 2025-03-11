package nl.ajax.alert;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class someValueApplication extends Application<someValueConfiguration> {

    public static void main(final String[] args) throws Exception {
        new someValueApplication().run(args);
    }

    @Override
    public String getName() {
        return "someValue";
    }

    @Override
    public void initialize(final Bootstrap<someValueConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final someValueConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
