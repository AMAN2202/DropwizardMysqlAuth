package org.example.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.dropwizard.auth.Auth;
import org.example.api.Saying;
import org.example.model.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;



@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloDropWizardResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter = new AtomicLong();

    @Inject
    public HelloDropWizardResource(@Named("template") String template, @Named("defualtName") String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
    }


    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        return new Saying(counter.getAndIncrement(), String.format(template, name.orElse(defaultName)));
    }
}
