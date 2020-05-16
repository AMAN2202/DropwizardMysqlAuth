package org.example;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.example.auth.AppAuthorizer;
import org.example.auth.AppBasicAuthenticator;
import org.example.db.User;
import org.example.db.UserDAO;
import org.example.db.UserService;
import org.example.resources.HelloDropWizardResource;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

public class DropwizardAuthApplication extends Application<DropwizardAuthConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DropwizardAuthApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropwizardAuth";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardAuthConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final DropwizardAuthConfiguration config,
                    final Environment environment) {
        // TODO: implement application
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, config.getDatabase(), "dbb");

        final UserDAO userDAO=jdbi.onDemand(UserDAO.class);


        environment.jersey().register(new HelloDropWizardResource("%s","aman"));


        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new AppBasicAuthenticator(new UserService(userDAO)))
                .setAuthorizer(new AppAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }

}
