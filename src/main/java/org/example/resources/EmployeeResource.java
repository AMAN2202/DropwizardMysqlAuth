package org.example.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import org.example.DropwizardSkolException;
import org.example.model.Employee;
import org.example.service.EmployeeService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Set;

@RolesAllowed("Admin")
@Path("/employee")
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource {

    private final EmployeeService employeeService;

    public EmployeeResource(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GET
    @Timed
    public Response getAllEmployee() {
        return Response.ok(employeeService.getAllEmployee()).build();
    }


    @GET
    @Timed
    @Path("/{id}")
    public Response getEmployee(@PathParam("id") long id) throws DropwizardSkolException {

        Employee employee;
        try{
            employee=employeeService.getEmployee(id);
        }
        catch (Exception e)
        {
            return Response.status(404).entity(new ErrorMessage("Employee not found")).build();
        }
        return Response.ok(employee).build();
    }


    @POST
    @Timed
    public void addEmployee(Employee employee) {
        employeeService.addEmployee(employee);
    }

    @PUT
    @Path("/{id}")
    public Response getEmployee(@PathParam("id") long id, Employee employee) {
        employee.setId(id);
        Employee emp;
        try{
            emp=employeeService.getEmployee(id);
        }
        catch (Exception e)
        {
            return Response.status(404).entity(new ErrorMessage("Employee not found")).build();
        }
        employeeService.updateEmployee(employee);
        return  Response.ok("Updated Employee record Sucessfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCountry(@PathParam("id") int id) {
        Employee employee;
        try{
            employee=employeeService.getEmployee(id);
        }
        catch (Exception e)
        {
            return Response.status(404).entity(new ErrorMessage("Employee not found")).build();
        }
        employeeService.deleteEmployee(id);
        return  Response.ok("Deleted Employee record Sucessfully").build();
    }

}
