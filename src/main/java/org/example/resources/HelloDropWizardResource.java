package org.example.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import org.example.api.Saying;
import org.example.model.User;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@RolesAllowed("Admin")
@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloDropWizardResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloDropWizardResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@Auth User user) {

        final String value;
        if (user != null)
            value = String.format(template, user.getName());
        else
            value = String.format(template, defaultName);
        return new Saying(counter.getAndIncrement(), value);
    }
}
