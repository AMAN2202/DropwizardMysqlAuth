package org.example.empApi.bindings;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.dropwizard.hibernate.HibernateBundle;
import org.example.empApi.config.DropwizardAuthConfiguration;
import org.example.empApi.db.EmployeeDAO;
import org.example.empApi.db.EmployeeDAOImpl;
import org.hibernate.SessionFactory;

public class SampleModule extends AbstractModule {
    final DropwizardAuthConfiguration configuration;
    final HibernateBundle<DropwizardAuthConfiguration> hibernate;

    public SampleModule(DropwizardAuthConfiguration configuration, HibernateBundle<DropwizardAuthConfiguration> hibernate) {
        this.configuration = configuration;
        this.hibernate = hibernate;
    }


    @Override
    protected void configure() {

        bind(SessionFactory.class).toInstance(hibernate.getSessionFactory());

        helloWorldApiBindings();
        employeeApiBinding();


    }

    private void employeeApiBinding() {


        bind(EmployeeDAO.class).to(EmployeeDAOImpl.class);
    }

    private void helloWorldApiBindings() {

        bind(String.class).annotatedWith(Names.named("template")).toInstance(configuration.getTemplate());
        bind(String.class).annotatedWith(Names.named("defualtName")).toInstance(configuration.getDefaultName());
    }


}
