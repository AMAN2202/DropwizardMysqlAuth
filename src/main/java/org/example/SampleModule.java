package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import org.example.db.EmployeeDAO;
import org.example.db.EmployeeDAOImpl;
import org.example.model.Employee;
import org.hibernate.SessionFactory;

public class SampleModule extends AbstractModule {


    @Override
    protected void configure() {


//

        bind(String.class).annotatedWith(Names.named("template")).toInstance("hello, %s");
        bind(String.class).annotatedWith(Names.named("defualtName")).toInstance("Stranger");



    }



}
