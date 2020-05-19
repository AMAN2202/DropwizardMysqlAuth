package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class SampleModule extends AbstractModule {
    @Override
    protected void configure() {



        bind(String.class).annotatedWith(Names.named("template")).toInstance("hello ,%s");
        bind(String.class).annotatedWith(Names.named("defualtName")).toInstance("Stranger");

    }


}
