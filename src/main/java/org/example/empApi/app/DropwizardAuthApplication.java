package org.example.empApi.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.netflix.config.ConfigurationManager;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.apache.commons.configuration.MapConfiguration;
import org.example.authApi.*;
import org.example.empApi.bindings.SampleModule;
import org.example.empApi.config.DropwizardAuthConfiguration;
import org.example.empApi.exception.DropwizardSkolExceptionMapper;
import org.example.empApi.model.Employee;
import org.example.empApi.resources.EmployeeResource;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

public class DropwizardAuthApplication extends Application<DropwizardAuthConfiguration> {

    private final HibernateBundle<DropwizardAuthConfiguration> hibernate = new HibernateBundle<DropwizardAuthConfiguration>(Employee.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(DropwizardAuthConfiguration configuration) {
            return configuration.getDatabase();
        }
    };


    public static void main(final String[] args) throws Exception {
        new DropwizardAuthApplication().run(args);
    }

    @Override
    public String getName() {
        return "DropwizardAuth";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardAuthConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(new SwaggerBundle<DropwizardAuthConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DropwizardAuthConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

    }

    @Override
    public void run(final DropwizardAuthConfiguration config,
                    final Environment environment) {


        //TODO: Hystrix related
        ConfigurationManager.install(new MapConfiguration(config.getDefaultHystrixConfig())); //hystrix configuration


        //TODO: Database related
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, config.getDatabase(), "dbb");
        final UserDAO userDAO = jdbi.onDemand(UserDAO.class);

//        final EmployeeDAOImpl employeeDAOImpl = new EmployeeDAOImpl(hibernate.getSessionFactory());


        //TODO: Dependency Injection related
        Injector injector = Guice.createInjector(new SampleModule(config, hibernate));


        environment.jersey().register(injector.getInstance(EmployeeResource.HelloDropWizardResource.class));
        environment.jersey().register(injector.getInstance(EmployeeResource.class));

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(new AppBasicAuthenticator(new UserService(userDAO)))
                .setAuthorizer(new AppAuthorizer())
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()));

        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(new DropwizardSkolExceptionMapper());


    }

}
