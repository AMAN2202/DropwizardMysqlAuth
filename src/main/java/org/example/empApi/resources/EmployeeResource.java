package org.example.empApi.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.name.Named;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.empApi.exception.DropwizardSkolException;
import org.example.empApi.model.Employee;
import org.example.empApi.model.Saying;
import org.example.empApi.service.EmployeeService;
import org.example.empApi.service.SalaryService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RolesAllowed("Admin")
@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
@NoArgsConstructor
@Setter
@Api
public class EmployeeResource {

    @Inject
    private EmployeeService employeeService;


    @GET
    @Timed
    @UnitOfWork
    public Response getAllEmployee() {
        return Response.ok(employeeService.getAllEmployee()).build();
    }


    @GET
    @Timed
    @Path("/{id}")
    @UnitOfWork
    public Response getEmployee(@PathParam("id") long id) throws DropwizardSkolException {

        Employee employee;
        try {
            employee = employeeService.getEmployee(id);
        } catch (Exception e) {
            return Response.status(404).entity(new ErrorMessage("Employee not found")).build();
        }
        long updated_salary = Long.valueOf(new SalaryService(employee.getSalary()).execute());
        employee.setSalary(updated_salary);
        return Response.ok(employee).build();
    }


    @POST
    @Timed
    @UnitOfWork
    public void addEmployee(Employee employee) {
        employeeService.addEmployee(employee);
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    public Response getEmployee(@PathParam("id") long id, Employee employee) {
        employee.setId(id);
        try {
            employeeService.updateEmployee(employee);
        } catch (Exception e) {
            return Response.status(404).entity(new ErrorMessage("Employee not found")).build();
        }
        return Response.ok("Updated Employee record Sucessfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response deleteEmloyee(@PathParam("id") int id) {
        try {
            employeeService.deleteEmployee(id);
        } catch (Exception e) {
            return Response.status(404).entity(new ErrorMessage("Employee not found")).build();
        }
        return Response.ok("Deleted Employee record Sucessfully").build();
    }

    @Path("/hello-world")
    @Produces(MediaType.APPLICATION_JSON)
    @Api
    public static class HelloDropWizardResource {
        private final String template;
        private final String defaultName;
        private final AtomicLong counter = new AtomicLong();

        @com.google.inject.Inject
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
}
