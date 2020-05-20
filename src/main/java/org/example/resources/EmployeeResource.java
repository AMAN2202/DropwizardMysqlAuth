package org.example.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.errors.ErrorMessage;
import lombok.AllArgsConstructor;
import org.example.DropwizardSkolException;
import org.example.model.Employee;
import org.example.service.EmployeeService;
import org.example.service.SalaryService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RolesAllowed("Admin")
@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class EmployeeResource {

    @Inject
    private final EmployeeService employeeService;


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
            e.printStackTrace();
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

}
